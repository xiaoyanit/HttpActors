
package com.novoda.lib.httpservice.actor.factory;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorNotFoundException;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.actor.SimpleActor1;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.test.MoreAsserts;

public class XmlActorFactoryTest extends InstrumentationTestCase {

    public static Intent GET_EXAMPLE = new Intent("GET", Uri.parse("http://www.example.com"));
    public static Intent GET_EXAMPLE_2 = new Intent("GET", Uri.parse("http://www.another.com/path/1"));

    public static Intent GET_FAILURE_HOST = new Intent("GET",
            Uri.parse("http://www.example_typo.com"));

    public void testParsingBasicXML() throws Exception {
        XmlResourceParser xml = getInstrumentation().getContext().getResources()
                .getXml(R.xml.xmlactor);
        XmlActorFactory factory = new XmlActorFactory(xml, getInstrumentation().getTargetContext());
        Actor actor = factory.getActor(GET_EXAMPLE, null);
        MoreAsserts.assertAssignableFrom(LoggingActor.class, actor);
        
        Actor actor_1 = factory.getActor(GET_EXAMPLE_2, null);
        MoreAsserts.assertAssignableFrom(SimpleActor1.class, actor_1);
    }
    
    public void testGettingExceptionIfNoActorPresents() throws Exception {
        try {
            XmlResourceParser xml = getInstrumentation().getContext().getResources()
                    .getXml(R.xml.xmlactor);
            XmlActorFactory factory = new XmlActorFactory(xml, getInstrumentation()
                    .getTargetContext());
            factory.getActor(GET_FAILURE_HOST, null);
            fail();
        } catch (ActorNotFoundException e) {
            assertTrue(true);
        }
    }
}
