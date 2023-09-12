package io.github.jangalinski.talks

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import org.apache.avro.Protocol
import org.apache.avro.Schema
import org.axonframework.queryhandling.QueryGateway
import java.util.concurrent.CompletableFuture

fun generate() {

  fun typeName(type: Schema.Type) : TypeName = when(type) {
    Schema.Type.RECORD -> TODO()
    Schema.Type.ENUM -> TODO()
    Schema.Type.ARRAY -> TODO()
    Schema.Type.MAP -> TODO()
    Schema.Type.UNION -> TODO()
    Schema.Type.FIXED -> TODO()
    Schema.Type.STRING -> String::class.asTypeName()
    Schema.Type.BYTES -> TODO()
    Schema.Type.INT -> TODO()
    Schema.Type.LONG -> TODO()
    Schema.Type.FLOAT -> TODO()
    Schema.Type.DOUBLE -> TODO()
    Schema.Type.BOOLEAN -> TODO()
    Schema.Type.NULL -> TODO()
  }

  fun dataClass4record(schema: Schema): Pair<ClassName,TypeSpec> {
    val className = ClassName(schema.namespace, schema.name)

    val parameters = schema.fields.map {
      ParameterSpec.builder(it.name(), typeName(it.schema().type)).build()
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


  val p = Protocol.parse({}::class.java.getResource("/avro/CodeTalksQueries.avpr")!!.openStream())

  val m = p.messages["findTalksByKeyword"]!!

  println(m.request.getField("query").schema())

  val q = m.request.getField("query").schema()

  val query = dataClass4record(q)
  val result = dataClass4record(m.response.elementType)


  println(query)
  println(result)

  val providerInterface = TypeSpec.interfaceBuilder(m.name.replaceFirstChar(Char::uppercase) + "QueryHandler")
    .addFunction(FunSpec.builder(m.name)
      .addParameter("query", query.first)
      .addModifiers(KModifier.ABSTRACT)
      .returns(List::class.asTypeName().parameterizedBy(result.first))
      .build())
    .build()



  CompletableFuture::class.asClassName().parameterizedBy(List::class.asTypeName().parameterizedBy(result.first))

  val queryParameter = ParameterSpec.builder("query", query.first).build()

  val extensionFn = FunSpec.builder("query")
    .receiver(QueryGateway::class)
    .addParameter(queryParameter)
    .returns(CompletableFuture::class.asClassName().parameterizedBy(List::class.asTypeName().parameterizedBy(result.first)))
    .addStatement(
      "return this.query(%P, %M(%T::class.java))", queryParameter.name, "listOf", result.first
    )
    .build()

  val file = FileSpec.builder(ClassName(p.namespace,p.name))
    .addType(query.second)
    .addType(result.second)

    .addType(providerInterface)

    .addFunction(extensionFn)
    .build()

  println(file)

}

fun main() {
  generate()
}
