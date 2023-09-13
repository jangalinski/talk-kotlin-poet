package io.github.jangalinski.talks._spike

import com.squareup.kotlinpoet.*
import io.github.jangalinski.talks.HelloWorld
import io.github.jangalinski._test.TestFixtures
import io.github.jangalinski._test.TestFixtures.assertThatCompilationIsOk
import io.github.jangalinski._test.TestFixtures.fqn
import io.github.jangalinski._test.TestFixtures.listContent
import io.github.jangalinski.talks.helloWorldAsFiglet
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class GenerateHelloWorldImplTest {


  @Test
  fun `generate and run greetings`(@TempDir sourceDirectory: File) {
    val className = ClassName("code.talks", "HelloCodeTalks")

    val type = TypeSpec.classBuilder(className)
      .addSuperinterface(HelloWorld::class)
      .addKdoc("%L", "A simple hello world demo for the code talks audience.")
      .addFunction(
        FunSpec.builder("helloWorld")
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

    result.outputDirectory.listContent()

    assertThatCompilationIsOk(result)

    val loadedClass = result.classLoader.loadClass(file.fqn())!!
    val newInstance = loadedClass.getConstructor().newInstance() as HelloWorld

    println(newInstance.helloWorldAsFiglet())
  }
}
