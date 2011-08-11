
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.exception.FileNotFinished;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.utils.FileReader;
import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

public class ZipActor extends LoggingActor {

    private FileReader fileReader = new FileReader();

    private long currentLenght = 0l;

    public ZipActor(Intent intent, Storage storage) {
        super(intent, storage);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onPreprocess(HttpUriRequest method, HttpContext context) {
        Cursor c = null;
        try {
            c = getHttpContext().getContentResolver().query(IntentModel.URI, null, "_id = ?",
                    new String[] {
                        "" + getIntent().filterHashCode()
                    }, null);
            if (c.moveToNext()) {
                currentLenght = c.getLong(c.getColumnIndex(IntentModel.Column.filelength));
                if(Log.verboseLoggingEnabled()) {
                	Log.v("current lenght is : " + currentLenght);
                }
            } else {
            	if(Log.verboseLoggingEnabled()) {
            		Log.v("no data in the database ");
            	}
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        String range = "bytes=" + currentLenght + "-"
                + (currentLenght + fileReader.getThreshold() + 1);
        method.addHeader("Range", range);
        super.onPreprocess(method, context);
    }

    @Override
    public void onHeaderReceived(HttpResponse response) {
        // Log.v("onHeaders receiveds : ");
        // Log.v("protocol version : " + response.getProtocolVersion());
        //
        // for(Header header : response.getAllHeaders()) {
        // Log.v("header name : " + header.getName() + " value : " +
        // header.getValue());
        // }
        //
        // HeaderIterator it = response.headerIterator("Set-Cookie");
        // while (it.hasNext()) {
        // Header h = (Header)it.next();
        // Log.v("name : " + h.getName() + " value : " + h.getValue());
        // }
        //
        // Log.v("Content Lenght : " + response.getEntity().getContentLength());
        // Log.v("Content Encoding : " +
        // response.getEntity().getContentEncoding());
        // Log.v("Content Type : " + response.getEntity().getContentType());
        // Log.v("Is repeateble : " + response.getEntity().isRepeatable());
        // Log.v("Is chuncked : " + response.getEntity().isChunked());
        // Log.v("Is streaming : " + response.getEntity().isStreaming());
    }

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {
        tryToHandleResponse(httpResponse, getIntent(), getStorage());
    }

    protected void tryToHandleResponse(HttpResponse res, Intent intent, Storage storage) {
        String filepath = null;
        Context context = getHttpContext();
        try {
        	if(Log.verboseLoggingEnabled()) {
        		Log.v("checking for intent : " + intent.filterHashCode());
        	}
            ContentValues cv = storage.getIntent(context, intent);
            if(Log.verboseLoggingEnabled()) {
            	Log.v("intent has : " + cv);
            }

            filepath = getFilepath(context, cv, intent);
            if(Log.verboseLoggingEnabled()) {
            	Log.v("filePath : " + filepath);
            }

            if (!fileReader.exists(filepath)) {
                storage.queued(context, intent);
            }
            storage.contendReceived(context, intent);
            readResponseFrom(fileReader, res, filepath);
        } catch (FileNotFinished fnf) {
            String filename = fnf.getFilename();
            if(Log.verboseLoggingEnabled()) {
            	Log.v("Part of file : " + filename + " length : " + fnf.getFileLength());
            }
            storage.updateDownload(context, intent, filename, fnf.getFileLength());
            context.startService(intent);
        }
        storage.contendConsumed(context, intent);
    }

    private void readResponseFrom(FileReader reader, HttpResponse res, String filen)
            throws FileNotFinished {
        HttpEntity entity = res.getEntity();
        try {
            InputStream is = entity.getContent();
            reader.addToFile(filen, is);
        } catch (IOException io) {
            throw new RuntimeException();
        }
    }

    private String getFilepath(Context c, ContentValues cv, Intent intent) {
        String existing = getFileName(c, cv);
        if (existing == null) {
            String filename = "download-" + intent.filterHashCode() + ".zip";
            String path = Environment.getExternalStorageDirectory() + "/" + filename;
            return path;
        }
        return existing;
    }

    private String getFileName(Context c, ContentValues cv) {
        if (cv == null) {
            return null;
        }
        String filename = cv.getAsString(IntentModel.Column.filename);
        if (TextUtils.isEmpty(filename)) {
            return null;
        }
        return filename;
    }

}
