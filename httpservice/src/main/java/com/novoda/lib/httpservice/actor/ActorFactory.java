package com.novoda.lib.httpservice.actor;

import com.novoda.lib.httpservice.storage.Storage;

import android.content.Intent;

public interface ActorFactory {
	Actor getActor(Intent intent, Storage storage) throws ActorNotFoundException;
}
