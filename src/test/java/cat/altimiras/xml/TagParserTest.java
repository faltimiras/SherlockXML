package cat.altimiras.xml;


import org.junit.Test;

import static cat.altimiras.xml.Tag.TagType.CLOSE;
import static cat.altimiras.xml.Tag.TagType.OPEN;
import static cat.altimiras.xml.Tag.TagType.SELF_CLOSED;
import static org.junit.Assert.assertEquals;

public class TagParserTest {

	@Test
	public void openTagTest() throws Exception {
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
	public void closeTagTest() throws Exception {
		Tag t = new TagParser().getTag("</t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void selfCloseTagTest() throws Exception {
		Tag t = new TagParser().getTag("<t/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void openWithAttributesTagTest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\">".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void openWithAttributesAndSpacesTag() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g g\">".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g g", t.attributes.get(0).value);
	}

	@Test
	public void openWith2AttributesTagTest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\" a2=\"g2\" >".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
		assertEquals("a2", t.attributes.get(1).name);
		assertEquals("g2", t.attributes.get(1).value);
	}

	@Test
	public void selfCloseWithAttributesTagTest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"g\"/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void selfCloseWithAttributesAndNoiseTagTest() throws Exception {
		Tag t = new TagParser().getTag("<t a=\"h=g\"/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("h=g", t.attributes.get(0).value);
	}

	@Test
	public void openWithNsTagTest() throws Exception {
		Tag t = new TagParser().getTag("<n:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("n", t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void selfClosedWithNsTagTest() throws Exception {
		Tag t = new TagParser().getTag("<n:t/>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("n", t.namespace);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void selfClosedWithNs2TagTest() throws Exception {
		Tag t = new TagParser().getTag("</nn:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void openWithNs2TagTest() throws Exception {
		Tag t = new TagParser().getTag("<nn:tt  xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void openWithNs3TagTest() throws Exception {
		Tag t = new TagParser().getTag("<nn:tt a=\"g\" xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(0).name);
		assertEquals("g", t.attributes.get(0).value);
	}

	@Test
	public void openWithNs4TagTest() throws Exception {
		Tag t = new TagParser().getTag("<nn:tt  xmlns:nn=\"http:/\" a=\"g\">".getBytes(), 0);
		assertEquals("tt", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(OPEN, t.type);
		assertEquals("a", t.attributes.get(1).name);
		assertEquals("g", t.attributes.get(1).value);
	}

	@Test
	public void closeTagWithContentBeforeTest() throws Exception {

		Tag t = new TagParser().getTag(" a</nn:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(1, t.getEndContentPosition());
		assertEquals(0, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentAfterTest() throws Exception {

		Tag t = new TagParser().getTag("aaaa </nn:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(0, t.getStartContentOffset());
		assertEquals(3, t.getEndContentPosition());
		assertEquals(1, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddleTest() throws Exception {

		Tag t = new TagParser().getTag(" aaaa   </nn:t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals("nn", t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(3, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle2Test() throws Exception {

		Tag t = new TagParser().getTag(" a  a   </t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(3, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle3Test() throws Exception {

		Tag t = new TagParser().getTag(" a  a   \n</t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(4, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle4Test() throws Exception {

		Tag t = new TagParser().getTag(" a  \na   \n</t>".getBytes(), 0);
		assertEquals("t", t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(5, t.getEndContentPosition());
		assertEquals(4, t.getEndContentOffset());
	}
}
