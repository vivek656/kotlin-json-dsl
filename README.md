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
  val node = ObjectMapper.createObjectNode()
  node.set("field" , TextNode("value"))
```

with this library
```
json {
       "field" .. "value"
}
```

see the repo's [wiki](https://github.com/vivek656/kotlin-json-dsl/wiki) and [tests](https://github.com/vivek656/kotlin-json-dsl/blob/main/src/test/kotlin/latwal/kotlin/jsondsl/kotlinjsondsl/json/builder/BuilderTests.kt) for more examples.

## Installation
Library is generally available to install via maven central.</br>
if using maven as build tool use.
```xml
<dependency>
    <groupId>io.github.vivek656</groupId>
    <artifactId>kotlin-json-dsl</artifactId>
    <version>0.0.1</version>
</dependency>
```
if using other build tools like gradle, sbt or need jars to work with see the package page.
[HERE](https://central.sonatype.com/artifact/io.github.vivek656/kotlin-json-dsl)


#### We want contributors that want to extend the functionality of this library.

If you think you can add more feature and fix some bugs lets connect on project [Discussions](https://github.com/vivek656/kotlin-json-dsl/discussions).
Or you can directly email the contributors.
