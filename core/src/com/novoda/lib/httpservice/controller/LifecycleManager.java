package com.novoda.lib.httpservice.controller;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;
import static com.novoda.lib.httpservice.utils.Log.w;

import java.util.Timer;
import java.util.TimerTask;

import com.novoda.lib.httpservice.Settings;

public abstract class LifecycleManager {
	
	private long lastCall;
	
	private Timer timer;
	
	public void notifyOperation() {
		lastCall = System.currentTimeMillis();
	}
	
	public void startLifeCycle() {	
		try {
			lastCall = System.currentTimeMillis();
			timer = new Timer();
			TimerTask monitorThread = new TimerTask() {
				@Override
				public void run() {
					boolean working = isWorking();
					long delta = System.currentTimeMillis() - lastCall;
					if (verboseLoggingEnabled()) {
						v("Lifecycle manager: working? " + working + " last execution was? " + delta);
					}
					if (working || delta < Settings.KEEP_ALIFE_TIME) {
						if (verboseLoggingEnabled()) {
							v("Lifecycle manager: keeping alive the service");
						}      
					} else {
						if (verboseLoggingEnabled()) {
							v("Lifecycle manager: stoping service");
						}		
						stopLifeCycle();
						stop();
					}
				}
			};
			timer.schedule(monitorThread, 0, Settings.SERVICE_LIFECYCLE);
		} catch(Throwable t) {
			w("Lifecycle manager: Scheduling timer already scheduled", t);
		}
	}

	public void stopLifeCycle() {
		try {
			if (verboseLoggingEnabled()) {
				v("Lifecycle manager: removing timer");
			}
			timer.cancel();
			timer.purge();
		} catch(Throwable t) {
			w("Lifecycle manager: Cancel on a not scheduled timer", t);
		}
	}

	protected abstract void stop();
	
	protected abstract boolean isWorking();
	
}
