package cat.altimiras.xml.parsed;

import cat.altimiras.xml.TagListener;
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

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/simpleTest.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		TagListener stringListener = mock(TagListener.class);
		when(stringListener.notify("element1", "111")).thenReturn(false);

		//register listener
		parser.register("element2", stringListener);

		parser.parse(xml);
		verify(stringListener, times(1)).notify("element2", "222");
	}

	@Test
	public void listenerObjectTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("simpleTestObj1"), any())).thenReturn(false);

		//register listener
		parser.register("simpleTestObj1", objListener);

		parser.parse(xml);
		verify(objListener, times(1)).notify(eq("simpleTestObj1"), any());
	}

	@Test
	public void listenerNotPresentTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("tagNotFound"), any())).thenReturn(false);

		//register listener
		parser.register("tagNotFound", objListener);

		parser.parse(xml);
		verify(objListener, never()).notify(eq("tagNotFound"), any());
	}

	@Test
	public void listenerStopTest() throws Exception {

		String xml = IOUtils.toString(this.getClass().getResourceAsStream("/nested2Test.xml"), "UTF-8");
		WoodStoxParsedParserImpl parser = new WoodStoxParsedParserImpl(xmlInputFactory); 

		TagListener objListener = mock(TagListener.class);
		when(objListener.notify(eq("simpleTestObj1"), any())).thenReturn(true);

		//register listener
		parser.register("simpleTestObj1", objListener);

		Parsed o = parser.parse(xml);

		verify(objListener, times(1)).notify(eq("simpleTestObj1"), any());
		assertNull(o.get("Nested2TestObj/simpleTestObj1/title").value());
		assertEquals("111", o.get("Nested2TestObj/simpleTestObj1/element1").value());
		assertNull(o.get("Nested2TestObj/simpleTestObj2").value());
	}
}
