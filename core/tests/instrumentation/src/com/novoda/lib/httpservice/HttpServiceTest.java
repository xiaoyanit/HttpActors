
package com.novoda.lib.httpservice;

import android.test.ServiceTestCase;

public class HttpServiceTest extends ServiceTestCase<HttpService> {

    public HttpServiceTest(Class<HttpService> serviceClass) {
        super(serviceClass);
    }

    public HttpServiceTest() {
        super(HttpService.class);
    }

    public void testGettingActorsByMetaData() throws Exception {
        startService(null);
    }
}
