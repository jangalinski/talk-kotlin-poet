package io.github.jangalinski.talks._ipc

import io.github.jangalinski.talks.CodeTalksDB
import io.github.jangalinski.talks.avro.CodeTalksQueries
import io.github.jangalinski.talks.avro.CodeTalksTalk
import org.apache.avro.ipc.Callback as AvroCallback

internal class JavaIpcCodeTalksQueries : CodeTalksQueries.Callback {

  override fun findTalksByKeyword(keyword: String): List<CodeTalksTalk> = CodeTalksDB.findByKeyword(keyword)

  override fun findTalksByKeyword(
    keyword: String,
    callback: AvroCallback<List<CodeTalksTalk>>
  ) {
    callback.handleResult(findTalksByKeyword(keyword))
  }
}

