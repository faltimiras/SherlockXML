package cat.altimiras.xml.pojo;


import cat.altimiras.xml.XMLElement;

public class TypeTestObj extends XMLElement {

	private String text;
	private Integer integerNum;
	private Long longNum;
	private Double doubleNum;
	private Float floatNum;
	private Boolean booleanValue;
	private int intPrimitiveNum;
	private long longPrimitiveNum;
	private double doublePrimitiveNum;
	private float floatPrimitiveNum;
	private boolean booleanPrimitiveValue;

	public String getText() {
		return text;
	}

	public Integer getIntegerNum() {
		return integerNum;
	}

	public Long getLongNum() {
		return longNum;
	}

	public Double getDoubleNum() {
		return doubleNum;
	}

	public Float getFloatNum() {
		return floatNum;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public int getIntPrimitiveNum() {
		return intPrimitiveNum;
	}

	public long getLongPrimitiveNum() {
		return longPrimitiveNum;
	}

	public double getDoublePrimitiveNum() {
		return doublePrimitiveNum;
	}

	public float getFloatPrimitiveNum() {
		return floatPrimitiveNum;
	}

	public boolean isBooleanPrimitiveValue() {
		return booleanPrimitiveValue;
	}
}
