package cat.altimiras.xml;

public abstract class XMLElement implements Cloneable {

	private transient boolean incomplete = false;

	/**
	 * Object matryoshka has not been totally matryoshka due to xml is not totally completed.
	 *
	 * @return
	 */
	public boolean isIncomplete() {
		return incomplete;
	}

	public void setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
	}

	public void markAsIncomplete() {
		this.incomplete = true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
