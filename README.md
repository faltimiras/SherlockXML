# SherlockXML (and JSON)

![Sherlock holmes](sherlockholmes.jpg "Sherlock Holmes")


Parser built to parse, obviously valid **XML** & **JSON**, but also **corrupted/incomplete or partially** ones.

Build simply and fast by default, no annotation, no magic... just convert and XML to and Java object.

2 options:
- XML to predefined Java class: Just needs a correspondence between XML tags and a member in the Java class

```
<example>this is an example</example> -> private String example
```

- XML|JSON to flexible class called [Matryoshka](https://github.com/faltimiras/Matryoshka): No need for any Java class predefined before. Parse XML|JSON and query for data in a kind of super-simplified-limited XPath query. 

# Corrupted/incomplete or partially XMLs|JSONs

XML|JSON is not valid or is a part of it. Java class|Matryoshka will be filled with all data available until corruption

# Stop parsing when...

Register a listener to a specific tag and stop parsing at any time. 
For instance, you have data you need, Stop parsing process and don't waste time parsing useless data, make no sense!
Don't waste time parsing huge XML|JSON if you only need a little part of it.


# Example

extremely simple, just this...

## XML to predefined Java class

```
XMLFactory.init(ComplexObj.class);

Parser<ComplexObj> parser = XMLFactory.getParser(ComplexObj.class);
ComplexObj o = parser.parse(xml);
```

XMLFactory.init() initialize factory with all objects that will support. Do it on application start. It preprocess classes to be faster then.
XMLFactory.getParse() must be called every time a new xml has to be parsed. DO NOT reuse.

### Known limitations
Do not support tag names collisions. ns:tagA and ns1:tagA is the same for this parser and it expects for a class called tagA.


## XML to [Matryoshka](https://github.com/faltimiras/Matryoshka)

```java
Parser<Matryoshka> parser = XMLFactory.getParser();
Matryoshka p = parser.parse(xml);

String value = p.get("/root/object/value").value();
```

Just get a parser instance without arguments.

After parse the xml you get an instance of Matryoshka where you can ask for values.

## JSON to [Matryoshka](https://github.com/faltimiras/Matryoshka)
```java
Parser<Matryoshka> parser = JSONFactory.getParser();
Matryoshka p = parser.parse(json);

String value = p.get("/root/object/value").value();
```

# Kudos
This wasn't possible without the amazing job of:
[Wookstox](https://github.com/FasterXML/woodstox)
