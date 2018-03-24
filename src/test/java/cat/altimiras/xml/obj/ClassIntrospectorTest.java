package cat.altimiras.xml.obj;


import cat.altimiras.xml.pojo.ListTestObj;
import cat.altimiras.xml.pojo.NestedTestObj;
import cat.altimiras.xml.pojo.NotXMLElement;
import cat.altimiras.xml.pojo.SimpleAnnotationTestObj;
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

	@Test
	public void testAnnotationClass() throws Exception {

		ClassIntrospector<SimpleAnnotationTestObj> classIntrospector = new ClassIntrospector<>(SimpleAnnotationTestObj.class);

		assertEquals(SimpleAnnotationTestObj.class.getName(), classIntrospector.getInstance("wrapper").getClass().getName());

		assertEquals("value", classIntrospector.getField(SimpleAnnotationTestObj.class, "int").getName());
		assertEquals(Integer.TYPE.getTypeName(), classIntrospector.getField(SimpleAnnotationTestObj.class, "int").getType().getName());
		assertEquals("field", classIntrospector.getField(SimpleAnnotationTestObj.class, "field").getName());
		assertEquals(String.class.getName(), classIntrospector.getField(SimpleAnnotationTestObj.class, "field").getType().getName());
	}

	@Test
	public void testClassHashCode() throws Exception {

		ClassIntrospector<ListTestObj> classIntrospector = new ClassIntrospector<>(ListTestObj.class);
		int hashListTestObj = classIntrospector.getClassHashCode(ListTestObj.class);
		assertEquals("ListTestObj".hashCode(), hashListTestObj);


		ClassIntrospector<SimpleAnnotationTestObj> classIntrospector2 = new ClassIntrospector<>(SimpleAnnotationTestObj.class);
		int hashSimpleAnnotationTestObj = classIntrospector2.getClassHashCode(SimpleAnnotationTestObj.class);
		assertEquals("wrapper".hashCode(), hashSimpleAnnotationTestObj);
	}
}
