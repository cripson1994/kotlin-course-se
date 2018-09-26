package ru.hse.spb


import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class ParseTest : FunLangTest() {
    fun testCode(source: String, result: String) {
        val parser = createParser(createFullLink(source))
        val byteArrayOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteArrayOutputStream)
        parser.file().accept(Printer(printStream))
        val actual = String(byteArrayOutputStream.toByteArray())
        val expected = File(createFullLink(result)).readText()
        assertEquals(expected, actual)
    }

    @Test
    fun testVariable() {
        testCode("test1.txt", "test1.txt")
    }

    @Test
    fun testAssignment() {
        testCode("test2.txt", "test2.txt")
    }

    @Test
    fun arithmTest() {
        testCode("test3.txt", "test3.txt")
    }

    @Test
    fun whileTest() {
        testCode("test4.txt", "test4.txt")
    }

    @Test
    fun ifTest() {
        testCode("test5.txt", "test5.txt")
    }

    @Test
    fun functionTest() {
        testCode("test6.txt", "test6.txt")
    }

    @Test
    fun scopeTest() {
        testCode("test7.txt", "test7.txt")
    }
}