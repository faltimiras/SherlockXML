package cat.altimiras.xml;


import org.junit.Test;

import static cat.altimiras.xml.Tag.TagType.CLOSE;
import static cat.altimiras.xml.Tag.TagType.OPEN;
import static cat.altimiras.xml.Tag.TagType.SELF_CLOSED;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TagParserTest {

	final private int bufferSize = 100;

	@Test
	public void openTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void openTagWithNoisetest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag(" \n<t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void closeTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("</t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void selfCloseTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t/>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void openWithAttributesTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"g\">".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(OPEN, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("g".getBytes(), t.attributes.get(0).value);
	}

	@Test
	public void openWithAttributesAndSpacesTag() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"g g\">".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(OPEN, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("g g".getBytes(), t.attributes.get(0).value);
	}

	@Test
	public void openWith2AttributesTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"g\" \t\n a2=\"g2\" >".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(OPEN, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("g".getBytes(), t.attributes.get(0).value);
		assertArrayEquals("a2".getBytes(), t.attributes.get(1).name);
		assertArrayEquals("g2".getBytes(), t.attributes.get(1).value);
	}

	@Test
	public void selfCloseWithAttributesTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"g\"/>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("g".getBytes(), t.attributes.get(0).value);
	}

	@Test
	public void selfCloseWithAttributesAndNoiseTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"h=g\"/>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("h=g".getBytes(), t.attributes.get(0).value);
	}

	@Test
	public void selfCloseWithAttributesAndNoise2TagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<t a=\"h=g\"  \n\n b=\"h\" />".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(SELF_CLOSED, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("h=g".getBytes(), t.attributes.get(0).value);
		assertArrayEquals("b".getBytes(), t.attributes.get(1).name);
		assertArrayEquals("h".getBytes(), t.attributes.get(1).value);
	}

	@Test
	public void openWithNsTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<n:t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("n".getBytes(), t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void selfClosedWithNsTagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<n:t/>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("n".getBytes(), t.namespace);
		assertEquals(SELF_CLOSED, t.type);
	}

	@Test
	public void selfClosedWithNs2TagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("</nn:t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(CLOSE, t.type);
	}

	@Test
	public void openWithNs2TagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<nn:tt  xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertArrayEquals("tt".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(OPEN, t.type);
	}

	@Test
	public void openWithNs3TagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<nn:tt a=\"g\" xmlns:nn=\"http:/\" >".getBytes(), 0);
		assertArrayEquals("tt".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(OPEN, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(0).name);
		assertArrayEquals("g".getBytes(), t.attributes.get(0).value);
	}

	@Test
	public void openWithNs4TagTest() throws Exception {
		Tag t = new TagParser(bufferSize).getTag("<nn:tt  xmlns:nn=\"http:/\" a=\"g\">".getBytes(), 0);
		assertArrayEquals("tt".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(OPEN, t.type);
		assertArrayEquals("a".getBytes(), t.attributes.get(1).name);
		assertArrayEquals("g".getBytes(), t.attributes.get(1).value);
	}

	@Test
	public void closeTagWithContentBeforeTest() throws Exception {

		Tag t = new TagParser(bufferSize).getTag(" a</nn:t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(1, t.getEndContentPosition());
		assertEquals(0, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentAfterTest() throws Exception {

		Tag t = new TagParser(bufferSize).getTag("aaaa </nn:t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(0, t.getStartContentOffset());
		assertEquals(3, t.getEndContentPosition());
		assertEquals(1, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddleTest() throws Exception {

		Tag t = new TagParser(bufferSize).getTag(" aaaa   </nn:t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertArrayEquals("nn".getBytes(), t.namespace);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(3, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle2Test() throws Exception {

		Tag t = new TagParser(bufferSize).getTag(" a  a   </t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(3, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle3Test() throws Exception {

		Tag t = new TagParser(bufferSize).getTag(" a  a   \n</t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(4, t.getEndContentPosition());
		assertEquals(4, t.getEndContentOffset());
	}

	@Test
	public void closeTagWithContentMiddle4Test() throws Exception {

		Tag t = new TagParser(bufferSize).getTag(" a  \na   \n</t>".getBytes(), 0);
		assertArrayEquals("t".getBytes(), t.name);
		assertEquals(CLOSE, t.type);
		assertEquals(1, t.getStartContentOffset());
		assertEquals(5, t.getEndContentPosition());
		assertEquals(4, t.getEndContentOffset());
	}
}
