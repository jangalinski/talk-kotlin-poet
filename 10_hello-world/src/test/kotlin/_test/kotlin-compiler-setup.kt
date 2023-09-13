package io.github.jangalinski.talks._test

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import java.io.File

val PRINTING_MESSAGE_COLLECTOR = PrintingMessageCollector(System.err, MessageRenderer.PLAIN_FULL_PATHS, false)

fun compilerArgs(out: File) = K2JVMCompilerArguments().apply {
  freeArgs += out.toString()
  classpath = System.getProperty("java.class.path")
  noStdlib = true
  noReflect = true
}
