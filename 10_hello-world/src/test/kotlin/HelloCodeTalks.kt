package io.github.jangalinski.talks

import com.squareup.kotlinpoet.*
import io.github.jangalinski.talks._test.TestFixtures
import io.github.jangalinski.talks._test.TestFixtures.assertThatCompilationIsOk
import io.github.jangalinski.talks._test.TestFixtures.fqn
import io.github.jangalinski.talks._test.TestFixtures.kotlinTestCompile
import org.junit.jupiter.api.Test

class HelloCodeTalks {

  @Test
  fun `say hello to code talk audience`() {
    val name = ClassName("code.talks", "SayHelloToAudience")

    val type = TypeSpec.classBuilder(name)
      .addSuperinterface(HelloWorld::class)
      .addKdoc("%L", "Simple hello world class")
      .addFunction(FunSpec.builder("helloWorld")
        .returns(String::class)
        .addModifiers(KModifier.OVERRIDE)
        .addCode("return %S", "Hello Code Talks 2023!")
        .build())
      .build()

    val file = FileSpec.builder(name).addType(type).build()


    println(file)

    val result = kotlinTestCompile(file)

    assertThatCompilationIsOk(result)

    val klass = result.classLoader.loadClass(file.fqn())
    val instance = klass.getConstructor().newInstance() as HelloWorld

    println(instance.helloWorld().figlet())
  }
}
