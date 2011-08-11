
package com.novoda.lib.httpservice;

import java.util.List;

import com.novoda.lib.httpservice.server.IntentResolver;

import android.content.Intent;
import android.content.IntentFilter;
import android.test.AndroidTestCase;
import android.util.Log;

public class IntentRegistryTest extends AndroidTestCase {

    @Override
    public void testAndroidTestCaseSetupProperly() {
        super.testAndroidTestCaseSetupProperly();
    }

    public void testSimpleRegistry() throws Exception {
        IntentResolver<Test, Test> resolver = new IntentResolver<Test, Test>();
        Test filter = new Test("test");
        
        resolver.addFilter(filter);
        
        List<Test> queryIntent = resolver.queryIntent(new Intent("test"), null, false);

        for (Test s : queryIntent) {
            Log.i("TEST", " " + s.string);
        }
        assertTrue(queryIntent.size() > 5);
        assertTrue(resolver + " ", false);
    }
    
    class Test extends IntentFilter {

        private String string;

        public Test(String string) {
            super(string);
            this.string = string;
        }
    }
}
