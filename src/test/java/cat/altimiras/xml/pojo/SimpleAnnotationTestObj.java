package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "wrapper")
public class SimpleAnnotationTestObj extends XMLElement{

	private String field;

	@XmlElement(name = "int")
	private int value;

	public String getField() {
		return field;
	}

	public int getValue() {
		return value;
	}
}
