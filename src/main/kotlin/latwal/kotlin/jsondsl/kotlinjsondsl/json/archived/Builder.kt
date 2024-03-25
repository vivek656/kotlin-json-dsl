package latwal.kotlin.jsondsl.kotlinjsondsl.json.archived


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
        return this to jsonData(init)
    }

    infix fun String.to(inits: Array<JsonObjectContext.() -> Unit>): JsonData {
        return this to ArrayJsonData.fromCollection(inits.map { jsonData(it) })
    }

    open fun data() = data
}

@Deprecated("Just for representation" )
class JsonArrayContext : JsonObjectContext() {
    private val arrayJsonData = ArrayJsonData()

    operator fun plus(jsonData: JsonData){
        arrayJsonData.add(jsonData)
    }

    infix fun plus(init: JsonObjectContext.() -> Unit) {
        plus(jsonData(init))
    }

    operator fun plus(any: Any) {
        arrayJsonData.add(any)
    }

    override fun data() = arrayJsonData
}


fun jsonData(init: JsonObjectContext.() -> Unit): JsonData {
    val jsonContext = JsonObjectContext()
    jsonContext.init()
    return jsonContext.data()
}
