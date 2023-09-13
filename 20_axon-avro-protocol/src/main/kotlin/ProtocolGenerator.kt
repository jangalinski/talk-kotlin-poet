package io.github.jangalinski.talks

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.github.jangalinski.talks._helper.AvroHelper
import io.github.jangalinski.talks._helper.AvroHelper.copyMessageRequestSchema
import io.github.jangalinski.talks._helper.AvroHelper.typeName
import org.apache.avro.Protocol.Message
import org.apache.avro.Schema
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import java.io.File
import java.util.concurrent.CompletableFuture


fun main() {
  val protocol = AvroHelper.CODE_TALKS_QUERIES

  val message = protocol.messages["findTalksByKeyword"]!!

  // request className and typeSpec
  val request = dataClassForRecordSchema(copyMessageRequestSchema(message, protocol.namespace))

  // response className and typeSpec
  val response = dataClassForRecordSchema(message.response.elementType)


  val responseList = List::class.asTypeName().parameterizedBy(response.first)

  val handler = queryHandlerInterface(message, request.first, responseList)

  val client = queryGatewayExtensionFunction(request.first, response.first)

  val file = FileSpec.builder(protocol.namespace, protocol.name)
    .addType(request.second)
    .addType(response.second)
    .addType(handler)
    .addFunction(client)
    .build()

  println(file)

  println(File(Thread.currentThread().getContextClassLoader().getResource("")!!.getPath()))
  file.writeTo(File(Thread.currentThread().getContextClassLoader().getResource("")!!.getPath()))
}

fun queryHandlerInterface(message: Message, queryType: ClassName, resultType: TypeName) =
  TypeSpec.interfaceBuilder(message.name.typeName("QueryHandler"))
    .addFunction(
      FunSpec.builder(message.name)
        .addKdoc("%L", message.doc ?: "-")
        .addParameter("query", queryType)
        .addModifiers(KModifier.ABSTRACT)
        .returns(resultType)
        .build()
    )
    .build()

fun queryGatewayExtensionFunction(queryType: ClassName, resultType: ClassName): FunSpec {
  val queryParameter = ParameterSpec.builder("query", queryType).build()

  val responseTypesClass = ResponseTypes::class.asClassName()

  val completableFuture = CompletableFuture::class.asTypeName()
    .parameterizedBy(List::class.asTypeName().parameterizedBy(resultType))

  return FunSpec.builder("query")
    .addKdoc(
      """
      @param query %T
      @returns %T
    """.trimIndent(), queryType,completableFuture
    )
    .receiver(QueryGateway::class)
    .addParameter(queryParameter)
    .returns(completableFuture)
    .addCode(
      "return this.query(%L, %M(%T::class.java))",
      queryParameter.name,
      responseTypesClass.member("multipleInstancesOf"),
      resultType
    )
    .build()
}

fun dataClassForRecordSchema(schema: Schema): Pair<ClassName, TypeSpec> {
  val className = ClassName(schema.namespace, schema.name)

  val parameters = schema.fields.map {
    ParameterSpec.builder(it.name(), AvroHelper.typeName(it.schema().type)).build()
  }

  val constructor = FunSpec.constructorBuilder()
    .addParameters(parameters)
    .build()
  val properties = parameters.map {
    PropertySpec.builder(it.name, it.type)
      .initializer("%N", it)
      .build()
  }

  return className to TypeSpec.classBuilder(className)
    .addModifiers(KModifier.DATA)
    .primaryConstructor(constructor)
    .addProperties(properties)
    .build()

}
