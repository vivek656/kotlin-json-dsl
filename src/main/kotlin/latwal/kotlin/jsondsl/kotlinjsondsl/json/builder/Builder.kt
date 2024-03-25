package latwal.kotlin.jsondsl.kotlinjsondsl.json.builder

import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonDataJsonArrayNodeWrapper
import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonDataJsonNodeWrapper
import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.TypedJsonData


open class JsonObjectNodeContext(
        private val data: JsonDataJsonNodeWrapper = JsonDataJsonNodeWrapper.createInitialObjectNode()
) {
    infix fun String.to(value: Any): JsonDataJsonNodeWrapper {
        data.set(this, value)
        return data.get(this)!!
    }

    infix fun String.to(init: JsonObjectNodeContext.() -> Unit): JsonDataJsonNodeWrapper {
        return this to json(init)
    }

    infix fun String.to(inits: Array<JsonObjectNodeContext.() -> Unit>): JsonDataJsonNodeWrapper {
        return this to JsonDataJsonArrayNodeWrapper.fromCollection(inits.map { json(it) })
    }

    open fun data() = data
}

class JsonArrayNodeContext : JsonObjectNodeContext() {
    private val arrayJsonData = JsonDataJsonNodeWrapper.createInitialArrayNode()

    operator fun plus(jsonData: JsonDataJsonNodeWrapper){
        arrayJsonData.add(jsonData)
    }

    infix fun plus(init: JsonObjectNodeContext.() -> Unit) {
        plus(json(init))
    }

    operator fun plus(any: Any) {
        arrayJsonData.add(any)
    }

    override fun data() = arrayJsonData
}


fun json(init: JsonObjectNodeContext.() -> Unit): JsonDataJsonNodeWrapper {
    val jsonContext = JsonObjectNodeContext()
    jsonContext.init()
    return jsonContext.data()
}

fun array(init: JsonArrayNodeContext.() -> Unit): JsonDataJsonArrayNodeWrapper {
    val jsonContext = JsonArrayNodeContext()
    jsonContext.init()
    return jsonContext.data()
}

/**
 * Function give typed Json, from the provided jsonData ,
 * the jsonData, should contain all the fields required to construct the typed jsonData.else will throw error
 */
inline fun <reified T : Any> typedJson(init: JsonObjectNodeContext.() -> Unit): TypedJsonData<T> {
    val jsonContext = JsonObjectNodeContext()
    jsonContext.init()
    return TypedJsonData.from(T::class,jsonContext.data())
}

inline fun <reified T : Any> typedJsonData(init: () -> T): TypedJsonData<out T> {
    val data = init()
    return TypedJsonData.from(data)
}

