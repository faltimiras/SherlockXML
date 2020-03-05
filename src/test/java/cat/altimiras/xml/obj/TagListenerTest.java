package cat.altimiras.xml.obj;


import cat.altimiras.Parser;
import cat.altimiras.TagListener;
import cat.altimiras.xml.pojo.Nested2TestObj;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TagListenerTest {

	private XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();


	@Test
	public void listenerStringTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(SimpleTestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/simpleTest.xml"), "UTF-8");
		Parser<SimpleTestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, SimpleTestObj.class, ci);

		TagListener stringListener = mock(TagListener.class);
		when(stringListener.notify("element1", "111")).thenReturn(false);

		//register listener
		parser.register("element2", stringListener);

		parser.parse(xml);
		verify(stringListener, times(1)).notify("element2", "222");
	}

	@Test
	public void listenerObjectTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nested2Test.xml"), "UTF-8");
		Parser<Nested2TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested2TestObj.class, ci);

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("simpleTestObj1"), any())).thenReturn(false);

		//register listener
		parser.register("simpleTestObj1", objListener);

		parser.parse(xml);
		verify(objListener, times(1)).notify(eq("simpleTestObj1"), any());
	}

	@Test
	public void listenerNotPresentTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nested2Test.xml"), "UTF-8");
		Parser<Nested2TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested2TestObj.class, ci);

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("tagNotFound"), any())).thenReturn(false);

		//register listener
		parser.register("tagNotFound", objListener);

		parser.parse(xml);
		verify(objListener, never()).notify(eq("tagNotFound"), any());
	}

	@Test
	public void listenerStopTest() throws Exception {

		ClassIntrospector ci = new ClassIntrospector(Nested2TestObj.class);

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/xml/nested2Test.xml"), "UTF-8");
		Parser<Nested2TestObj> parser = new WoodStoxObjParserImpl<>(xmlInputFactory, Nested2TestObj.class, ci);

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("simpleTestObj1"), any())).thenReturn(true);

		//register listener
		parser.register("simpleTestObj1", objListener);

		Nested2TestObj o = parser.parse(xml);

		verify(objListener, times(1)).notify(eq("simpleTestObj1"), any());
		assertNull("title should not be matryoshka", o.getTitle());
		assertEquals("111", o.getSimpleTestObj1().getElement1().trim());
		assertNull("title should not be matryoshka", o.getSimpleTestObj2());
	}
}
