
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PatternMatcher;
import android.os.RemoteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class FileActor extends Actor implements ResumableActor {

    public static final String DOWNLOAD_COMPLETE = "com.novoda.lib.httpservice.action.DOWNLOAD_COMPLETE";

    public static final String DOWNLOAD_FAILED = "com.novoda.lib.httpservice.action.DOWNLOAD_FAILED";
    
    public static IntentFilter DOWNLOAD_FAILED_FILTER = new IntentFilter(); static {
    	DOWNLOAD_FAILED_FILTER.addAction(FileActor.DOWNLOAD_FAILED);
	    DOWNLOAD_FAILED_FILTER.addDataScheme("http");
	    DOWNLOAD_FAILED_FILTER.addDataPath("*", PatternMatcher.PATTERN_SIMPLE_GLOB);
    }

    public static final String DOWNLOAD_DIRECTORY_PATH_EXTRA = "downloadDirectoryPath";

    public static final String FILE_NAME_EXTRA = "fileName";

    public static final String WRITE_TO = "writeToUri";

    public static final String EXCEPTION_MESSAGE_EXTRA = "exception";

    public static final String ERROR_TYPE_EXTRA = "errorType";

    public static final String CANCELLABLE_EXTRA = "cancellable";

    public static final String EXCEPTION_TYPE_EXTRA = "exceptionType";

    private RandomAccessFile file;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i("Cancelling download for " + getIntent());
                if (response != null) {
                    response.getEntity().consumeContent();
                }
                this.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }
    };

    private Messenger cancel = new Messenger(handler);

    private HttpResponse response;

    @Override
    public void onPreprocess(HttpUriRequest method, HttpContext context) {
        super.onPreprocess(method, context);
    }

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {
    	OutputStream out = null;
        if (Log.infoLoggingEnabled()) {
            Log.i("Downloading " + getIntent().getDataString() + " to "
                    + getIntent().getStringExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA));
        }
        try {
            if (getIntent().hasExtra(CANCELLABLE_EXTRA)) {
                registerForCancel();
                this.response = httpResponse;
            }
            out = getOutputStream();
            httpResponse.getEntity().writeTo(out);
            broadcastFinishedDownload();
        } catch (FileNotFoundException e) {
            broadcastDownloadFailed(e, -1);
        } catch (IOException e) {
            // closed stream usually because of a cancel request
            if (e.getMessage() != null && e.getMessage().contains("closed")) {
                broadcastDownloadFailed(e, 2);
            } else {
                broadcastDownloadFailed(e, 1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("Got a cancel?" + e);
        } finally {
        	if (out!=null){
        		try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        super.onResponseReceived(httpResponse);
    }

    private void registerForCancel() {
        Messenger receiver = getIntent().getParcelableExtra(CANCELLABLE_EXTRA);
        Message msg = Message.obtain();
        msg.replyTo = cancel;
        try {
            receiver.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void broadcastDownloadFailed(Throwable t, int type) {
        Intent intent = getIntent();
        intent.setAction(DOWNLOAD_FAILED);
        intent.putExtra(EXCEPTION_MESSAGE_EXTRA, t.getMessage());
        intent.putExtra(EXCEPTION_TYPE_EXTRA, type);
        intent.setComponent(null);
        broadcast(intent);
        if (Log.errorLoggingEnabled()) {
            Log.e("Download failed for " + intent.getDataString()  + " " + t.getMessage());
        }
    }

    private void broadcast(Intent intent) {
        if (Log.infoLoggingEnabled()) {
            Log.i("Broadcasting " + intent);
        }
        getHttpContext().sendBroadcast(intent);
    }

   private OutputStream getOutputStream() throws FileNotFoundException {
        if (getIntent().hasExtra(WRITE_TO)) {
            Uri writeTo = getIntent().getParcelableExtra(WRITE_TO);
            return getHttpContext().getContentResolver().openOutputStream(writeTo);
        }
        OutputStream stream = new FileOutputStream(getFile());
        return stream;
    }

    private void broadcastFinishedDownload() {
        Intent intent = getIntent();
        Uri uri = null;
        if (getIntent().hasExtra(WRITE_TO)) {
            uri = getIntent().getParcelableExtra(WRITE_TO);
        } else {
            uri = new Uri.Builder().scheme(ContentResolver.SCHEME_FILE)
                    .appendEncodedPath(getFile().getAbsolutePath()).build();
            intent.putExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA, getFile().getAbsolutePath());
        }
        intent.setAction(DOWNLOAD_COMPLETE);
        intent.setData(uri);
        intent.setComponent(null);
        broadcast(intent);
    }

    @Override
    public void onCreate(Bundle bundle) {
        // try {
        // // file = new RandomAccessFile(getFile(), "rw");
        // } catch (FileNotFoundException e) {
        // e.printStackTrace();
        // }
        super.onCreate(bundle);
    }

    @Override
    public void onBytesReceived(byte[] bytes) throws IOException {
        file.seek(length());
        file.write(bytes);
    }

    @Override
    public void onAllBytesReceived() {
        try {
            file.close();
        } catch (IOException e) {
            throw new FileActorException();
        }
    }

    @Override
    public long length() {
        try {
            return file.length();
        } catch (IOException e) {
            throw new FileActorException();
        }
    }

    protected File getFile() {
        String file = getIntent().getStringExtra(DOWNLOAD_DIRECTORY_PATH_EXTRA);
        File fileFile = new File(file);
        fileFile.mkdirs();
        return new File(fileFile, getIntent().getData().getLastPathSegment());
    }

    @Override
    public void onThrowable(Throwable throwable) {
        broadcastDownloadFailed(throwable, 1);
        super.onThrowable(throwable);
    }
}
