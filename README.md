# XMLParser
Parser built to parse, obviously valid XML, but also corrupted/incomplete or partially XMLs

Build simply and fast by default, no annotation, no magic... just convert and XML to and Java object.

Just needs a correspondence between XML tags and a member in the Java class

```
<example>this is an example</example> -> private String example
```

#Corrupted/incomplete or partially XMLs

XML is not valid or is a part of it. Java class will be filled with all data available until corruption

#Stop parsing when...

Register a listener to a specific tag and stop parsing at any time. 
For instance, you have data you need, Stop parsing process and don't waste time parsing useless data, make no sense!
Don't waste time parsing huge XML if you only need a little part of it.

#Example

extremely simple, just this...
```
XMLFactory.init(ComplexObj.class);

XMLParser<ComplexObj> parser = XMLFactory.getParser(ComplexObj.class);
ComplexObj o = parser.parse(xml);
```

XMLFactory.init() initialize factory with all objects that will support. Do it on aplication start. It preprocess classes to be faster then.
XMLFactory.getParse() must be called every time a new xml has to be parsed. DO NOT reuse.

#Known limitations
Do not support tag names collisions. ns:tagA and ns1:tagA is the same for this parser and it expects for a class called tagA.
Do not support comments. XML can not contain `<!-- -->` 