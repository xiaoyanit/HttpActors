
package com.novoda.lib.httpservice.example;

import com.novoda.lib.httpservice.HttpService;
import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.receiver.ProgressableReceiver;
import com.novoda.lib.httpservice.utils.Log;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

public class PausableFileDownload extends Activity {

    private ProgressBar progressBar;

    private Button pause;

    private Button start;

    protected Messenger actor;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("" + msg);
            Message msg2 = Message.obtain();
            msg2.what = 1465162;
            if (msg.replyTo != null)
                actor = msg.replyTo;
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pausable_file_download);
        msg = new Messenger(handler);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        final ProgressableReceiver pv = new ProgressableReceiver(new Handler(), progressBar);
        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(pauseListener);
        start = (Button) findViewById(R.id.download);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Intent intent = new Intent("GET", Uri
                        .parse("http://dl.dropbox.com/u/615212/novoda-HttpService-0.1.0-0-g6e51d8b.zip"));
                intent.setClass(PausableFileDownload.this, HttpService.class);
                intent.putExtra("test", pv);
                intent.putExtra("handler", msg);
                startService(intent);
            }
        });
    }

    OnClickListener pauseListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (actor != null) {
                Message mesg = handler.obtainMessage();
                mesg.what = 11564981;
                try {
                    actor.send(mesg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Message mesg = handler.obtainMessage();
            mesg.what = 1;
            try {
                msg.send(mesg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private Messenger msg;
}
