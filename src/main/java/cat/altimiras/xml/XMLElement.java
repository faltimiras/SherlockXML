package cat.altimiras.xml;

public abstract class XMLElement implements Cloneable {

	private transient boolean incomplete = false;

	/**
	 * Object parsed has not been totally parsed due to xml is not totally completed.
	 * @return
	 */
	public boolean isIncomplete() {
		return incomplete;
	}

	public void markAsIncomplete() {
		this.incomplete = true;
	}

	public void setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
