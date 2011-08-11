
package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.receiver.ProgressableReceiver;
import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ResultReceiver;

import java.io.IOException;
import java.io.InputStream;

public class DownloadActor extends Actor {

    private Messenger messenger = new Messenger(this);

    private ResultReceiver progressableView;

    @Override
    public void handleMessage(Message msg) {
        Log.i(" in actor " + msg);
        super.handleMessage(msg);
    }

    @Override
    public void onCreate(Bundle bundle) {
        progressableView = getIntent().getParcelableExtra("test");
        Messenger msg = getIntent().getParcelableExtra("handler");
        try {
            Message message = Message.obtain();
            message.replyTo = messenger;
            message.what = 111111111;
            msg.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onCreate(bundle);
    }

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {

        sendMessage(ProgressableReceiver.STARTED, null);
        Header lastHeader = httpResponse.getLastHeader("content-length");

        Log.i("header " + lastHeader);
        Bundle bundle = new Bundle();
        bundle.putLong(ProgressableReceiver.CONTENT_TOTAL_LENGHT_BUNDLE_KEY,
                Long.parseLong(lastHeader.getValue()));
        sendMessage(ProgressableReceiver.SET_CONTENT_TOTAL_LENGHT, bundle);

        try {
            InputStream in = httpResponse.getEntity().getContent();
            int i = 0;
            byte[] buffer = new byte[2 * 1024];
            while (in.read(buffer) != -1) {
                bundle = new Bundle();
                bundle.putLong(ProgressableReceiver.CONTENT_LENGHT_BUNDLE_KEY, ++i * 2 * 1024);
                sendMessage(ProgressableReceiver.SET_CONTENT_LENGHT, bundle);
            }
            sendMessage(ProgressableReceiver.FINISHED, null);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResponseReceived(httpResponse);
    }

    private void sendMessage(int started, Bundle bundle) {
        // if (messenger != null) {
        // try {
        // Message msg = Message.obtain(this);
        // msg.setData(bundle);
        // messenger.send(msg);
        // } catch (RemoteException e) {
        // e.printStackTrace();
        // }
        // }
        if (progressableView != null) {
            progressableView.send(started, bundle);
        }
    }

}
