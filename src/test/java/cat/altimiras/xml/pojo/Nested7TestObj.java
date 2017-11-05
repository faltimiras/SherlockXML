package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

import java.util.List;

public class Nested7TestObj extends XMLElement {

	private String field;
	private List<Integer> Values;
	private ListPrimitivesObj aaa;
	private ListPrimitivesObj bbb;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<Integer> getValues() {
		return Values;
	}

	public void setValues(List<Integer> values) {
		Values = values;
	}

	public ListPrimitivesObj getAaa() {
		return aaa;
	}

	public void setAaa(ListPrimitivesObj aaa) {
		this.aaa = aaa;
	}

	public ListPrimitivesObj getBbb() {
		return bbb;
	}

	public void setBbb(ListPrimitivesObj bbb) {
		this.bbb = bbb;
	}
}
