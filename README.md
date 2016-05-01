# XMLParser
XML Parser build to parse, obviously valid XML, but also corrupted/incomplete or partially XMLs

Build simply and fast by default, no annotation, no magic... just convert and XML to and Java object.

Just needs that XML tags have a correspondence with a member in a Java class

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
```
XMLParser<ComplexObj> parser = XMLFactory.getParser(ComplexObj.class);
ComplexObj o = parser.parse(xml);
```

