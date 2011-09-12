
package com.novoda.lib.httpservice.configuration;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.processor.BasicOAuthProcessor;
import com.novoda.lib.httpservice.utils.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Xml;

import java.lang.reflect.Field;

public class HttpServiceComponentTest extends XmlTestCase {

    public void testParsingStandardXML() throws Exception {
        HttpServiceComponent cp = HttpServiceComponent.fromXml(getXmlPullParser(R.xml.stdconfig),
                getInstrumentation().getTargetContext());
        assertNotNull(cp);
        assertEquals(BasicOAuthProcessor.class.getCanonicalName(), cp.processors.get(0).name);
    }

    public void testAttributeSetLogic() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        XmlPullParser parser = getXmlPullParser(R.xml.nonstdconfig);
        int eventType = parser.getEventType();
        String name;
        do {

            AttributeSet a1 = Xml.asAttributeSet(parser);
            for (int i = 0; i < a1.getAttributeCount(); i++) {
                Log.i("a1.getAttributeValue(0) count : " + a1.getAttributeName(i) + " "
                        + a1.getAttributeValue(i) + a1.getClassAttribute());
            }

            name = parser.getName();

            Log.i("name: " + name);
            if (name != null && name.endsWith("Processor")) {
                Log.i("id: "
                        + getInstrumentation()
                                .getContext()
                                .getResources()
                                .getIdentifier("Processor_name", "styleable",
                                        "com.novoda.lib.httpservice"));

            }

            // Class<?> forName =
            // Class.forName("com.novoda.lib.httpservice.R$styleable");
            // for (Field f : forName.getFields()) {
            // if (f.get(forName).getClass().isArray()) {
            // Log.i("Name: " + f.getName());
            // int[] i = (int[]) f.get(forName);
            // TypedArray a = context.getResources().obtainAttributes(
            // Xml.asAttributeSet(parser), i);
            // Log.i("TESF" + i[0]);
            // Log.i("GF" + a.getPositionDescription());
            // Log.i("GF" + a.getString(R.styleable.Processor_name));
            // }
            // }

            XmlUtils.nextElement(parser);
            eventType = parser.getEventType();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        // TypedArray ta = context.obtainStyledAttributes(
        // Xml.asAttributeSet(, R.styleable.Actor);
        //
        // ta.getString(R.styleable.Actor_name);
        // ta.recycle();
    }
}
