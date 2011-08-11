
package com.novoda.lib.httpservice.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Xml;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.utils.BundleUtils;

public class HttpServiceComponent implements Parcelable {

    private static final String HTTP_SERVICE_TAG = "HttpService";

    private static final String ACTOR_TAG = "Actor";

    private static final String PROCESSOR_TAG = "Processor";

    public List<ActorComponent> actors;

    public List<ProcessorComponent> processors;

    HttpServiceComponent() {
        actors = new ArrayList<ActorComponent>();
        processors = new ArrayList<ProcessorComponent>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static HttpServiceComponent fromXml(XmlPullParser parser, Context context)
            throws XmlPullParserException, IOException {

        HttpServiceComponent component = new HttpServiceComponent();
        XmlUtils.beginDocument(parser, HTTP_SERVICE_TAG);
        AttributeSet attributes = Xml.asAttributeSet(parser);
        TypedArray ta = context.obtainStyledAttributes(attributes, R.styleable.HttpService);
        ta.recycle();
        XmlUtils.nextElement(parser);
        int eventType = parser.getEventType();
        String name;
        do {
            name = parser.getName();

            if (ACTOR_TAG.equals(name)) {

                ta = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.Actor);
                ActorComponent actor = new ActorComponent();
                actor.name = ta.getString(R.styleable.Actor_name);
                component.actors.add(actor);
                ta.recycle();

            } else if (PROCESSOR_TAG.equals(name)) {

                ta = context.obtainStyledAttributes(Xml.asAttributeSet(parser),
                        R.styleable.Processor);

                ProcessorComponent processor = new ProcessorComponent();
                processor.name = ta.getString(R.styleable.Processor_name);

                processor.bundle = BundleUtils.fromXml(parser);
                component.processors.add(processor);
                ta.recycle();
            }
            XmlUtils.nextElement(parser);
            eventType = parser.getEventType();
        } while (eventType != XmlPullParser.END_DOCUMENT);
        return component;
    }

}
