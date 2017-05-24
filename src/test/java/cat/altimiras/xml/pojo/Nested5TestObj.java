package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested5TestObj extends XMLElement {
	private String title;
	private List<SimpleTestObj> list;

	public String getTitle() {
		return title;
	}

	public List<SimpleTestObj> getList() {
		return list;
	}

}
