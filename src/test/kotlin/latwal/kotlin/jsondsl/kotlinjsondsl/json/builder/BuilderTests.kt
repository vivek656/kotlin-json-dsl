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
                +json {
                    "type" to "one"
                }
                +"me"
                +json {

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
    fun `use --json-- builder to build json structure`() {
        json {
            "primitiveField" to 7
        } asserting {
            containsField("primitiveField")
        }
    }

    @Test
    fun `use --to-- construct to set field as an object, array or primitive value`() {
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
    fun `simply set field value to data class using --to-- construct`() {

        json {
            "leafObject" to TestDataClass(fieldOne = "fieldOneValue")
        } asserting {
            containsObjectAt("leafObject")
        }
    }

    @Test
    fun `use --array-- to create an array, use --plus-- inside to add a json object`() {
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

    @Test
    fun `used --typedJson-- to create json , bounded or constrained by a kotlin type`() {
        typedJson<TestDataClass2> {
            "stringField" to "value1"
            "integerField" to 7
            "objectField" to typedJsonData {
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
            "kind" to "youtube#searchListResponse"
            "etag" to "q4ibjmYp1KA3RqMF4jFLl6PBwOg"
            "nextPageToken" to "CAUQAA"
            "regionCode" to "NL"
            "pageInfo" to {
                "totalResults" to 1_000_000
                "resultsPerPage" to 5
            }
            "items" to array {
                plus {
                    "kind" to "youtube#searchResult"
                    "etag" to "QCsHBifbaernVCbLv8Cu6rAeaDQ"
                    "id" to {
                        "kind" to "youtube#video"
                        "videoId" to "TvWDY4Mm5GM"
                    }
                    "snippet" to {
                        "publishedAt" to "2023-07-24T14:15:01Z"
                        "channelId" to "UCwozCpFp9g9x0wAzuFh0hwQ"
                        "title" to "3 Football Clubs Kylian Mbappe Should Avoid Signing ✍️❌⚽️ #football #mbappe #shorts"
                        "description" to ""
                        "thumbnails" to {
                            "default" to {
                                "url" to "https://i.ytimg.com/vi/TvWDY4Mm5GM/default.jpg"
                                "width" to 120
                                "height" to 90
                            }
                            "medium" to {
                                "url" to "https://i.ytimg.com/vi/TvWDY4Mm5GM/mqdefault.jpg"
                                "width" to 320
                                "height" to 180
                            }
                            "high" to {
                                "url" to "https://i.ytimg.com/vi/TvWDY4Mm5GM/hqdefault.jpg"
                                "width" to 480
                                "height" to 360
                            }
                        }
                        "channelTitle" to "FC Motivate"
                        "liveBroadcastContent" to "none"
                        "publishTime" to "2023-07-24T14:15:01Z"
                    }
                }
                plus {
                    "kind" to "youtube#searchResult"
                    "etag" to "0NG5QHdtIQM_V-DBJDEf-jK_Y9k"
                    "id" to {
                        "kind" to "youtube#video"
                        "videoId" to "aZM_42CcNZ4"
                    }
                    "snippet" to {
                        "publishedAt" to "2023-07-24T16:09:27Z"
                        "channelId" to "UCM5gMM_HqfKHYIEJ3lstMUA"
                        "title" to "Which Football Club Could Cristiano Ronaldo Afford To Buy? 💰"
                        "description" to "Sign up to Sorare and get a FREE card: https://sorare.pxf.io/NellisShorts Give Soraredata a go for FREE: ..."
                        "thumbnails" to {
                            "default" to {
                                "url" to "https://i.ytimg.com/vi/aZM_42CcNZ4/default.jpg"
                                "width" to 120
                                "height" to 90
                            }
                            "medium" to {
                                "url" to "https://i.ytimg.com/vi/aZM_42CcNZ4/mqdefault.jpg"
                                "width" to 320
                                "height" to 180
                            }
                            "high" to {
                                "url" to "https://i.ytimg.com/vi/aZM_42CcNZ4/hqdefault.jpg"
                                "width" to 480
                                "height" to 360
                            }
                        }
                        "channelTitle" to "John Nellis"
                        "liveBroadcastContent" to "none"
                        "publishTime" to "2023-07-24T16:09:27Z"
                    }
                }
            }
        } asserting {
            isAnObject()
        }
    }

    @Test
    fun simple(){
        println()
    }

}


