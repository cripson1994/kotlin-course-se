package itmo2018.se

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class DSLTest {

    private fun createFullLink(fileName: String): String {
        return "${System.getProperty("user.dir")}/tests/$fileName"
    }

    private fun assertDocument(doc: Document, fileWithResult: String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteArrayOutputStream)
        doc.render(printStream)
        val actual = String(byteArrayOutputStream.toByteArray())
        val expected = File(createFullLink(fileWithResult)).readText()
        assertEquals(expected.trimIndent(), actual.trimIndent())
    }

    @Test(expected = DocumentDefinedException::class)
    fun twoClassesDefenite() {
        document {
            documentClass("class1")
            usepackage("package")
            documentClass("class2")
            frame {
            }
        }
    }

    @Test(expected = DocumentDefinedException::class)
    fun packageBeforClass() {
        document {
            usepackage("package")
            documentClass("class")
            frame {
            }
        }
    }

    @Test(expected = DocumentDefinedException::class)
    fun packageAmongFrames() {
        document {
            documentClass("class")
            usepackage("package1")
            frame {
            }
            usepackage("package2")
            frame {
            }
        }
    }

    @Test(expected = DocumentDefinedException::class)
    fun withoutClass() {
        document {
            usepackage("instead class")
            usepackage("package")
            frame {
            }
            usepackage("package2")
            frame {
            }
        }
    }

    @Test()
    fun withoutPackage() {
        document {
            documentClass("class")
            frame {
            }
            frame {
            }
        }
    }

    @Test()
    fun simpleDocumentConfiguration() {
        val doc = document {
            documentClass("class")
            usepackage("package1")
            usepackage("package2", "arg1", "arg2")
            frame {
            }
        }
        assertDocument(doc, "simpleDocumentConfiguration")
    }

    @Test
    fun testTextAndMathContent() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                +"start"
                +"formula:"
                !"a + b - c"
                +"formula number 2 and 3:"
                !"4 < 5"
                !"v + 8 = 4 / 4"
                itemize {
                }
                +"after tag"
                alignment(Align.Right) {
                    +"inner"
                }
            }
        }
        assertDocument(doc, "testTextAndMathContent")
    }

    @Test
    fun testFramePossibleTags() {
        val doc = document {
            documentClass("class")
            frame("frameTile", "a" to "1", "b" to "2", "c" to "3") {
                itemize {
                }
                enumerate {
                }
                +"test math formula:"
                !"5 + 5 - (4 * 3) ^ 2"
                alignment(Align.Left) {
                }
                customTag("myTag") {
                }
            }
            frame {
            }
        }
        assertDocument(doc, "testFramePossibleTags")
    }

    @Test
    fun testItemizePossibleTags() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                itemize {
                    item {
                        +"first item"
                    }
                    for (str in arrayOf("1", "2", "3")) {
                        item {
                            +str
                        }
                    }
                    for (str in arrayOf("1 + 1", "2 * 2", "3 / 3")) {
                        item {
                            !str
                        }
                    }
                }
            }
        }
        assertDocument(doc, "testItemizePossibleTags")
    }

    @Test
    fun testEnumeratePossibleTags() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                enumerate {
                    item {
                        +"first item"
                    }
                    for (str in arrayOf("1", "2", "3")) {
                        item {
                            +str
                        }
                    }
                    for (str in arrayOf("1 + 1", "2 * 2", "3 / 3")) {
                        item {
                            !str
                        }
                    }
                }
            }
        }
        assertDocument(doc, "testEnumeratePossibleTags")
    }

    @Test
    fun testAlignmentPossibleTags() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                alignment(Align.Left) {
                    itemize {
                    }
                }
                alignment(Align.Right) {
                    enumerate {
                    }
                }
                alignment(Align.Centre) {
                    customTag("myTag") {
                    }
                    +"test formula into alignment:"
                    !"5 + 5 = 10"
                }
            }
        }
        assertDocument(doc, "testAlignmentPossibleTags")
    }

    @Test
    fun testItemPossibleTags() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                itemize {
                    item {
                        itemize {
                        }
                        enumerate {
                        }
                        alignment(Align.Centre) {
                        }
                        customTag("myTag") {
                        }
                        +"test formula into item:"
                        !"2 * 4 + 2 = 10"
                    }
                }
            }
        }
        assertDocument(doc, "testItemPossibleTags")
    }

    @Test
    fun testCustomPossibleTags() {
        val doc = document {
            documentClass("class")
            usepackage("package")
            frame {
                customTag("myTag", "abc" to "123", "zxc" to "456") {
                    +"Custom tag don't have any tags"
                    +"But you can write math formula here:"
                    +"2 * 2 = 5 + 7 - 8"
                    +"5 < 10 - 6"
                    +"end"
                }
            }
        }
        assertDocument(doc, "testCustomPossibleTags")
    }

    @Test
    fun bigTest() {
        val doc = document {
            documentClass("class")
            usepackage("package1")
            usepackage("package2", "a", "bc", "efg", "hijk")
            frame("firstFrame", "x" to "a", "y" to "b") {
                +"first frame"
                itemize {
                    item {
                        !"1 + 1 = 2"
                        enumerate {
                            item {
                                +"inner item"
                            }
                            for (i in 1..2) {
                                item {
                                    +"item number $i"
                                }
                            }
                        }
                    }
                    item {
                        customTag("tagInItem", "one" to "1", "two" to "2") {
                            !"one + two = 3"
                        }
                    }
                }
            }
            frame("Second") {
                alignment(Align.Right) {
                    itemize {
                        item {
                        }
                    }
                }
                alignment(Align.Centre) {
                    !"5 + 7"
                }
            }
            frame {
                customTag("EmptyTag"){
                    !"0"
                }
            }
        }
        assertDocument(doc, "bigTest")
    }
}
