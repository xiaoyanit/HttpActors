
package com.novoda.lib.httpservice.net;

import com.novoda.lib.httpservice.receiver.ProgressableReceiver;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;
import java.io.InputStream;

public class ProgressableEntityWrapper extends HttpEntityWrapper {

    public static int BUFFER_SIZE = 10;

    public long contentlenght = -1;

    public long currentProgress = 0;

    private ResultReceiver prorgess;

    public ProgressableEntityWrapper(HttpEntity wrapped, ResultReceiver progressableView) {
        super(wrapped);
        contentlenght = wrapped.getContentLength();
        this.prorgess = progressableView;
    }

    public long getProgress() {
        return currentProgress;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new ProgressableInputStream(super.getContent());
    }

    private class ProgressableInputStream extends InputStream {

        private InputStream wrappedStream;

        public ProgressableInputStream(InputStream wrappedStream) {
            this.wrappedStream = wrappedStream;
        }

        @Override
        public int read() throws IOException {
            return wrappedStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            currentProgress += b.length;
            Bundle bundle = new Bundle();
            bundle.putLong(ProgressableReceiver.CONTENT_LENGHT_BUNDLE_KEY, currentProgress);
            prorgess.send(ProgressableReceiver.SET_CONTENT_LENGHT, bundle);
            return super.read(b);
        }

        @Override
        public int read(byte[] b, int offset, int length) throws IOException {
            currentProgress += length;
            Bundle bundle = new Bundle();
            bundle.putLong(ProgressableReceiver.CONTENT_LENGHT_BUNDLE_KEY, currentProgress);
            prorgess.send(ProgressableReceiver.SET_CONTENT_LENGHT, bundle);
            return super.read(b, offset, length);
        }

        @Override
        public synchronized void reset() throws IOException {
            currentProgress = 0;
            super.reset();
        }
    }
}
