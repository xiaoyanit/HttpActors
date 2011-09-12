package com.novoda.lib.httpservice.tester.service;

import android.content.Intent;

import com.novoda.lib.httpservice.HttpService;
import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.actor.ZipActor;
import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.storage.DatabaseStorage;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.tester.util.Log;

public class AppService extends HttpService {
	
	public static final String TYPE1 = "vnd.android.cursor.dir/vnd.httptester.actor1";
	public static final String TYPE2 = "vnd.android.cursor.dir/vnd.httptester.actor2";
	public static final String ZIP = "vnd.android.cursor.dir/vnd.httptester.zip";
	
	@Override
	protected Storage getStorage() {
		return new DatabaseStorage();
	}
	
	// This is just a sample to show how to implement a 
	// switcher on the actors
	// Check the manifest intent-filters
	@Override
	protected ActorFactory getActorFactory() {
		Log.dev("getting actor factory");
		return new ActorFactory() {
			@Override
			public Actor getActor(Intent intent, Storage storage) {
				Actor actor = null;
				if(TYPE1.equals(intent.getType())) {
					Log.v("Actor1 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else if(TYPE2.equals(intent.getType())) {
					Log.v("Actor2 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else if(ZIP.equals(intent.getType())) {
					Log.v("Zip defined for intent : " + intent.toString());
					actor = new ZipActor(intent, storage);
				} else {
					Log.v("No actor defined for intent : " + intent.toString() + ", " + intent.getType());
					actor =  new LoggingActor(intent, storage);
				}
				actor.applyContext(new ContextHttpWrapper(AppService.this.getApplicationContext()));
				return actor;
			}
		};
	}
	
	@Override
	public void onLowMemory() {
		Log.dev("onLowMemory on AppService");
		super.onLowMemory();
	}
	
}
