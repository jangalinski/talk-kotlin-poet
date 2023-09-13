package io.github.jangalinski.talks._helper

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import org.apache.avro.Protocol
import org.apache.avro.Schema

object AvroHelper {

  val CODE_TALKS_QUERIES = Protocol.parse({}::class.java.getResource("/avro/CodeTalksQueries.avpr")!!.openStream())

  fun String.typeName(suffix: String) = replaceFirstChar(Char::uppercase) + suffix

  fun typeName(type: Schema.Type): TypeName = when (type) {
    Schema.Type.STRING -> String::class.asTypeName()
    else -> TODO("not implemented")
  }

  fun copyMessageRequestSchema(message: Protocol.Message, namespace: String): Schema = Schema.createRecord(
    message.name.typeName("Request"),
    message.request.doc,
    namespace,
    message.request.isError,
    message.request.fields.map { Schema.Field(it, it.schema()) }
  )

}
