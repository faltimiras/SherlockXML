# SherlockXML

![Sherlock holmes](sherlockholmes.jpg "Sherlock Holmes")


Parser built to parse, obviously valid XML, but also corrupted/incomplete or partially XMLs

Build simply and fast by default, no annotation, no magic... just convert and XML to and Java object.

2 options:
- XML to predefined Java class: Just needs a correspondence between XML tags and a member in the Java class

```
<example>this is an example</example> -> private String example
```

- XML to flexible class called Parsed: No need for any Java class predefined before. Parse XML and query for data in a kind of super-simplified XPath query. 

#Corrupted/incomplete or partially XMLs

XML is not valid or is a part of it. Java class will be filled with all data available until corruption

#Stop parsing when...

Register a listener to a specific tag and stop parsing at any time. 
For instance, you have data you need, Stop parsing process and don't waste time parsing useless data, make no sense!
Don't waste time parsing huge XML if you only need a little part of it.

#Example

extremely simple, just this...

##XML to predefined Java class

```
XMLFactory.init(ComplexObj.class);

XMLParser<ComplexObj> parser = XMLFactory.getParser(ComplexObj.class);
ComplexObj o = parser.parse(xml);
```

XMLFactory.init() initialize factory with all objects that will support. Do it on application start. It preprocess classes to be faster then.
XMLFactory.getParse() must be called every time a new xml has to be parsed. DO NOT reuse.

###Known limitations
Do not support tag names collisions. ns:tagA and ns1:tagA is the same for this parser and it expects for a class called tagA.


##XML to Parsed

```
XMLParser<Parsed> parser = XMLFactory.getParser();
Parsed p = parser.parse(xml);

String value = p.get("/root/object/value").value();
```

Just get a parser instance without arguments.

After parse the xml you get an instance of Parsed where you can ask for values.
 
###Parsed
 
Class with some sugar to ask for data.

- get(String): To retrieve data from XML tree structure using element names split by '/'. ex: get("/root/object/value") or get("root","object","value")

Once you are in a node you can retrieve data with:

- value(): You get raw data from this element
- asParsed(): You get Parsed element from this XML tree node.
- asList(): You get a list of Parsed elements form this XML tree node.

Based on Woodstox