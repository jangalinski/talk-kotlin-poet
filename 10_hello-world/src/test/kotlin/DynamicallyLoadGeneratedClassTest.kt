package io.github.jangalinski.talks

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.github.jangalinski.talks._test.KotlinTestCompilerLoggingMessageCollector
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files


 class DynamicallyLoadGeneratedClassTest {

  interface Greeting : () -> String

   @Test
   fun `create for interface and load class`(@TempDir out: File) {

     val file = FileSpec.builder("foo", "Bar")
       .addType(TypeSpec.classBuilder("Bar")
         .addSuperinterface(Greeting::class)
         .addFunction(FunSpec.builder("invoke")
           .addModifiers(KModifier.OVERRIDE)
           .returns(String::class)
           .addCode("return %S", "Code Talks 2023")
           .build())
         .build())
       .build()

     val result = KotlinCompilation().apply {
       sources = listOf(SourceFile.kotlin(file.packageName + "." + file.name + ".kt", file.toString()))
       inheritClassPath = true
     }.compile()

     println(result.messages)

     println(result.compiledClassAndResourceFiles)

     val hello = (result.classLoader.loadClass("${file.packageName}.${file.name}").getConstructor().newInstance() as Greeting)()

     println(hello)
   }


  @Test
  fun `create and load class`(@TempDir out: File) {
    println(out)

    val file = FileSpec.builder("foo", "Bar")
      .addType(TypeSpec.classBuilder("Bar")
        .addSuperinterface(Greeting::class)
        .addFunction(FunSpec.builder("invoke")
          .addModifiers(KModifier.OVERRIDE)
          .returns(String::class)
          .addCode("return %S", "Code Talks 2023")
          .build())
        .build())
      .build()

    println(file)
    file.writeTo(out)

    Files.walk(out.toPath()).forEach {
      println(it)
    }

    val compilerArgs = K2JVMCompilerArguments().apply {
      freeArgs += out.toString()
      classpath = System.getProperty("java.class.path")
      noStdlib = true
      noReflect = true
      this.destination
    }

    K2JVMCompiler().exec(KotlinTestCompilerLoggingMessageCollector(CompilerMessageSeverity.ERROR), Services.EMPTY, compilerArgs)
  }
}
