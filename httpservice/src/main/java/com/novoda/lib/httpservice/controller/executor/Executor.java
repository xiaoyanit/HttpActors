package com.novoda.lib.httpservice.controller.executor;

import java.util.concurrent.Callable;


public interface Executor {

	void shutdown();

	void addCallable(Callable<Void> callable);
	
	boolean isWorking();
	
	void start();
	
	void pause();

	void onLowMemory();

}
