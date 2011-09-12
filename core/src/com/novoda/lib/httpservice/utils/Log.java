
package com.novoda.lib.httpservice.utils;

import android.content.IntentFilter;
import android.content.IntentFilter.AuthorityEntry;

import java.util.Iterator;

public class Log {
	
	//private static final int VERBOSE = android.util.Log.VERBOSE;
	//private static final int INFO = android.util.Log.INFO;
	//private static final int ERROR = android.util.Log.ERROR;
	//private static final int WARN = android.util.Log.WARN;
	private static final int ERROR = android.util.Log.ERROR;
	private static final int WARN = android.util.Log.VERBOSE;
	private static final int INFO = android.util.Log.VERBOSE;
	private static final int VERBOSE = android.util.Log.VERBOSE;

    // Default
    public static final String TAG = "httpservice";

    public static final boolean verboseLoggingEnabled() {
        return android.util.Log.isLoggable(TAG, VERBOSE);
    }

    public static final boolean infoLoggingEnabled() {
        return android.util.Log.isLoggable(TAG, INFO);
    }

    public static final boolean errorLoggingEnabled() {
        return android.util.Log.isLoggable(TAG, ERROR);
    }

    public static final void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static final void e(String msg, Throwable t) {
        android.util.Log.e(TAG, msg, t);
    }

    public static final void w(String msg, Throwable t) {
        android.util.Log.w(TAG, msg, t);
    }

    public static final void v(String msg) {
        android.util.Log.v(TAG, msg);
    }

    public static final void i(String msg) {
        android.util.Log.i(TAG, msg);
    }

    public static class Provider {

        private static final String TAG = "httpservice-provider";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, ERROR);
        }

        public static final boolean warnLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, WARN);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }

        public static final void w(String msg) {
            android.util.Log.w(TAG, msg);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable t) {
            android.util.Log.e(TAG, msg, t);
        }
    }

    public static class Processor {

        private static final String TAG = "httpservice-processor";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final boolean infoLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, INFO);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, ERROR);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable t) {
            android.util.Log.e(TAG, msg, t);
        }

        public static final void w(String msg, Throwable t) {
            android.util.Log.w(TAG, msg, t);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }

        public static final void i(String msg) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static class Storage {

        private static final String TAG = "httpservice-storage";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, ERROR);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable t) {
            android.util.Log.e(TAG, msg, t);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static class Bus {

        private static final String TAG = "httpservice-bus";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final boolean infoLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, INFO);
        }

        public static final boolean errorLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, ERROR);
        }

        public static final boolean warnLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, WARN);
        }

        public static final void e(String msg) {
            android.util.Log.e(TAG, msg);
        }

        public static final void e(String msg, Throwable t) {
            android.util.Log.e(TAG, msg, t);
        }

        public static final void w(String msg) {
            android.util.Log.w(TAG, msg);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }

        public static final void i(String msg) {
            android.util.Log.i(TAG, msg);
        }
    }

    public static class Registry {

        private static final String TAG = "httpservice-registry";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static class Con {

        private static final String TAG = "httpservice-con";

        public static final boolean verboseLoggingEnabled() {
            return android.util.Log.isLoggable(TAG, VERBOSE);
        }

        public static final void v(String msg) {
            android.util.Log.v(TAG, msg);
        }
    }

    public static String filterToString(IntentFilter filter) {
        StringBuilder builder = new StringBuilder("IntentFilter[");
        Iterator<String> actions = filter.actionsIterator();
        builder.append("actions: ");
        while (actions!= null && actions.hasNext()) {
            builder.append(actions.next()).append(",");
        }
        Iterator<String> data = filter.typesIterator();
        builder.append(";types: ");
        while (data!= null && data.hasNext()) {
            builder.append(data.next()).append(",");
        }
        builder.append(";auth: ");
        Iterator<AuthorityEntry> auth = filter.authoritiesIterator();
        while (auth != null && auth.hasNext()) {
            AuthorityEntry e = auth.next();
            builder.append(e.getHost()).append(",");
        }
        builder.append("]");
        return builder.toString();
    }
}
