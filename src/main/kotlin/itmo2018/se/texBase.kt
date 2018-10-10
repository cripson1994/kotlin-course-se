package itmo2018.se

import java.io.PrintStream
import java.util.*

typealias StringPair = Pair<String, String>

@DslMarker
annotation class TexTagMarker

interface Element {
    fun render(out: PrintStream, indent: String)
}

class Config(val texCommand: String, val name: String, vararg val args: String) : Element {
    override fun render(out: PrintStream, indent: String) {
        out.print("$indent\\$texCommand")
        if (args.size > 0) {
            out.print("[${args.joinToString { x -> x }}]")
        }
        out.println("{$name}")
    }
}

@TexTagMarker
open class Tag(val name: String, vararg val arguments: String) : Element {
    val children = LinkedList<Element>()

    private fun printArgs(out: PrintStream) {
        if (arguments.size > 0) {
            out.print("[${arguments.joinToString { x -> x }}]")
        }
        out.println()
    }

    override fun render(out: PrintStream, indent: String) {
        out.print("$indent\\begin{$name}")
        printArgs(out)
        for (child in children) {
            child.render(out, "$indent  ")
        }
        out.println("$indent\\end{$name}")
    }


    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }
}

open class TagWithText(name: String, vararg args: String) : Tag(name, *args) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    operator fun String.not() {
        children.add(TextElement("\$${this}\$"))
    }
}

class TextElement(val text: String) : Element {
    override fun render(out: PrintStream, indent: String) {
        out.println("$indent$text")
    }
}