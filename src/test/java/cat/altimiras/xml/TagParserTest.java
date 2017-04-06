package cat.altimiras.xml;


import org.junit.Test;

import static cat.altimiras.xml.Tag.TagType.CLOSE;
import static cat.altimiras.xml.Tag.TagType.OPEN;
import static cat.altimiras.xml.Tag.TagType.SELF_CLOSED;
import static org.junit.Assert.assertEquals;

public class TagParserTest {

	@Test
	public void openTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void openTagWithNoisetest() throws Exception {
		Tag t = new TagParser().getTag(" \n<t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void closeTagtest() throws Exception {
		Tag t = new TagParser().getTag("</t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void selfCloseTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void openWithAttributesTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\">".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void openWith2AttributesTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\" a2=\"g2\" >".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
		assertEquals("a2", t.attributes.get(1).name);
		assertEquals("g2", t.attributes.get(1).value);
	}

	@Test
	public void selfCloseWithAttributesTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\"/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void selfCloseWithAttributesAndNoiseTagtest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"h=g\"/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("h=g", t.attributes.get(0).value);
	}

	@Test
	public void openWithNsTagtest() throws Exception {
		Tag t = new TagParser().getTag("<n:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("n", t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void selfClosedWithNsTagtest() throws Exception {
		Tag t = new TagParser().getTag("<n:t/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("n", t.namespace);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void selfClosedWithNs2Tagtest() throws Exception {
		//namespace selfclosed
		Tag t = new TagParser().getTag("</nn:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void openWithNs2Tagtest() throws Exception {
		Tag t = new TagParser().getTag("<nn:tt  xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void openWithNs3Tagtest() throws Exception {
		//namespace open
		Tag t = new TagParser().getTag("<nn:tt a=\"g\" xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void openWithNs4Tagtest() throws Exception {
		//namespace open
		Tag t = new TagParser().getTag("<nn:tt  xmlns:nn=\"http:/\" a=\"g\">".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(1).name);
		assertEquals("g", t.attributes.get(1).value);
	}
}
