package ru.hse.spb

import junit.framework.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.math.E

class ExecuteTest : FunLangTest() {
    fun testCode(source: String, result: String) {
        val parser = createParser(createFullLink(source))
        val byteArrayOutputStream = ByteArrayOutputStream()
        val printStream = PrintStream(byteArrayOutputStream)
        parser.file().accept(Executor(printStream))
        val actual = String(byteArrayOutputStream.toByteArray())
        val expected = File(createFullLink(result)).readText()
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testVariable() {
        testCode("test1.txt", "result1.txt")
    }

    @Test
    fun testAssignment() {
        testCode("test2.txt", "result2.txt")
    }

    @Test
    fun arithmTest() {
        testCode("test3.txt", "result3.txt")
    }

    @Test
    fun whileTest() {
        testCode("test4.txt", "result4.txt")
    }

    @Test
    fun ifTest() {
        testCode("test5.txt", "result5.txt")
    }

    @Test
    fun functionTest() {
        testCode("test6.txt", "result6.txt")
    }

    @Test
    fun scopeTest() {
        testCode("test7.txt", "result7.txt")
    }
}