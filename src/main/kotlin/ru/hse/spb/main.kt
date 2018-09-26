package ru.hse.spb

import antlr.FunLangLexer
import antlr.FunLangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import java.nio.file.NoSuchFileException


fun main(args: Array<String>) {
    val file = args.getOrNull(0)
    if (file == null) {
        println("file not found")
        return
    }
    try {
        val lexer = FunLangLexer(CharStreams.fromFileName(file))
        lexer.removeErrorListeners()
        lexer.addErrorListener(ErrorListener.INSTANCE)

        val tokens = CommonTokenStream(lexer)
        val parser = FunLangParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(ErrorListener.INSTANCE)
        parser.file().accept(Executor(System.out))
    } catch (e: ParseCancellationException) {
        println(e.message)
    } catch (e: NoSuchFileException) {
        println("file not found")
    }
}