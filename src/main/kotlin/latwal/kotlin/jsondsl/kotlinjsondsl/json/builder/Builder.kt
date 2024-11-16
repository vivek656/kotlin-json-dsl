package latwal.kotlin.jsondsl.kotlinjsondsl.json.builder

import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonDataJsonArrayNodeWrapper
import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonDataJsonNodeWrapper
import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.TypedJsonData

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@DslMarker
annotation class JsonBuilderDsl

@JsonBuilderDsl
open class JsonObjectNodeContext(
        private val data: JsonDataJsonNodeWrapper = JsonDataJsonNodeWrapper.createInitialObjectNode()
) {
//    -------------------
    infix operator fun String.rangeTo(value: Any): JsonDataJsonNodeWrapper {
        data.set(this, value)
        return data.get(this)!!
    }

    infix fun String.rangeTo(init: @JsonBuilderDsl JsonObjectNodeContext.() -> Unit): JsonDataJsonNodeWrapper {
        return this .. json(init)
    }

    infix fun String.rangeTo(inits: Array< @JsonBuilderDsl JsonObjectNodeContext.() -> Unit>): JsonDataJsonNodeWrapper {
        return this .. JsonDataJsonArrayNodeWrapper.fromCollection(inits.map { json(it) })
    }

    infix fun <T> String.rangeTo(units: Array<T>): JsonDataJsonNodeWrapper where T : Any {
        return this .. JsonDataJsonArrayNodeWrapper.fromCollection(units.toList())
    }

    open fun data() = data
}

@JsonBuilderDsl
class JsonArrayNodeContext : JsonObjectNodeContext() {
    private val arrayJsonData = JsonDataJsonNodeWrapper.createInitialArrayNode()

    operator fun plus(jsonData: JsonDataJsonNodeWrapper){
        arrayJsonData.add(jsonData)
    }

    infix operator fun plus( @JsonBuilderDsl init: JsonObjectNodeContext.() -> Unit) {
        plus(json(init))
    }

    infix operator fun plus(any: Any) {
        arrayJsonData.add(any)
    }

    operator fun Any.unaryPlus() {
        plus(this)
    }

    override fun data() = arrayJsonData
}

@JsonBuilderDsl
fun json( init: @JsonBuilderDsl  JsonObjectNodeContext.() -> Unit): JsonDataJsonNodeWrapper {
    val jsonContext = JsonObjectNodeContext()
    jsonContext.init()
    return jsonContext.data()
}

@JsonBuilderDsl
fun array( init: @JsonBuilderDsl  JsonArrayNodeContext.() -> Unit): JsonDataJsonArrayNodeWrapper {
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

