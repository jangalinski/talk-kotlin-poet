package io.github.jangalinski.talks

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec



fun main() {

  val file = FileSpec.builder(ClassName("","dummy"))
    .addFileComment("first example")
    .addFunction(
      FunSpec.builder("main")
        .addStatement("""println("Hello code talks!")""")
        .build()
    ).build()

    println(file)

}
