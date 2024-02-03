package latwal.kotlin.jsondsl.kotlinjsondsl.json

// Define a DSL marker interface
@DslMarker
annotation class JsonDsl


open class JsonObjectContext {
    private val data = JsonData()

    infix fun String.to(value: Any): JsonData {
        data.set(this, value)
        return data.get(this)!!
    }

    infix fun String.to(value: JsonData): JsonData {
        return data.set(this, value)
    }

    infix fun String.to(init: JsonObjectContext.() -> Unit): JsonData {
        return this to json(init)
    }

    infix fun String.to(inits: Array<JsonObjectContext.() -> Unit>): JsonData {
        return this to ArrayJsonData.fromCollection(inits.map { json(it) })
    }

    open fun data() = data
}

class JsonArrayContext : JsonObjectContext() {
    private val arrayJsonData = ArrayJsonData()

    operator fun JsonData.unaryPlus() {
        arrayJsonData.add(this)
    }

    operator fun Any.unaryPlus() {
        arrayJsonData.add(this)
    }

    infix fun plus(init: JsonObjectContext.() -> Unit) {
        json(init).unaryPlus()
    }

    operator fun plus(any: Any) {
        arrayJsonData.add(any)
    }

    override fun data() = arrayJsonData
}


fun json(init: JsonObjectContext.() -> Unit): JsonData {
    val jsonContext = JsonObjectContext()
    jsonContext.init()
    return jsonContext.data()
}

fun array(init: JsonArrayContext.() -> Unit): ArrayJsonData {
    val jsonContext = JsonArrayContext()
    jsonContext.init()
    return jsonContext.data()
}



fun main() {
    val data = json {
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
    }

    println(data)

}

