package io.github.jangalinski.talks

import com.github.dtmo.jfiglet.FigFontResources.BIG_FLF
import com.github.dtmo.jfiglet.FigFontResources.loadFigFontResource
import com.github.dtmo.jfiglet.FigletRenderer

/**
 * A simple interface that says ... uhm ... hello!
 */
interface HelloWorld {

  fun helloWorld(): String

}


/**
 *
 * Surprise hidden below
 *
 *
 */
fun String.figlet() = FigletRenderer(loadFigFontResource(BIG_FLF)).renderText(this)
