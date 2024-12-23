package latwal.kotlin.jsondsl.kotlinjsondsl.json.base

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.reflect.KClass


typealias JsonDataNode = JsonNode


open class JsonDataJsonNodeWrapper(jsonNode: JsonNode = serializer.createObjectNode()) {

    companion object {
        private val serializer = jacksonObjectMapper()
        private const val VALUE_KEY = "_json_value_key"

        fun createInitialObjectNode() = JsonDataJsonNodeWrapper(
                serializer.createObjectNode()
        )

        fun createInitialArrayNode() = JsonDataJsonArrayNodeWrapper(
                serializer.createArrayNode()
        )

        fun wrapAndGet(jsonNode: JsonNode): JsonDataJsonNodeWrapper {
            return when {
                jsonNode.isArray -> JsonDataJsonArrayNodeWrapper(jsonNode as ArrayNode)
                else -> JsonDataJsonNodeWrapper(jsonNode)
            }
        }

    }

    private val jsonNodeHolder: ObjectNode = serializer.createObjectNode()

    init {
        jsonNodeHolder.set<JsonNode>(VALUE_KEY, jsonNode)
    }

    protected fun getAsObject(): ObjectNode {
        return kotlin.runCatching { jsonNodeHolder.get(VALUE_KEY) as ObjectNode }
                .onFailure {
                    throw UnsupportedOperationException("The Json Data is not an Object, cannot get or set a property")
                }.getOrThrow()
    }

    @JsonIgnore
    open fun getNode(): JsonNode = jsonNodeHolder.get(VALUE_KEY)

    fun get(key: String): JsonDataJsonNodeWrapper? {
        return getAsObject().get(key)?.let(::wrapAndGet)
    }

    open fun set(key: String, any: Any): JsonDataJsonNodeWrapper {
        val jsonNode = getAsObject()
        jsonNode.set<JsonNode>(key, resolve(any))
        return wrapAndGet(jsonNode[key])
    }

    fun resolve(any: Any): JsonNode {
        return when (any) {
            is JsonDataJsonArrayNodeWrapper -> any.getArrayNode()
            is JsonDataJsonNodeWrapper -> any.getNode()
            else -> serializer.convertValue(any, JsonNode::class.java)
        }
    }

    @JsonIgnore
    open fun getArrayNode() = getNode().let {
        if(it.isArray) it
        else throw UnsupportedOperationException("The Json data is not an Array. Cannot get ArrayNode")
    }


    override fun toString() = getNode().toString()

}


class JsonDataJsonArrayNodeWrapper(jsonNode: ArrayNode = serializer.createArrayNode()) : JsonDataJsonNodeWrapper(jsonNode) {

    companion object {
        private val serializer = jacksonObjectMapper()
        private const val VALUE_KEY = "_json_value_key"

        fun fromCollection(collections: Collection<*>): JsonDataJsonArrayNodeWrapper {
            val array = createInitialArrayNode()
            array.addAll(collections)
            return array
        }
    }

    private val jsonNodeHolder: ObjectNode = serializer.createObjectNode()

    init {
        jsonNodeHolder.set<ArrayNode>(VALUE_KEY, jsonNode)
    }

    override fun getNode(): JsonNode = jsonNodeHolder.get(VALUE_KEY)

    override fun getArrayNode() = getNode() as ArrayNode

    fun get(indexedValue: Int): JsonDataJsonNodeWrapper? {
        return getNode().get(indexedValue)?.let {
            wrapAndGet(it)
        }
    }

    fun set(indexedValue: Int, any: Any): JsonDataJsonNodeWrapper {
        getArrayNode()[indexedValue] = resolve(any)
        return wrapAndGet(getArrayNode()[indexedValue])
    }

    fun addAll(collections: Collection<*>) {
        collections.filterNotNull().forEach {
            add(it)
        }
    }

    fun add(any: Any) {
        getArrayNode().add(resolve(any))
    }

    override fun toString() = getNode().toString()

}


class TypedJsonData<T : Any>(private val clazz: KClass<T> , objectAsJson : JsonDataNode) : JsonDataJsonNodeWrapper(objectAsJson) {
    companion object {
        private val serializer = jacksonObjectMapper()

        fun <E : Any> from(data : E): TypedJsonData<out E> {
            val json = serializer.convertValue(data, JsonDataNode::class.java)
            val typedJson = TypedJsonData(clazz = data::class, objectAsJson = json)
            return typedJson
        }

        fun <E : Any> from(clazz: KClass<E>, data : Map<String,Any>): TypedJsonData<E> {
            val json = serializer.convertValue(data, JsonDataNode::class.java)
            val typedJson = TypedJsonData(clazz,json)
            return typedJson
        }

        fun <E : Any> from(clazz: KClass<E>, data : JsonDataJsonNodeWrapper): TypedJsonData<E> {
            val json = serializer.convertValue(data.getNode(), JsonDataNode::class.java)
            val typedJson = TypedJsonData(clazz,json)
            return typedJson
        }

    }

    init {
        checkJson(jsonNode = objectAsJson)
    }



    private fun checkJson(jsonNode: JsonNode) {
       kotlin.runCatching { serializer.convertValue(jsonNode,clazz.java) }
                .onFailure {
                    throw IllegalArgumentException("Typed jsonData, attached to type ${clazz.simpleName} cannot be constructed, from jsonData provided")
                }
    }

    override fun set(key: String, any: Any): JsonDataJsonNodeWrapper {
        val currentValue = get(key)
        val node = serializer.convertValue(getAsObject(),ObjectNode::class.java)
        node.set<JsonNode>(key,resolve(any))
        kotlin.runCatching { serializer.convertValue(node,clazz.java) }
                .onSuccess {
                    super.set(key, any)
                }.onFailure {
                    currentValue?.let { super.set(key, currentValue) }
                    throw IllegalArgumentException("Typed jsonData, attached to type ${clazz.simpleName} cannot be constructed , after inserting the latest data,for key $key value $any")
                }
        return get(key)!!
    }
}