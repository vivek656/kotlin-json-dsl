package latwal.kotlin.jsondsl.kotlinjsondsl.json.base

import latwal.kotlin.jsondsl.kotlinjsondsl.json.common.AbstractJsonDslTests
import org.junit.jupiter.api.Test

class JsonDataNodeWrapperTests : AbstractJsonDslTests() {

    companion object {
        private const val TAG = "JSON_DATA_TESTS"
    }
    private fun generateSimpleJsonData(): JsonDataJsonNodeWrapper {
        val jsonData = JsonDataJsonNodeWrapper()
        return jsonData.set("fieldOne" , "one")
    }

    @Test
    fun `create a json data`(){
        val data = JsonDataJsonNodeWrapper()
        //setting primitive
        data.set("fieldOne" , 1)
        //setting string
        data.set("fieldTwo" , "one")

        //setting another object
        val anotherObject = JsonDataJsonNodeWrapper()
        anotherObject.set("fieldOne" , "one")
        data.set("objectField" ,anotherObject)

        //settingArray
        val array = JsonDataJsonArrayNodeWrapper()
        val objectOne = JsonDataJsonNodeWrapper()
        objectOne.set("fieldOne" , "one")
        val objectTwo = JsonDataJsonNodeWrapper()
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
        val array = JsonDataJsonArrayNodeWrapper.fromCollection(jsonDataCollection)
        array.add(generateSimpleJsonData())
        array.addAll(jsonDataCollection)

        assert(array.getArray().isArray)
        assert(array.getArray().size() > jsonDataCollection.size)

        val stringifyData = array.toString()
        logInfo(TAG, stringifyData)
        assert(stringifyData.isNotBlank())
    }


}
