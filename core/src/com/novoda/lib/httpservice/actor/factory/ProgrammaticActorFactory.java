package com.novoda.lib.httpservice.actor.factory;

import android.content.Intent;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.storage.Storage;

public class ProgrammaticActorFactory implements ActorFactory {

	@Override
	public Actor getActor(Intent intent, Storage storage) {
		Actor actor = new LoggingActor(intent, storage);
		actor.sendEmptyMessage(Actor.ON_CREATE);
		return actor;
	}

}
