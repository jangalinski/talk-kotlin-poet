package io.github.jangalinski.talks

import com.github.dtmo.jfiglet.FigFontResources
import com.github.dtmo.jfiglet.FigletRenderer

/**
 *
 * Surprise hidden below
 *
 *
 */
fun String.figlet() = FigletRenderer(FigFontResources.loadFigFontResource(FigFontResources.BIG_FLF)).renderText(this)
