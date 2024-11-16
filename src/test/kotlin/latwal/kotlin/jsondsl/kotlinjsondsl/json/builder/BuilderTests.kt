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
            "kind" .. "youtube#searchListResponse"
            "etag" .. "q4ibjmYp1KA3RqMF4jFLl6PBwOg"
            "nextPage..ken" .. "CAUQAA"
            "regionCode" .. "NL"
            "pageInfo" .. {
                "..talResults" .. 1_000_000
                "resultsPerPage" .. 5
            }
            "nativeObjectField" .. TestDataClass(
                fieldOne = "myfield"
            )
            "arrayContruct" .. arrayOf(
                "k",
                json {
                    "json field" .. "mismatch"
                }
            )
            "array2" .. arrayOf(
                1,"2",3
            )
            "items" .. array {
                plus {
                    "kind" .. "youtube#searchResult"
                    "etag" .. "QCsHBifbaernVCbLv8Cu6rAeaDQ"
                    "id" .. {
                        "kind" .. "youtube#video"
                        "videoId" .. "TvWDY4Mm5GM"
                    }
                    "snippet" .. {
                        "publishedAt" .. "2023-07-24T14:15:01Z"
                        "channelId" .. "UCwozCpFp9g9x0wAzuFh0hwQ"
                        "title" .. "3 Football Clubs Kylian Mbappe Should Avoid Signing ✍️❌⚽️ #football #mbappe #shorts"
                        "description" .. ""
                        "thumbnails" .. {
                            "default" .. {
                                "url" .. "https://i.ytimg.com/vi/TvWDY4Mm5GM/default.jpg"
                                "width" .. 120
                                "height" .. 90
                            }
                            "medium" .. {
                                "url" .. "https://i.ytimg.com/vi/TvWDY4Mm5GM/mqdefault.jpg"
                                "width" .. 320
                                "height" .. 180
                            }
                            "high" .. {
                                "url" .. "https://i.ytimg.com/vi/TvWDY4Mm5GM/hqdefault.jpg"
                                "width" .. 480
                                "height" .. 360
                            }
                        }
                        "channelTitle" .. "FC Motivate"
                        "liveBroadcastContent" .. "none"
                        "publishTime" .. "2023-07-24T14:15:01Z"
                    }
                }
                plus {
                    "kind" .. "youtube#searchResult"
                    "etag" .. "0NG5QHdtIQM_V-DBJDEf-jK_Y9k"
                    "id" .. {
                        "kind" .. "youtube#video"
                        "videoId" .. "aZM_42CcNZ4"
                    }
                    "snippet" .. {
                        "publishedAt" .. "2023-07-24T16:09:27Z"
                        "channelId" .. "UCM5gMM_HqfKHYIEJ3lstMUA"
                        "title" .. "Which Football Club Could Cristiano Ronaldo Afford .. Buy? 💰"
                        "description" .. "Sign up .. Sorare and get a FREE card: https://sorare.pxf.io/NellisShorts Give Soraredata a go for FREE: ..."
                        "thumbnails" .. {
                            "default" .. {
                                "url" .. "https://i.ytimg.com/vi/aZM_42CcNZ4/default.jpg"
                                "width" .. 120
                                "height" .. 90
                            }
                            "medium" .. {
                                "url" .. "https://i.ytimg.com/vi/aZM_42CcNZ4/mqdefault.jpg"
                                "width" .. 320
                                "height" .. 180
                            }
                            "high" .. {
                                "url" .. "https://i.ytimg.com/vi/aZM_42CcNZ4/hqdefault.jpg"
                                "width" .. 480
                                "height" .. 360
                            }
                        }
                        "channelTitle" .. "John Nellis"
                        "liveBroadcastContent" .. "none"
                        "publishTime" .. "2023-07-24T16:09:27Z"
                    }
                }
            }
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


