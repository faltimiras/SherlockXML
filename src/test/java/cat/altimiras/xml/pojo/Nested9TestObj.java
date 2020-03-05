package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested9TestObj extends XMLElement {

	private String field;
	private List<SimpleTestObj> SimpleTestObj;
	private List<Simple2TestObj> Simple2TestObj;

	public String getField() {
		return field;
	}

	public List<cat.altimiras.xml.pojo.SimpleTestObj> getSimpleTestObj() {
		return SimpleTestObj;
	}

	public List<cat.altimiras.xml.pojo.Simple2TestObj> getSimple2TestObj() {
		return Simple2TestObj;
	}
}
