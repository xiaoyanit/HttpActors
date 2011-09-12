
package com.novoda.lib.httpservice;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;

import java.util.concurrent.Callable;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.ActorNotFoundException;
import com.novoda.lib.httpservice.actor.factory.ProgrammaticActorFactory;
import com.novoda.lib.httpservice.actor.factory.XmlActorFactory;
import com.novoda.lib.httpservice.configuration.HttpServiceComponent;
import com.novoda.lib.httpservice.configuration.ProcessorComponent;
import com.novoda.lib.httpservice.controller.CallableWrapper;
import com.novoda.lib.httpservice.controller.LifecycleManager;
import com.novoda.lib.httpservice.controller.executor.ConnectedMultiThreadExecutor;
import com.novoda.lib.httpservice.controller.executor.Executor;
import com.novoda.lib.httpservice.processor.Processor;
import com.novoda.lib.httpservice.provider.Provider;
import com.novoda.lib.httpservice.provider.http.HttpProvider;
import com.novoda.lib.httpservice.storage.InMemoryStorage;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.utils.Log;

/**
 * @author luigi@novoda.com
 */
public class HttpService extends Service {

    private static final String META_DATA_KEY = "com.novoda.lib.httpservice";

    private Provider provider;

    private ActorFactory actorFactory;

    private Storage storage;

    private LifecycleManager lifecycleManager;

    private Executor executor;

    public HttpService() {
        super();
        this.provider = getProvider();
        this.actorFactory = getActorFactory();
        this.storage = getStorage();
        initLifecycleManager();
        initExecutorManager();
    }

    protected ActorFactory getActorFactory() {
        return new ProgrammaticActorFactory();
    }

    protected Provider getProvider() {
        if (provider == null)
            provider = new HttpProvider();
        return provider;
    }

    protected Storage getStorage() {
        return new InMemoryStorage();
    }

    private void initLifecycleManager() {
        this.lifecycleManager = new LifecycleManager() {
            @Override
            protected boolean isWorking() {
                return HttpService.this.isWorking();
            }

            @Override
            protected void stop() {
                stopSelf();
            }
        };
    }

    private void initExecutorManager() {
        this.executor = new ConnectedMultiThreadExecutor(this);
    }

    @Override
    public void onCreate() {
        if (verboseLoggingEnabled()) {
            v("Starting HttpService");
        }
        tryConfigureViaMetaData();
        lifecycleManager.startLifeCycle();
        executor.start();
        super.onCreate();
    }

    private void tryConfigureViaMetaData() {
        try {
            ComponentName cn = new ComponentName(getBaseContext(), this.getClass());
            ServiceInfo serviceInfo = getBaseContext().getPackageManager().getServiceInfo(cn,
                    PackageManager.GET_META_DATA);
            Bundle bundle = serviceInfo.metaData;

            if (bundle != null && bundle.containsKey(META_DATA_KEY)) {
                String value = (String) bundle.get(META_DATA_KEY);
                int id = getResources().getIdentifier(value, "xml", this.getPackageName());
                actorFactory = new XmlActorFactory(getResources().getXml(id),
                        getApplicationContext());

                HttpServiceComponent config = HttpServiceComponent.fromXml(getResources()
                        .getXml(id), getApplicationContext());

                for (ProcessorComponent comp : config.processors) {
                    Processor processor = (Processor) Class.forName(comp.name).newInstance();
                    processor.attach(getApplicationContext());
                    processor.onCreate(comp.bundle);
                    Provider provider = getProvider();
                    if (provider instanceof HttpProvider) {
                        if (Log.infoLoggingEnabled()) {
                            Log.i("Adding: " + comp.name + " to list of processors " + processor);
                        }
                        ((HttpProvider) provider).setHttpProcessor(processor);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (verboseLoggingEnabled()) {
            v("Executing intent");
        }
        lifecycleManager.notifyOperation();
        executor.addCallable(getCallable(intent));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (verboseLoggingEnabled()) {
            v("Executor Service on Destroy");
        }
        lifecycleManager.stopLifeCycle();
        if (executor != null) {
            executor.shutdown();
        }
        if(provider != null) {
        	provider.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        executor.onLowMemory();
        provider.onLowMemory();
        super.onLowMemory();
    }

    private Callable<Void> getCallable(Intent intent) {
        if (verboseLoggingEnabled()) {
            v("Building up a callable with the provider and the intentWrapper");
        }
        Actor actor;
        try {
            actor = actorFactory.getActor(intent, storage);
            actor.sendEmptyMessage(Actor.ON_CREATE);
            return new CallableWrapper(provider, actor);
        } catch (ActorNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isWorking() {
        return executor.isWorking();
    }

}
