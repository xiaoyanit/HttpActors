
package com.novoda.lib.httpservice.example;

import com.novoda.lib.httpservice.example.auth.AuthenticationService;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.Smoke;

import java.util.concurrent.CountDownLatch;

@Smoke
public class Launcher extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        Runtime.getRuntime().exec("setprop log.tag.httpservice VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-provider VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-bus VERBOSE");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-con VERBOSE");
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        Runtime.getRuntime().exec("setprop log.tag.httpservice INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-provider INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-bus INFO");
        Runtime.getRuntime().exec("setprop log.tag.httpservice-con INFO");
        super.tearDown();
    }

    @LargeTest
    @Smoke
    public void testTwitterTrend() throws Exception {
        Intent intent = new Intent("GET", Uri.parse("http://api.twitter.com/1/trends.json"));

        intent.setClass(getInstrumentation().getContext(),
                com.novoda.lib.httpservice.HttpService.class);

        assertNotNull(getInstrumentation().getContext().startService(intent));
        Thread.sleep(3000);
    }

    public void testOAuthProcessor() throws Exception {
        // Activating the callback on the processor...
        // this should log `BasicOAuthProcessor: Account found for type novoda.test.httpservice, account_test`
        AccountManager accountManager = AccountManager.get(getInstrumentation().getContext());
        AccountManagerFuture<Bundle> addAccount = accountManager.addAccount(
                AuthenticationService.TEST_ACCOUNT.type, "test", null, null, null, null,
                new Handler());
        addAccount.getResult();
    }
}
