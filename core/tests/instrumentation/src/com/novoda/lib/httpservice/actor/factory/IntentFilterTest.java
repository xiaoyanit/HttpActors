
package com.novoda.lib.httpservice.actor.factory;

import com.novoda.lib.httpservice.R;

import org.xmlpull.v1.XmlPullParser;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.test.MoreAsserts;
import android.util.StringBuilderPrinter;

import java.util.HashSet;
import java.util.Iterator;

public class IntentFilterTest extends InstrumentationTestCase {

    public void testParsingXMLAutomatically() throws Exception {

        XmlResourceParser xml = getInstrumentation().getContext().getResources()
                .getXml(R.xml.intentfiltertest);

        final IntentFilter filter = new IntentFilter();

        int type;
        while ((type = xml.next()) != XmlPullParser.END_DOCUMENT && type != XmlPullParser.START_TAG) {
        }

        final String nodeName = xml.getName();

        if (!"intent-filter".equals(nodeName)) {
            throw new RuntimeException();
        }

        filter.readFromXml(xml);
        MoreAsserts.assertContentsInAnyOrder(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return filter.actionsIterator();
            }
        }, "testAction");

        Intent intent = new Intent();
        intent.setAction("testAction");
        assertTrue(filter.matchAction(intent.getAction()));
    }

    public static Intent GET_EXAMPLE = new Intent("com.test.GET", Uri.parse("http://example/"));

    public void testIntentFilter() throws Exception {
        GET_EXAMPLE.setType("which1/what1");

        IntentFilter filter = new IntentFilter();
        filter.addDataScheme("http");
        filter.addDataAuthority("www.example.com", null);

        int result = filter.matchData(GET_EXAMPLE.getType(), GET_EXAMPLE.getScheme(),
                GET_EXAMPLE.getData());

        checkMatches(filter, new MatchCondition[] {
                new MatchCondition(IntentFilter.MATCH_CATEGORY_HOST, null,
                        null, null, "http://www.example.com/"),
        });
    }

    public static void checkMatches(IntentFilter filter, MatchCondition[] results) {
        for (int i = 0; i < results.length; i++) {
            MatchCondition mc = results[i];
            HashSet<String> categories = null;
            if (mc.categories != null) {
                for (int j = 0; j < mc.categories.length; j++) {
                    if (categories == null) {
                        categories = new HashSet<String>();
                    }
                    categories.add(mc.categories[j]);
                }
            }
            int result = filter.match(mc.action, mc.mimeType, mc.data != null ? mc.data.getScheme()
                    : null, mc.data, categories, "test");
            if ((result & IntentFilter.MATCH_CATEGORY_MASK) != (mc.result & IntentFilter.MATCH_CATEGORY_MASK)) {
                StringBuilder msg = new StringBuilder();
                msg.append("Error matching against IntentFilter:\n");
                filter.dump(new StringBuilderPrinter(msg), "    ");
                msg.append("Match action: ");
                msg.append(mc.action);
                msg.append("\nMatch mimeType: ");
                msg.append(mc.mimeType);
                msg.append("\nMatch data: ");
                msg.append(mc.data);
                msg.append("\nMatch categories: ");
                if (mc.categories != null) {
                    for (int j = 0; j < mc.categories.length; j++) {
                        if (j > 0)
                            msg.append(", ");
                        msg.append(mc.categories[j]);
                    }
                }
                msg.append("\nExpected result: 0x");
                msg.append(Integer.toHexString(mc.result));
                msg.append(", got result: 0x");
                msg.append(Integer.toHexString(result));
                throw new RuntimeException(msg.toString());
            }
        }
    }

    public static class MatchCondition {
        public final int result;

        public final String action;

        public final String mimeType;

        public final Uri data;

        public final String[] categories;

        public MatchCondition(int _result, String _action, String[] _categories, String _mimeType,
                String _data) {
            result = _result;
            action = _action;
            mimeType = _mimeType;
            data = _data != null ? Uri.parse(_data) : null;
            categories = _categories;
        }
    }
    /*
     * public void testGettingIntentFromXML() throws Exception {
     * XmlResourceParser parser =
     * getInstrumentation().getContext().getResources() .getXml(R.xml.xmlactor);
     * Intent intent = parseAlias(parser); assertNotNull(intent);
     * assertEquals(intent + " should contain GET", "GET", intent.getAction());
     * } private Intent parseAlias(XmlPullParser parser) throws
     * XmlPullParserException, IOException { XmlResourceParser n =
     * getInstrumentation().getContext().getAssets()
     * .openXmlResourceParser("AndroidManifest.xml"); AttributeSet attrs =
     * Xml.asAttributeSet(n); Intent intent = null; int type; while ((type =
     * parser.next()) != XmlPullParser.END_DOCUMENT && type !=
     * XmlPullParser.START_TAG) { } String nodeName = parser.getName(); if
     * (!"activity".equals(nodeName)) { throw new
     * RuntimeException("Actor meta-data must start with <actor> tag; found" +
     * nodeName + " at " + parser.getPositionDescription()); } int outerDepth =
     * parser.getDepth(); while ((type = parser.next()) !=
     * XmlPullParser.END_DOCUMENT && (type != XmlPullParser.END_TAG ||
     * parser.getDepth() > outerDepth)) { if (type == XmlPullParser.END_TAG ||
     * type == XmlPullParser.TEXT) { continue; } nodeName = parser.getName(); if
     * ("intent".equals(nodeName)) { Intent gotIntent =
     * Intent.parseIntent(Resources.getSystem(), parser, attrs); if (intent ==
     * null) intent = gotIntent; } else { skipCurrentTag(parser); } } return
     * intent; } public static void skipCurrentTag(XmlPullParser parser) throws
     * XmlPullParserException, IOException { int outerDepth = parser.getDepth();
     * int type; while ((type = parser.next()) != XmlPullParser.END_DOCUMENT &&
     * (type != XmlPullParser.END_TAG || parser.getDepth() > outerDepth)) { } }
     */
}
