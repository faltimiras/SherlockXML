package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class NestedAnnotationTestObj extends XMLElement {

	private SimpleAnnotationTestObj object;

	@XmlElement(name = "xmlList")
	private List<SimpleAnnotationTestObj> list;

	public SimpleAnnotationTestObj getObject() {
		return object;
	}

	public List<SimpleAnnotationTestObj> getList() {
		return list;
	}
}
