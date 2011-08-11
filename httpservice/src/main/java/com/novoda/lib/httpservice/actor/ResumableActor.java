
package com.novoda.lib.httpservice.actor;

import java.io.IOException;

public interface ResumableActor {
    public void onBytesReceived(byte[] bytes) throws IOException;
    public void onAllBytesReceived();
    public long length();
}
