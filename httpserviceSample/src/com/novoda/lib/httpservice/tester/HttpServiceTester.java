
package com.novoda.lib.httpservice.tester;

import static com.novoda.lib.httpservice.tester.util.Log.v;
import android.app.Application;

/**
 * @author luigi.agosti
 */
public class HttpServiceTester extends Application {

    private static HttpServiceTester instance;
    
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        v("============================================");
        v("Create event : Start up");
        instance = this;
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        v("Low memory waring.");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        v("On terminate : Shutting down");
        v("============================================");
    }

    public static HttpServiceTester getInstance() {
        return instance;
    }

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
