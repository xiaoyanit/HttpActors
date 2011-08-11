
package com.novoda.lib.httpservice.example.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class AuthenticationService extends Service {

    public static Account TEST_ACCOUNT = new Account("account_test", "novoda.test.httpservice");

    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

    public class AccountAuthenticator extends AbstractAccountAuthenticator {

        public AccountAuthenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                String authTokenType, String[] requiredFeatures, Bundle options)
                throws NetworkErrorException {

            AccountManager.get(AuthenticationService.this).addAccountExplicitly(TEST_ACCOUNT,
                    "test", null);

            // populate the results bundle
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, "account_test");
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, "novoda.test.httpservice");

            // return result
            response.onResult(result);

            return result;// result returned via response instead
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                Bundle options) throws NetworkErrorException {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                String authTokenType, Bundle options) throws NetworkErrorException {

            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            data.putString(AccountManager.KEY_AUTHTOKEN, "token=token&token_secret=secret");
            return data;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                String[] features) throws NetworkErrorException {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                String authTokenType, Bundle options) throws NetworkErrorException {
            throw new UnsupportedOperationException("not implemented");
        }
    }
}
