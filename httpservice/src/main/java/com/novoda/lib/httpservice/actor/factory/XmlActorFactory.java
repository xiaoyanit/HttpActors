
package com.novoda.lib.httpservice.actor.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XmlResourceParser;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.ActorNotFoundException;
import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.utils.Log;

public class XmlActorFactory implements ActorFactory {

    private Map<IntentFilter, Class<? extends Actor>> registry;

    private Context context;

    public XmlActorFactory(XmlResourceParser parser, Context context)
            throws XmlPullParserException, IOException, ClassNotFoundException {
        registry = new HashMap<IntentFilter, Class<? extends Actor>>();
        this.context = context;
        init(parser);
    }

    @Override
    public Actor getActor(Intent intent, Storage storage) throws ActorNotFoundException {
        for (Entry<IntentFilter, Class<? extends Actor>> entry : registry.entrySet()) {

        	if(Log.infoLoggingEnabled()){
        		Log.i(intent + " " + intent.getData().getAuthority() + " "
        				+ entry.getKey().authoritiesIterator().next().match(intent.getData()));
        	}

            if (entry.getKey().match(intent.getAction(), intent.getType(), intent.getScheme(),
                    intent.getData(), intent.getCategories(), Log.TAG) > 0) {
                try {
                    Actor actor =  entry.getValue().newInstance();
                    actor.setIntent(intent);
                    actor.setStorage(storage);
                    actor.applyContext(new ContextHttpWrapper(context));
                    return actor;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        throw new ActorNotFoundException("No actor found for intent: " + intent);
    }

    @SuppressWarnings("unchecked")
    private void init(XmlResourceParser xpp) throws XmlPullParserException, IOException,
            ClassNotFoundException {
        xpp.next();
        int eventType = xpp.getEventType();
        IntentFilter filter = null;
        Class<? extends Actor> klass = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                if ("Actor".equals(xpp.getName())) {
                    String cls = xpp.getAttributeValue(
                            "http://schemas.android.com/apk/res/android", "name");
                    klass = (Class<? extends Actor>) Class.forName(cls);
                } else if ("intent-filter".equals(xpp.getName())) {
                    filter = new IntentFilter();
                    filter.readFromXml(xpp);
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if ("Actor".equals(xpp.getName())) {
                    if (Log.infoLoggingEnabled()) {
                        Log.i("Adding " + Log.filterToString(filter) + " with " + klass
                                + " to registry");
                    }
                    if (filter != null && klass != null) {
                        registry.put(filter, klass);
                    } else {
                        Log.e("Can not add filter to class as one or both are null");
                    }
                }
            } else if (eventType == XmlPullParser.TEXT) {
            }
            eventType = xpp.next();
        }
    }
}
