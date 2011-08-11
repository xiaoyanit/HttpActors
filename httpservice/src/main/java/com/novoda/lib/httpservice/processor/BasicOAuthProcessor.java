
package com.novoda.lib.httpservice.processor;

import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.os.Bundle;

import java.io.IOException;

public class BasicOAuthProcessor extends Processor implements OnAccountsUpdateListener {

    private static final String ACCOUNT_TYPE = "accountType";

    private AccountManager accountManager;

    private String accountType;

    @Override
    public void onCreate(Bundle fromXml) {
        accountType = fromXml.getString(ACCOUNT_TYPE);
        if (Log.infoLoggingEnabled()) {
            Log.i("BasicOAuthProcessor: setup against type " + accountType);
        }
        accountManager = AccountManager.get(getContext());
        Account[] accountsByType = accountManager.getAccountsByType(accountType);
        accountManager.addOnAccountsUpdatedListener(this, null, true);
        initOAuth(accountsByType);
        super.onCreate(fromXml);
    }

    private void initOAuth(Account[] accounts) {
        for (Account account : accounts) {
            if (account.type.equalsIgnoreCase(accountType)) {
                if (Log.infoLoggingEnabled()) {
                    Log.i("BasicOAuthProcessor: Account found for type " + accountType + ", "
                            + account.name);
                }
                accountManager.getAuthToken(account, accountType, true, null, null);
            }
        }
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {
        initOAuth(accounts);
    }
}
