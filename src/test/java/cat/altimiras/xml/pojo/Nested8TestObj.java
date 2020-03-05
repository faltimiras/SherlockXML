package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested8TestObj extends XMLElement {

	private String field;
	private List<Nested6TestObj> list;

	public String getField() {
		return field;
	}

	public List<Nested6TestObj> getList() {
		return list;
	}
}
