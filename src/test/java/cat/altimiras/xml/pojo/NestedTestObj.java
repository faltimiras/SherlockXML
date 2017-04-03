package cat.altimiras.xml.pojo;

public class NestedTestObj {
	private String title;
	private SimpleTestObj simpleTestObj;

	public String getTitle() {
		return title;
	}

	public SimpleTestObj getSimpleTestObj() {
		return simpleTestObj;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSimpleTestObj(SimpleTestObj simpleTestObj) {
		this.simpleTestObj = simpleTestObj;
	}
}
