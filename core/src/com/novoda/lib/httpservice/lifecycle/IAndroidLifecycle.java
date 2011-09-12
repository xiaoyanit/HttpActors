
package com.novoda.lib.httpservice.lifecycle;

import android.content.ComponentCallbacks;
import android.os.Bundle;

public interface IAndroidLifecycle extends ComponentCallbacks {
    void onCreate(Bundle fromXml);

    void onResume();

    void onPause();

    void onDestroy();
}
