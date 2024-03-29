package latwal.kotlin.jsondsl.kotlinjsondsl.json.archived

import latwal.kotlin.jsondsl.kotlinjsondsl.json.common.AbstractJsonDslTests
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@SuppressWarnings("deprecated")
class JsonDataTests : AbstractJsonDslTests() {

    companion object {
        private const val TAG = "JSON_DATA_TESTS"
    }
    private fun generateSimpleJsonData(): JsonData {
        val jsonData = JsonData()
        return jsonData.set("fieldOne" , "one")
    }

    class BuilderAssertionContext(private val jsonData: JsonData) {

        private val topLevelJson = jsonData.asJsonNode()

        fun isAnArray() = topLevelJson.isArray
        fun isAnObject() = topLevelJson.isObject
        infix fun containsObjectAt(childPointer : String) {
            val pointer = "/${childPointer.trimStart('/')}"
            topLevelJson.at(pointer).isObject
        }

        infix fun containsField(field : String){
            assert(jsonData.get(field)!=null)
        }

        infix fun containsArrayAt(childPointer : String) {
            val pointer = "/${childPointer.trimStart('/')}"
            topLevelJson.at(pointer).isArray
        }

    }

    private infix fun JsonData.asserting(init : BuilderAssertionContext.() -> Unit): JsonData {
        BuilderAssertionContext(this).init()
        return this
    }

    @Test
    fun `create a json data`(){
        val data = JsonData()
        //setting primitive
        data.set("fieldOne" , 1)
        //setting string
        data.set("fieldTwo" , "one")

        //setting another object
        val anotherObject = JsonData()
        anotherObject.set("fieldOne" , "one")
        data.set("objectField" ,anotherObject)

        //settingArray
        val array = ArrayJsonData()
        val objectOne = JsonData()
        objectOne.set("fieldOne" , "one")
        val objectTwo = JsonData()
        objectTwo.set("fieldOne" , "one")
        array.add(objectOne)
        array.add(objectTwo)
        data.set("arrayField" , array)

        //setting data class
        val simpleDataClass = TestDataClass(fieldOne = "fieldOne")
        data.set("simpleDataObjectField" , simpleDataClass)

        data asserting {
            isAnObject()
            containsField("fieldOne")
            containsObjectAt("objectField")
            containsArrayAt("arrayField")
            containsObjectAt("simpleDataObjectField")
        }

        val stringifyData = data.toString()
        logInfo(TAG,stringifyData)
        assert(stringifyData.contains("fieldOne"))

    }

    @Test
    fun `creating simple array nodes`(){
        val jsonDataCollection = listOf(
                generateSimpleJsonData(),
                generateSimpleJsonData(),
                generateSimpleJsonData()
        )
        val array = ArrayJsonData.fromCollection(jsonDataCollection)
        array.add(generateSimpleJsonData())
        array.addAll(jsonDataCollection)

        assert(array.asJsonNode().isArray)
        assert(array.size() > jsonDataCollection.size)

        val stringifyData = array.toString()
        logInfo(TAG, stringifyData)
        assert(stringifyData.isNotBlank())
    }

    @Test
    fun `attempt to create a primitive data , with non primitive value result in exception`(){
        val nonPrimitiveData = TestDataClass(fieldOne = "one")
        assertThrows<IllegalArgumentException> {
            PrimitiveDataNode(nonPrimitiveData)
        }
    }

}
