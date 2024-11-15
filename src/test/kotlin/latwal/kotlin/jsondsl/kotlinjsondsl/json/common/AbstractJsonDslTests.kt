package latwal.kotlin.jsondsl.kotlinjsondsl.json.common

import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonDataJsonNodeWrapper
import org.slf4j.LoggerFactory

abstract class AbstractJsonDslTests {

    private val logger = LoggerFactory.getLogger(AbstractJsonDslTests::class.java)
    protected fun logInfo(tag : String , value : String){
        logger.info("${tag}: ${value}")
    }



    protected data class TestDataClass(
            val fieldOne: String
    )

    protected data class TestDataClass2(
            val stringField: String,
            val integerField : Int,
            val objectField : TestDataClass
    )

    class NodeBuilderAssertionContext(private val jsonData: JsonDataJsonNodeWrapper) {

        private val topLevelJson = jsonData.getValue()

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

        fun jsonData() = jsonData

    }

    protected infix fun JsonDataJsonNodeWrapper.asserting(init : NodeBuilderAssertionContext.() -> Unit): JsonDataJsonNodeWrapper {
        NodeBuilderAssertionContext(this).init()
        return this
    }


}