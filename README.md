# kotlin-json-dsl
koltin dsl for simple json operations

This library provided DSL to create Json objects.

how you create Json in Javascript
```
  {
    "field" : "value"
  }
```

in kotlin using Jackson 
```
  val node = ObjectMapper.createObjectNode();
  node.set("field" , TextNode("value");
```

with this library
```
json {
       "field" to "value"
}
```

see the repo's wiki and tests for more example
