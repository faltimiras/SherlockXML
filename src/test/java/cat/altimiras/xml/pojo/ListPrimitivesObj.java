package cat.altimiras.xml.pojo;

import cat.altimiras.xml.XMLElement;

import java.util.List;

public class ListPrimitivesObj extends XMLElement {

	private List<Integer> values;

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}
}
