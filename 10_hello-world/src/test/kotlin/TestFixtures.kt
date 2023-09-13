package io.github.jangalinski.talks

import com.github.dtmo.jfiglet.FigFontResources
import com.github.dtmo.jfiglet.FigletRenderer
import com.squareup.kotlinpoet.FileSpec
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.github.jangalinski.talks._test.KotlinTestCompilerLoggingMessageCollector
import org.assertj.core.api.Assertions
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.util.*

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

  fun File.listContent() = Files.walk(this.toPath()).forEach { println(it) }

  fun compilerArgs(sourceDirectory: File) = K2JVMCompilerArguments().apply {
    freeArgs += sourceDirectory.toString()
    classpath = System.getProperty("java.class.path")
    noStdlib = true
    noReflect = true
    this.destination
  }

  fun compile(compilerArguments: K2JVMCompilerArguments) {
    K2JVMCompiler().exec(KotlinTestCompilerLoggingMessageCollector(CompilerMessageSeverity.ERROR), Services.EMPTY, compilerArguments)
  }

  fun assertThatCompilationIsOk(result: KotlinCompilation.Result) {
    Assertions.assertThat(result.exitCode)
      .`as` { "failed with : ${result.messages.lines().filter { it.startsWith("e:") }}" }
      .isEqualTo(KotlinCompilation.ExitCode.OK)
  }

  fun String.figlet() = FigletRenderer(FigFontResources.loadFigFontResource(FigFontResources.BIG_FLF)).renderText(this)

}
