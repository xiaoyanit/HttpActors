
package com.novoda.lib.httpservice.utils;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;

public class BundleUtils {

    public static Bundle fromXml(XmlPullParser parser) {
        Bundle bundle = new Bundle();
        AttributeSet attr = Xml.asAttributeSet(parser);
        for (int i = 0; i < attr.getAttributeCount(); i++) {
            bundle.putString(attr.getAttributeName(i), attr.getAttributeValue(i));
        }
        return bundle;
    }
}
