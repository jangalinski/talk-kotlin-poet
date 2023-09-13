package io.github.jangalinski.talks

import com.squareup.kotlinpoet.*
import io.github.jangalinski.talks._test.TestFixtures
import io.github.jangalinski.talks._test.TestFixtures.assertThatCompilationIsOk
import io.github.jangalinski.talks._test.TestFixtures.fqn
import io.github.jangalinski.talks._test.TestFixtures.kotlinTestCompile
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class Dummy {

  @Test
  fun `say hello to ct`() {
    val name = ClassName("code.talks", "Hello")

    val type = TypeSpec.classBuilder(name)
      .addSuperinterface(HelloWorld::class)
      .addFunction(FunSpec.builder("helloWorld")
        .addModifiers(KModifier.OVERRIDE)
        .returns(String::class)
        .addCode("return %S", "Hello Code Talks 2023!")
        .build())
      .build()

    val file = FileSpec.builder(name)
      .addType(type)
      .build()

    println(file)

    val result = kotlinTestCompile(file)

    assertThatCompilationIsOk(result)

    val generated = result.classLoader.loadClass(file.fqn())!!
    val instance = generated.getConstructor().newInstance() as HelloWorld

    println(instance.helloWorld().figlet())
  }
}
