package latwal.kotlin.jsondsl.kotlinjsondsl.json.common

import latwal.kotlin.jsondsl.kotlinjsondsl.json.base.JsonData
import org.slf4j.LoggerFactory

abstract class AbstractJsonDslTests {

    private val logger = LoggerFactory.getLogger(AbstractJsonDslTests::class.java)
    protected fun logInfo(tag : String , value : String){
        logger.info("${tag}: ${value}")
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

    protected infix fun JsonData.asserting(init : BuilderAssertionContext.() -> Unit): JsonData {
        BuilderAssertionContext(this).init()
        return this
    }

    protected data class TestDataClass(
            val fieldOne: String
    )

}