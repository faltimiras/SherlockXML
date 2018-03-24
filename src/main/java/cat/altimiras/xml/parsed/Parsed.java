package cat.altimiras.xml.parsed;

import cat.altimiras.xml.XMLElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Parsed extends XMLElement {

	private final static String SEPARATOR = "/";

	protected String name;

	protected Map<String, Object> data;

	protected String value;

	Parsed(){
	}

	Parsed(String name){
		this.name = name;
	}

	Parsed(String name, Map<String, Object> data){
		this.name = name;
		this.data = data;
	}

	public Result get(String path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		return get(path.split(SEPARATOR));
	}

	public Result get(String... parts){

		if (parts == null || parts.length == 0) {
			return null;
		}

		//support to / or not at the beginning of the path expression
		int start = 0;
		if(parts[0].isEmpty()){
			start = 1;
		}
		Object o = getRec(name, data, parts, start);
		return  new Result(o);
	}

	private Object getRec(String name, Map<String, Object> p, String[] path, int pos) {

		if (p == null || path == null || path.length == 0){
			return null;
		}

		if(name.equals(path[pos])){
			if (path.length -1 == pos){
				return p.get(name);
			}
			else {
				if (p.get(name) instanceof String){
					return p.get(name);
				}
				else {
					return getRec(path[pos + 1], (Map) p.get(name), path, ++pos);
				}
			}
		}

		return null;
	}



	public class Result {

		private Object content;

		Result(Object o){
			this.content = o;
		}


		public List<Parsed> asList(){

			if (this.content == null){
				return new ArrayList<>(0);
			}

			if (this.content instanceof List) {
				if (((List) this.content).isEmpty()){
					return new ArrayList<>(0);
				}
				List list = new ArrayList(((List)content).size());
				((List)this.content).stream().forEach( e-> list.add(new Parsed(((Map)e).keySet().iterator().next().toString(), (Map)e)));
				return list;
			}
			else if (this.content instanceof Map) {

				if (((Map) this.content).isEmpty()) {
					return new ArrayList<>(0);
				}
				List list = new ArrayList(1);
				list.add(new Parsed(((Map) this.content).keySet().iterator().next().toString(), (Map) this.content));
				return list;
			}
			return new ArrayList<>(0);
		}

		public Object value(){
			return content;
		}

		public boolean isParsed(){
			return this.content instanceof Map;
		}




	}
}
