package com.novoda.lib.httpservice.utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * If you need to send parameters with the request you can add a list of 
 * parcelableBasicNameValuePair directly to the intent with the IntentRequestBuilder.
 * 
 * @author luigi@novoda.com
 *
 */
public class ParcelableBasicNameValuePair extends BasicNameValuePair implements Parcelable, NameValuePair {

	private static final long serialVersionUID = 1L;

	public ParcelableBasicNameValuePair(String name, String value) {
        super(name, value);
    }

    private ParcelableBasicNameValuePair(Parcel parcel) {
        this(parcel.readString(), parcel.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int parcelableFlags) {
        parcel.writeString(getName());
        parcel.writeString(getValue());
    }

    public static final Parcelable.Creator<ParcelableBasicNameValuePair> CREATOR = new Parcelable.Creator<ParcelableBasicNameValuePair>() {
        @Override
        public ParcelableBasicNameValuePair createFromParcel(Parcel parcel) {
            return new ParcelableBasicNameValuePair(parcel);
        }

        @Override
        public ParcelableBasicNameValuePair[] newArray(int size) {
            return new ParcelableBasicNameValuePair[size];
        }
    };
}
