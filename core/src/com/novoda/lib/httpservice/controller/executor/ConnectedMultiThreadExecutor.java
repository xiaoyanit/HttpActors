package com.novoda.lib.httpservice.controller.executor;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;
import static com.novoda.lib.httpservice.utils.Log.w;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.novoda.lib.httpservice.Settings;

import android.app.Service;

public class ConnectedMultiThreadExecutor implements Executor {

	private ThreadPoolExecutor poolExecutor;

	private ExecutorCompletionService<Void> completitionService;

	private Thread looperThread;
	
	private boolean runLoop = true;

	public ConnectedMultiThreadExecutor(Service service) {
		if (verboseLoggingEnabled()) {
			v("Starting thread manager");
		}
		initThreadPool(service);
		initCompletionService();
	}
	
	protected void initThreadPool(Service service) {
		this.poolExecutor = new ConnectedThreadPoolExecutor(service);
	}
	
	protected void initCompletionService() {
		this.completitionService = new ExecutorCompletionService<Void>(poolExecutor);
	}

	@Override
	public void shutdown() {
		if(poolExecutor != null) {
			if (verboseLoggingEnabled()) {
				v("Shutting down pool executor");
			}
			poolExecutor.shutdown();
			while(poolExecutor.isTerminating()) {
				if (verboseLoggingEnabled()) {
					v("Thread Manager : waiting for shut down of poolExecutor...");
				}
			}
			if (verboseLoggingEnabled()) {
				v("Thread Manager : poolExecutor is terminated...");
			}
		}
		if(looperThread != null) {
			if (verboseLoggingEnabled()) {
				v("Thread Manager : Shutting down looperThread");
			}
			runLoop = false;
			if (verboseLoggingEnabled()) {
				v("Thread Manager : looperThread is terminated");
			}
		}
	}

	@Override
	public void addCallable(Callable<Void> callable) {
		completitionService.submit(callable);
	}

	@Override
	public boolean isWorking() {
		if(poolExecutor.getActiveCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		if (verboseLoggingEnabled()) {
			v("Thread Manager : Starting Thread Loop");
		}
		looperThread = new Thread() {
			public void run() {
				Thread.currentThread().setPriority(Settings.THREAD_PRIORITY);
				if (verboseLoggingEnabled()) {
					v("Thread Manager : is running now");
				}
				while (runLoop) {
					try {
						if (verboseLoggingEnabled()) {
							v("Thread Manager : new cycle");
						}
						Future<Void> future = completitionService.take();
						future.get();
						future.cancel(true);
						if (verboseLoggingEnabled()) {
							v("Response received");
						}
					} catch (InterruptedException e) {
						w("InterruptedException", e);
					} catch (ExecutionException e) {
						w("ExecutionException", e);
					}
				}
				if (verboseLoggingEnabled()) {
					v("Thread Manager : ending cycle");
				}
			};
		};
		looperThread.start();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		// how do I transfer this event to the future?
	}

}