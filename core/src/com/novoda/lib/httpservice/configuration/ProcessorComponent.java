
package com.novoda.lib.httpservice.configuration;

import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ProcessorComponent extends ComponentInfo implements Parcelable {

    public Bundle bundle;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

}
