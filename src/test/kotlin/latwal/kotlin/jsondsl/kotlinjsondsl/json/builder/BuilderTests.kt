package latwal.kotlin.jsondsl.kotlinjsondsl.json.builder

import latwal.kotlin.jsondsl.kotlinjsondsl.json.common.AbstractJsonDslTests
import org.junit.jupiter.api.Test


class BuilderTests : AbstractJsonDslTests() {

    @Test
    fun `testing building a simple json`() {
        json {
            "participants" to arrayOf(
                    {
                        "name" to {
                            "firstName" to "Peter"
                            "lastname" to "Hetherington-Smythe"
                        }
                        "dateOfBirth" to "1975-12-25"
                    },
                    {
                        "name" to {
                            "firstName" to "Mary"
                            "lastname" to "john"
                        }
                    }
            )
            "propertyPack" to {
                "materialFacts" to {
                    "uprn" to 123456789
                    "address" to {
                        "line1" to "47 park road"
                        "postcode" to "AL3 5AF"
                    }
                    "delayFactors" to {
                        "hasDelayFactors" to {
                            "yesNo" to "yes"
                        }
                    }
                }
            }
            "titles" to array {
                plus {
                    "type" to "leasehold"
                    "rent" to 100
                }
                plus {
                    "type" to "one"
                }
            }
            "randomInts" to array {
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
    fun `use --json-- builder to build json structure`(){
        json {
            "primitiveField" to 7
        } asserting {
            containsField("primitiveField")
        }
    }

    @Test
    fun `use --to-- construct to set field as an object, array or primitive value`(){
        json {
            "intField" to 7
            "stringField" to "seven"
            "arrayField" to arrayOf(
                    {
                        "simpleItem" to {
                            "itemField" to 5
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
    fun `simply set field value to data class using --to-- construct`(){

        json {
            "leafObject" to TestDataClass(fieldOne = "fieldOneValue")
        } asserting {
            containsObjectAt("leafObject")
        }
    }

    @Test
    fun `use --array-- to create an array, use --plus-- inside to add a json object`(){
        array {
            plus {
                "fieldOne" to "one"
            }
            plus {
                "fieldOne" to "two"
            }
        } asserting {
            isAnArray()
        }
    }

}