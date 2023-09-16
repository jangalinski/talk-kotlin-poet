package io.github.jangalinski.talks._test

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File

val PRINTING_MESSAGE_COLLECTOR = PrintingMessageCollector(System.err, MessageRenderer.PLAIN_FULL_PATHS, false)

fun callKotlinCompiler(source:File) = K2JVMCompiler()
  .exec(PRINTING_MESSAGE_COLLECTOR, Services.EMPTY, compilerArgs(source))

fun compilerArgs(source: File) = K2JVMCompilerArguments().apply {
  freeArgs += source.toString()
  classpath = System.getProperty("java.class.path")
  noStdlib = true
  noReflect = true
}
