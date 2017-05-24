package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

public class NestedLoopTestObj extends XMLElement {

	private Integer num;
	private NestedLoopTestObj nestedLoopTestObj;

	public int getNum() {
		return num;
	}

	public NestedLoopTestObj getNestedLoopTestObj() {
		return nestedLoopTestObj;
	}
}
