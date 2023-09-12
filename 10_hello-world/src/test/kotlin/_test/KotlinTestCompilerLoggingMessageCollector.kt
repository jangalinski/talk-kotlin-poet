package io.github.jangalinski.talks._test

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSourceLocation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

private const val TAB_WIDTH = 4

private fun PrintStream.printExcerptLine(line: String) {
  this.println(" ".repeat(TAB_WIDTH) + "> " + line.replace("\t", " ".repeat(TAB_WIDTH)))
}


/**
 * [MessageCollector] for use in unit tests. Logs all messages together
 * together with an excerpt of the code in question. Collects information about
 * messages with a severity equal to or worse than [errorSeverity].
 */
class KotlinTestCompilerLoggingMessageCollector(private val errorSeverity: CompilerMessageSeverity)
: MessageCollector {
	private var hasErrors = false
	private val errorCount = HashMap<CompilerMessageSeverity, Int>()

	private fun chooseStream(severity: CompilerMessageSeverity) =
		when (severity) {
			CompilerMessageSeverity.EXCEPTION,
            CompilerMessageSeverity.ERROR,
            CompilerMessageSeverity.STRONG_WARNING,
            CompilerMessageSeverity.WARNING -> System.err
			CompilerMessageSeverity.INFO,
            CompilerMessageSeverity.LOGGING,
            CompilerMessageSeverity.OUTPUT -> System.out
		}

	private fun reportMessageWithSeverity(severity: CompilerMessageSeverity) {
		if (severity <= errorSeverity) {
			hasErrors = true
			errorCount[severity] = (errorCount[severity] ?: 0) + 1
		}
	}

	/**
	 * Whether any message with a severity equal to or worse than
	 * [errorSeverity] was encountered.
	 */
	override fun hasErrors() = hasErrors

  override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageSourceLocation?) {
    val stream = chooseStream(severity)
    stream.println("$severity: $message")
    if (location != null) {
      stream.print("    @ $location")
//   FIXME   if (location.hasExcerpt()) {
//        stream.print(":\n")
//        location.printExcerpt(stream)
//      } else {
//        stream.print("\n")
//      }
    }
    stream.println()
    reportMessageWithSeverity(severity)
  }

  /**
	 * A messages that describes how many messages of each severity equal to or
	 * worse than [errorSeverity] have been encountered.
	 */
	val errorMessage
		get() = CompilerMessageSeverity.values().asSequence()
			.filter { it <= errorSeverity }
			.filter { errorCount.containsKey(it) }
			.map { "${errorCount[it]} ${it.presentableName}s" }
			.joinToString(", ")

	override fun clear() {
		hasErrors = false
	}

	private fun CompilerMessageLocation.printExcerpt(stream: PrintStream) {
		val linesBefore = 3
		val linesAfter = 2
		val linesToPrint = Files.lines(Paths.get(this.path))
			.skip(Math.max(0L, this.line - linesBefore * 1L))
			.limit(linesBefore + linesAfter * 1L)
			.collect(Collectors.toList())
		val tabsInErrorLine = linesToPrint[linesBefore - 1].count { it == '\t' }
		val errorMarkerIndent = this.column + ((TAB_WIDTH - 1) * tabsInErrorLine) - 1;
		linesToPrint.take(linesBefore).forEach(stream::printExcerptLine)
		stream.printExcerptLine("-".repeat(errorMarkerIndent) + "^")
		linesToPrint.takeLast(linesAfter).forEach(stream::printExcerptLine)
	}

	private fun CompilerMessageLocation.hasExcerpt() = this.line > -1 && this.column > -1;
}
