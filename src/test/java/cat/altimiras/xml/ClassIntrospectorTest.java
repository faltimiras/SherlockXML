package cat.altimiras.xml;


import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.NestedTestObj;
import cat.altimiras.xml.pojo.NotXMLElement;
import cat.altimiras.xml.pojo.SimpleTestObj;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClassIntrospectorTest {

	@Test
	public void testIntrospectionSimple() throws Exception {

		ClassIntrospector<SimpleTestObj> classIntrospector = new ClassIntrospector<>(SimpleTestObj.class);

		assertEquals("element1", classIntrospector.getField(SimpleTestObj.class, "element1").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element1").getType().getName());
		assertEquals("element2", classIntrospector.getField(SimpleTestObj.class, "element2").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element2").getType().getName());
	}

	@Test
	public void testIntrospectionNested() throws Exception {

		ClassIntrospector<NestedTestObj> classIntrospector = new ClassIntrospector<>(NestedTestObj.class);

		assertEquals("title", classIntrospector.getField(NestedTestObj.class, "title").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(NestedTestObj.class, "title").getType().getName());
		assertEquals("simpleTestObj", classIntrospector.getField(NestedTestObj.class, "simpleTestObj").getName());
		assertEquals(SimpleTestObj.class.getName(), classIntrospector.getField(NestedTestObj.class, "simpleTestObj").getType().getName());

		assertEquals("element1", classIntrospector.getField(SimpleTestObj.class, "element1").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element1").getType().getName());
		assertEquals("element2", classIntrospector.getField(SimpleTestObj.class, "element2").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element2").getType().getName());
	}

	@Test
	public void testList() throws Exception {

		ClassIntrospector<ListTestObj> classIntrospector = new ClassIntrospector<>(ListTestObj.class);

		assertEquals("list", classIntrospector.getField(ListTestObj.class, "list").getName());
		assertEquals(List.class.getName(), classIntrospector.getField(ListTestObj.class, "list").getType().getName());

		assertEquals("element1", classIntrospector.getField(SimpleTestObj.class, "element1").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element1").getType().getName());
		assertEquals("element2", classIntrospector.getField(SimpleTestObj.class, "element2").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleTestObj.class, "element2").getType().getName());
	}

	@Test(expected = Exception.class)
	public void testNotXMLElement() throws Exception {
		new ClassIntrospector<>(NotXMLElement.class);
	}
}
