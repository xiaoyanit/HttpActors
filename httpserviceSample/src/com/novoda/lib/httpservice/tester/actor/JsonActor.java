
package com.novoda.lib.httpservice.tester.actor;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.tester.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JsonActor extends Actor {

    @Override
    public void onResponseReceived(HttpResponse httpResponse) {
        try {
            Log.v(EntityUtils.toString(httpResponse.getEntity()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResponseReceived(httpResponse);
    }

}
