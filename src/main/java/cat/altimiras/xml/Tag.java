package cat.altimiras.xml;

import java.util.List;

class Tag {

	enum TagType {OPEN, CLOSE, SELF_CLOSED}

	protected String name;
	protected int position;
	protected TagType type;
	protected List<Attribute> attributes;
	protected String namespace;
	protected boolean cdata;

	public Tag(String name, String namespace, int position, TagType type, List<Attribute> attributes, boolean cdata) {
		this.position = position;
		this.name = name;
		this.namespace = namespace;
		this.type = type;
		this.attributes = attributes;
		this.cdata = cdata;
	}

	public int getStartPosition() {
		if (namespace == null) {
			return position - name.length() - 3; // 3 = <(1= /(2)>(3)
		}
		else {
			return position - name.length() - 3 - namespace.length() - 1; // 3 = <(1= /(2)>(3)
		}
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