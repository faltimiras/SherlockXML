package cat.altimiras.xml.pojo;

import java.util.List;

public class Nested4TestObj {
	private String title;
	private List<SimpleTestObj> simpleElements;

	public String getTitle() {
		return title;
	}

	public List<SimpleTestObj> getSimpleElements() {
		return simpleElements;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSimpleElements(List<SimpleTestObj> simpleElements) {
		this.simpleElements = simpleElements;
	}
}
