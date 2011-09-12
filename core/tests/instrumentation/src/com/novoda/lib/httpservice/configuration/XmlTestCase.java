
package com.novoda.lib.httpservice.configuration;

import org.xmlpull.v1.XmlPullParser;

import android.test.InstrumentationTestCase;

public class XmlTestCase extends InstrumentationTestCase {

    protected XmlPullParser getXmlPullParser(int xmlResId) {
        return getInstrumentation().getContext().getResources().getXml(xmlResId);
    }
}
