package cat.altimiras.xml;

import java.util.List;

class Tag {

	enum TagType {OPEN, CLOSE, SELF_CLOSED}

	protected byte[] name;
	protected byte[] namespace;
	protected TagType type;
	protected List<Attribute> attributes;
	protected boolean cdata;

	private int startContent = 0;
	private int endContent = 0;
	private int position;


	public Tag(byte[] name, byte[] namespace, int position, TagType type, List<Attribute> attributes, boolean cdata, int startContent, int endContent) {
		this.position = position;
		this.name = name;
		this.namespace = namespace;
		this.type = type;
		this.attributes = attributes;
		this.cdata = cdata;
		this.startContent = startContent;
		this.endContent = endContent;
	}

	public int getStartPosition() {
		if (namespace == null) {
			return position - name.length - 3; // 3 = <(1= /(2)>(3)
		}
		else {
			return position - name.length - 3 - namespace.length - 1; // 3 = <(1= /(2)>(3)
		}
	}

	public int getStartContentOffset() {
		return startContent;
	}

	public int getEndContentOffset() {
		return endContent;
	}

	public int getEndContentPosition() {
		return getStartPosition() - endContent - 1;
	}

	public int getEndPosition() {
		return position;
	}

	public boolean isOpening() {
		return type == TagType.OPEN || type == TagType.SELF_CLOSED;
	}

	public boolean isClosing() {
		return type == TagType.CLOSE || type == TagType.SELF_CLOSED;
	}

	public boolean hasNamespace() {
		return namespace != null;
	}
}