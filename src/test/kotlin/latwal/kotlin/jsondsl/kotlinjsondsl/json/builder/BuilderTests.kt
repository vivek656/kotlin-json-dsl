package latwal.kotlin.jsondsl.kotlinjsondsl.json.builder

import latwal.kotlin.jsondsl.kotlinjsondsl.json.common.AbstractJsonDslTests
import org.junit.jupiter.api.Test


class BuilderTests : AbstractJsonDslTests() {

    @Test
    fun `testing building a simple json`() {
        json {
            "participants" .. arrayOf(
                {
                    "name" .. {
                        "firstName" .. "Peter"
                        "lastname" .. "Hethering..n-Smythe"
                    }
                    "dateOfBirth" .. "1975-12-25"
                },
                {
                    "name" .. {
                        "firstName" .. "Mary"
                        "lastname" .. "john"
                    }
                }
            )
            "nested" .. {

            }
            "propertyPack" .. {
                "materialFacts" .. {
                    "uprn" .. 123456789
                    "address" .. {
                        "line1" .. "47 park road"
                        "postcode" .. "AL3 5AF"
                    }
                    "delayFac..rs" .. {
                        "hasDelayFac..rs" .. {
                            "yesNo" .. "yes"
                        }
                    }
                }
            }
            "titles" .. array {
                plus {
                    "type" .. "leasehold"
                    "rent" .. 100
                }
                plus {
                    "type" .. "one"
                }
                +json {
                    "type" .. "one"
                }
                +"me"
                +json {

                }
            }
            "randomInts" .. array {
                this + 1
                this + 2
                this + 3
            }
        } asserting {
            isAnObject()
            containsArrayAt("participants")
            containsObjectAt("propertyPack")
            containsArrayAt("randomInts")
        }
    }

    @Test
    fun `use --json-- builder to build json structure`() {
        json {
            "primitiveField" .. 7
        } asserting {
            containsField("primitiveField")
        }
    }

    @Test
    fun `use --dot dot-- construct to set field as an object, array or primitive value`() {
        json {
            "intField" .. 7
            "stringField" .. "seven"
            "arrayField" .. arrayOf(
                {
                    "simpleItem" .. {
                        "itemField" .. 5
                    }
                }
            )
        } asserting {
            isAnObject()
            containsField("intField")
            containsArrayAt("arrayField")
        }
    }

    @Test
    fun `simply set field value to data class using --dot dot-- construct`() {

        json {
            "leafObject" .. TestDataClass(fieldOne = "fieldOneValue")
        } asserting {
            containsObjectAt("leafObject")
        }
    }

    @Test
    fun `use --array-- to create an array, use --plus-- inside to add a json object`() {
        array {
            plus {
                "fieldOne" .. "one"
            }
            plus {
                "fieldOne" .. "two"
            }
        } asserting {
            isAnArray()
        }
    }

    @Test
    fun `used --typedJson-- to create json , bounded or constrained by a kotlin type`() {
        typedJson<TestDataClass2> {
            "stringField" .. "value1"
            "integerField" .. 7
            "objectField" .. typedJsonData {
                TestDataClass(fieldOne = "value1")
            }
        } asserting {
            isAnObject()
            containsField("objectField")
        }
    }

    @Test
    fun `real json to json dsl`() {
        json {
            "arrayContruct" .. arrayOf(
                json {
                    "fieldOne" .. 1
                    "field2" .. {
                        "field3" .. arrayOf(
                            1, 2 , "4"
                        )
                    }
                } ,
                "4"
            )
        } asserting {
            isAnObject()
        }
    }

    @Test
    fun simple(){
        val myJson = json {
            "stringField" .. "stringValue"
            "intField" .. 1
            "booleanField" .. true
            "jsonField" .. {
                "myField" .. "field"
            }
            "nativeObjectField" .. TestDataClass(
                fieldOne = "myfield"
            )
            "arrayConstruct" .. arrayOf(
                {
                    "myField" .. "1"
                    "mycash" .. 3
                },
                {
                    "fieldOne" .. 7
                }
            )
            "arrayMe" .. arrayOf(1,"2",3)
        } asserting {
            val jsonData = jsonData()
            val node = jsonData.getNode()
            val sf = jsonData.get("stringField")
            val sfnode = sf?.getNode();
            val arrayJson = jsonData.get("arrayConstruct")!!
            val arrayAsNode = arrayJson.getNode()
            val assArray = arrayJson.getArrayNode()
            val string = jsonData.toString()
        }
    }

}


