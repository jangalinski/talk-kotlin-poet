package io.github.jangalinski.talks.avro

import io.github.jangalinski.talks._ipc.JavaIpcCodeTalksQueries
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference
import org.apache.avro.ipc.Callback as AvroCallback

internal class JavaIpcCodeTalksQueriesTest {

  @Test
  fun `ipc via callback`() {
    val reference = AtomicReference<List<CodeTalksTalk>>()

    JavaIpcCodeTalksQueries().findTalksByKeyword("Sourcing", object : AvroCallback<List<CodeTalksTalk>> {
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
