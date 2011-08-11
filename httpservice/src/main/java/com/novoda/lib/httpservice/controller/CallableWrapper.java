package com.novoda.lib.httpservice.controller;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;

import java.util.concurrent.Callable;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.exception.HandlerException;
import com.novoda.lib.httpservice.provider.Provider;

/**
 * Wrapper for the callable.
 * 
 * @author luigi@novoda.com
 *
 */
public class CallableWrapper implements Callable<Void> {
	
	private Actor actor;
	private Provider provider;
	
	public CallableWrapper(Provider provider, Actor actor) {
		if(actor == null) {
			throw new HandlerException("Actor is null!");
		}
		if(provider == null) {
			throw new HandlerException("Provider is null!");
		}
		this.actor = actor;
		this.provider = provider;
	}
	
	@Override
	public Void call() throws Exception {
		if(verboseLoggingEnabled()) {
			v("Executing request : " + actor);
		}
		provider.execute(actor);
		if(verboseLoggingEnabled()) {
			v("Response received");
		}
		return null;
	}

}
