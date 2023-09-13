package io.github.jangalinski.talks

import com.squareup.kotlinpoet.*
import io.github.jangalinski.talks.TestFixtures.assertThatCompilationIsOk
import io.github.jangalinski.talks.TestFixtures.figlet
import io.github.jangalinski.talks.TestFixtures.fqn
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GenerateGreetingServiceImplTest {


  @Test
  fun `generate and run greetings`(@TempDir sourceDirectory: File) {
    val className = ClassName("code.talks", "HelloCodeTalksService")

    val type = TypeSpec.classBuilder(className)
      .addSuperinterface(GreetingService::class)
      .addKdoc("%L", "A simple hello world demo for the code talks audience.")
      .addFunction(
        FunSpec.builder("sayHello")
          .addModifiers(KModifier.OVERRIDE)
          .returns(String::class)
          .addCode("return %S", "Greetings, Lukas!")
          .build()
      )
      .build()

    val file = FileSpec.builder(className).addType(type).build()
    file.writeTo(sourceDirectory)

    println(file)

    val result = TestFixtures.kotlinTestCompile(file)

    assertThatCompilationIsOk(result)

    val loadedClass = result.classLoader.loadClass(file.fqn())!!

    println("""${(loadedClass.getConstructor().newInstance() as GreetingService).sayHello().figlet()}""".trimIndent())
  }
}
