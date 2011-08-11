
package com.novoda.lib.httpservice.receiver;

import com.novoda.lib.httpservice.utils.Log;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.widget.ProgressBar;

public class ProgressableReceiver extends ResultReceiver implements Parcelable {

    public final static int SET_CONTENT_TOTAL_LENGHT = 24;

    public final static int SET_CONTENT_LENGHT = 1;

    public final static int FINISHED = 2;

    public final static int STARTED = 3;

    public static final String CONTENT_LENGHT_BUNDLE_KEY = "contentLenghtBundleKey";

    public static final String CONTENT_TOTAL_LENGHT_BUNDLE_KEY = "contentTotalLenghtBundleKey";

    private ProgressBar progressBar;

    private long total;

    public ProgressableReceiver(Handler handler, ProgressBar progressBar) {
        super(handler);
        this.progressBar = progressBar;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle data) {
        log(resultCode, data);
        Bundle resultData = new Bundle();
        if (data != null) {
            resultData.putAll(data);
        }
        switch (resultCode) {
            case STARTED:
                break;
            case FINISHED:
                progressBar.setProgress((int) (total << 2));
                break;
            case SET_CONTENT_TOTAL_LENGHT:
                total = resultData.getLong(CONTENT_TOTAL_LENGHT_BUNDLE_KEY);
                progressBar.setMax((int) (total << 2));
                break;
            case SET_CONTENT_LENGHT:
                int progress = (int) (resultData.getLong(CONTENT_LENGHT_BUNDLE_KEY) << 2);
                progressBar.setProgress(progress);
                break;
        }
        super.onReceiveResult(resultCode, resultData);
    }

    private void log(int resultCode, Bundle data) {
        Bundle resultData = new Bundle();
        if (data != null) {
            resultData.putAll(data);
        }
        if (Log.verboseLoggingEnabled()) {
            StringBuffer buf = new StringBuffer("[ProgressableView] ");
            switch (resultCode) {
                case STARTED:
                    buf.append("starting download...");
                    break;
                case FINISHED:
                    buf.append("stopped downloading..");
                    break;
                case SET_CONTENT_TOTAL_LENGHT:
                    buf.append("total lenght received of: ");
                    buf.append("" + resultData.getLong(CONTENT_TOTAL_LENGHT_BUNDLE_KEY));
                    break;
                case SET_CONTENT_LENGHT:
                    buf.append("Content lenght received of: ");
                    buf.append("" + resultData.getLong(CONTENT_LENGHT_BUNDLE_KEY));
                    buf.append(" against a total of: ");
                    buf.append(total);
                    break;
                default: { 
                	if(Log.errorLoggingEnabled()) {                	
                		Log.e("Unknown resultCode: " + resultCode);
                	}
                }
            }
            Log.v(buf.toString());
        }
    }
}
