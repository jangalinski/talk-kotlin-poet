package io.github.jangalinski.talks._test

import com.squareup.kotlinpoet.FileSpec
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files

object TestFixtures {

  /**
   * This is the kotlin-compile-test abstraction of the K2 compiler. It is easier to use in tests.
   */
  fun kotlinTestCompile(fileSpec: FileSpec): KotlinCompilation.Result = KotlinCompilation().apply {
    sources = listOf(fileSpec.sourceFile())
    inheritClassPath = true
    messageOutputStream = ByteArrayOutputStream()
  }.compile()

  fun FileSpec.fqn() = "${this.packageName}.${this.name}"
  fun FileSpec.fileName() = "${this.fqn()}.kt"
  fun FileSpec.sourceFile() = SourceFile.kotlin(this.fileName(), this.toString())

  fun File.printContent() = Files.walk(this.toPath()).forEach { println(it) }

  fun assertThatCompilationIsOk(result: KotlinCompilation.Result) {
    Assertions.assertThat(result.exitCode)
      .`as` { "failed with : ${result.messages.lines().filter { it.startsWith("e:") }}" }
      .isEqualTo(KotlinCompilation.ExitCode.OK)
  }
}
