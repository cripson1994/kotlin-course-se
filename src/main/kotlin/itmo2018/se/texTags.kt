package itmo2018.se

import java.io.PrintStream
import java.util.ArrayList


fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

class Document : Tag("document") {
    private var classFlag = false
    private val configs = ArrayList<Config>();

    fun documentClass(name: String) {
        if (classFlag) {
            throw DocumentDefinedException("Multiple class definitions found")
        } else if (children.size > 0) {
            throw DocumentDefinedException("Packages must be declared after `documentClass` tag and before `frame` tags")
        }
        classFlag = true
        configs.add(Config("documentClass", name))
    }

    fun usepackage(name: String, vararg args: String) {
        if (children.size > 0 || !classFlag) {
            throw DocumentDefinedException("Packages must be declared after `documentClass` tag and before `frame` tags")
        }
        configs.add(Config("usepackage", name, *args))
    }

    override fun render(out: PrintStream, indent: String) {
        if (!classFlag && children.size > 0) {
            throw DocumentDefinedException("Document class not found")
        }
        for (config in configs) {
            config.render(out, indent)
        }
        super.render(out, indent)
    }

    fun render(out: PrintStream) {
        render(out, "")
    }

    fun frame(frameTitle: String, vararg args: StringPair, init: Frame.() -> Unit): Frame {
        val frame = initTag(Frame(), init)
        val params = args.map { x -> "${x.first}=${x.second}" }.toTypedArray()
        frame.children.addFirst(Config("frametitle", frameTitle, *params))
        return frame
    }

    fun frame(init: Frame.() -> Unit) = initTag(Frame(), init)
}

class Frame() : ContentHolder("frame")
class Alignment(align: Align) : ContentHolder(align.cmd)
class Item : ContentHolder("item")

class Itemize : ItemHolder("itemize")
class Enumerate : ItemHolder("enumerate")


class CustomTag(name: String, vararg args: String) : TagWithText(name, *args)

abstract class ContentHolder(name: String) : TagWithText(name) {
    fun alignment(align: Align, init: Alignment.() -> Unit) = initTag(Alignment(align), init)

    fun itemize(init: Itemize.() -> Unit) = initTag(Itemize(), init)

    fun enumerate(init: Enumerate.() -> Unit) = initTag(Enumerate(), init)

    fun customTag(name: String, vararg args: StringPair, init: CustomTag.() -> Unit): CustomTag {
        val params = args.map { x -> "${x.first}=${x.second}" }.toTypedArray()
        return initTag(CustomTag(name, *params), init)
    }
}

open class ItemHolder(name: String) : Tag(name) {
    fun item(init: Item.() -> Unit) = initTag(Item(), init)
}

enum class Align(val cmd: String) {
    Left("flushleft"),
    Right("flushright"),
    Centre("center")
}