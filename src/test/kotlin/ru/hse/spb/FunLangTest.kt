package ru.hse.spb

import antlr.FunLangLexer
import antlr.FunLangParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream


abstract class FunLangTest {
     protected fun createParser(path: String): FunLangParser {
        val lexer = FunLangLexer(CharStreams.fromFileName(path))
        lexer.removeErrorListeners()
        lexer.addErrorListener(ErrorListener.INSTANCE)

        val tokens = CommonTokenStream(lexer)
        val parser = FunLangParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(ErrorListener.INSTANCE)
        return parser
    }

    protected fun createFullLink(fileName: String): String {
        return "${System.getProperty("user.dir")}/tests/$fileName"
    }
}