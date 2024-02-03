package latwal.kotlin.jsondsl.kotlinjsondsl.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

typealias JsonDataNode = JsonNode


open class JsonData {
    
    companion object {
        val serializer = jacksonObjectMapper()
    }
    private val properties = mutableMapOf<String, JsonData>()

    fun set(key: String, jsonData: JsonData): JsonData {
        properties[key] = jsonData
        return properties[key]!!
    }

    open fun set(key: String, any: Any): JsonData {
        val objectValue = resolve(any)
        properties[key] = objectValue
        return properties[key]!!
    }

    fun get(key : String) : JsonData? {
        return properties[key]
    }

    protected fun resolve(any: Any) : JsonData{
        return  if(any is JsonData) any
        else if (PrimitiveDataNode.objectIsPrimitive(any))  PrimitiveDataNode(any)
        else if (any is Collection<*>) ArrayJsonData.fromCollection(any)
        else LeafObjectDataNode(any)
    }
    
    override fun toString(): String {
        if(properties.isEmpty()) return "{}"
        val sb= StringBuilder()
        sb.append("{")
        properties.toList().mapIndexed { ind,  (key,value) ->
            sb.append("\r\n").levelIdent(1).append("\"").append(key).append("\"").append(": ")
                    .append(value.toString(1))
            if(ind!=properties.size-1) sb.append(",")

        }
        sb.append("\r\n}")
        return sb.toString()
    }

    open fun toString(level : Int): String {
        if(properties.isEmpty()) return "{}"
        val sb= StringBuilder()

        sb.append("{")
        properties.toList().forEachIndexed {ind, (key,value) ->
            sb.append("\r\n").levelIdent(level+1).append("\"").append(key).append("\"").append(":")
                    .append(value.toString(1))
            if(ind!=properties.size-1) sb.append(",")
        }
        sb.append("\r\n")
        sb.levelIdent(level).append("}")
        return sb.toString()
    }
}

class LeafObjectDataNode(nodeValue: Any) : JsonData() {
    companion object {
        val commonSerializer = jacksonObjectMapper()
    }

    private val nodeValueJson: JsonDataNode

    init {
        nodeValueJson = commonSerializer.convertValue(nodeValue, JsonDataNode::class.java)
    }
    override fun toString(): String = serializer.writeValueAsString(this.nodeValueJson)

    override fun toString(level: Int) = toString()
}

class PrimitiveDataNode(private val any: Any) : JsonData() {
    init {
        require(any::class.javaPrimitiveType != null) {
            "primitive data node only accepts java primitive type and their wrappers"
        }
    }

    companion object {
        fun objectIsPrimitive(any: Any): Boolean {
            return any::class.javaPrimitiveType != null
        }
    }

    override fun toString(): String = serializer.writeValueAsString(any)
    override fun toString(level: Int) = toString()
}

class ArrayJsonData : JsonData(){
    private val arrayList = ArrayList<JsonData>()

    companion object {
       // val serializationMapper = jacksonObjectMapper()
        fun fromCollection(collections: Collection<*>): ArrayJsonData {
            val array = ArrayJsonData()
            array.addAll(collections)
            return array
        }
    }


    fun add(jsonData : JsonData){
        arrayList.add(jsonData)
    }

    fun add(any: Any){
        arrayList.add(resolve(any))
    }

    fun addAll(collections: Collection<*>){
        collections.filterNotNull().forEach {
            add(it)
        }
    }

    override fun toString(): String {
        if(arrayList.isEmpty()) return "[]"
        val sb = StringBuilder()
        sb.append("[")
        arrayList.forEachIndexed() {ind,json ->
            sb.levelIdent(1)
            sb.append(json.toString(1))
            if(ind!=arrayList.lastIndex) sb.append(",")
        }
        sb.append("\r\n")
        sb.append("]")
        return sb.toString()
    }

    override fun toString(level: Int): String {
        if(arrayList.isEmpty()) return "[]"
        val sb = StringBuilder()
        sb.append("[")
        arrayList.forEachIndexed() {ind,json ->
            sb.append("\r\n")
            sb.levelIdent(level+1)
            sb.append(json.toString(level+1))
            if(ind!=arrayList.lastIndex) sb.append(",")
        }
        sb.append("\r\n")
        sb.levelIdent(level)
        sb.append("]")
        return sb.toString()
    }

}

private fun StringBuilder.levelIdent(level: Int): StringBuilder {
    (1..level).forEach { _ -> append("  ") }
    return this
}