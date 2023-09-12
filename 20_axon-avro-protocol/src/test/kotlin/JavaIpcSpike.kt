package io.github.jangalinski.talks

import io.github.jangalinski.CodeTalksQueries.Callback
import io.github.jangalinski.CodeTalksTalk
import io.github.jangalinski.FindTalksByKeywordQuery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference
import org.apache.avro.ipc.Callback as AvroCallback

internal class JavaIpcSpike {

  class DummyCodeTalksQueries : Callback {

    override fun findTalksByKeyword(query: FindTalksByKeywordQuery): List<CodeTalksTalk> = CodeTalksDB.findByKeyword(query.keyword)

    override fun findTalksByKeyword(
      query: FindTalksByKeywordQuery,
      callback: AvroCallback<List<CodeTalksTalk>>
    ) {
      callback.handleResult(findTalksByKeyword(query))
    }
  }

  @Test
  fun `ipc via callback`() {
    val reference = AtomicReference<List<CodeTalksTalk>>()

    DummyCodeTalksQueries().findTalksByKeyword(FindTalksByKeywordQuery("Sourcing"), object : AvroCallback<List<CodeTalksTalk>> {
      override fun handleResult(result: List<CodeTalksTalk>) {
        reference.set(result)
      }

      override fun handleError(error: Throwable?) {
        TODO("Not yet implemented")
      }
    })


    assertThat(reference.get()).hasSize(1)

  }
}
