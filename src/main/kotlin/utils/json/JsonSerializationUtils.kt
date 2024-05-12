package utils.json

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.restassured.internal.mapping.Jackson2Mapper
import io.restassured.response.ResponseBodyExtractionOptions
import java.lang.reflect.Type
import utils.json.JsonSerializationUtils.constructType
import utils.json.JsonSerializationUtils.objectMapper

object JsonSerializationUtils {
    /** ObjectMapper для сериализации/десериализации Json  */
    val objectMapper: ObjectMapper by lazy { ObjectMapper().registerKotlinModule() }

    fun <T, E> constructType(tClass: Class<T>, eClass: Class<E>): JavaType =
        objectMapper.typeFactory.constructParametricType(tClass, eClass)
}

/** Позволяет десериализовать Json-строку как список объектов
 * ```
 * val tagList = tagsJsonString.deserializeList<GitHubTagDto>()
 * ```
 */
inline fun <reified T : Any> String.deserializeList(): List<T> = objectMapper.readValue(
    this,
    constructType(List::class.java, T::class.java)
)

/**
 * Позволяет десериализовать результат запроса REST-assured [ResponseBodyExtractionOptions] в объект класса [T].
 * ```
 * extractableResponse.responseAs<List<GitHubTagDto>>(constructType(List::class.java, GitHubTagDto::class.java))
 * ```
 */
fun <T> ResponseBodyExtractionOptions.responseAs(type: Type): T {
    return this.`as`(type, Jackson2Mapper { _, _ -> objectMapper })
}