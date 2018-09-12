package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun wordTest() {
        assertEquals(solution("petr"), "YES")
    }

    @Test
    fun notAcceptSeqTest() {
        assertEquals(solution("etis atis animatis etis atis amatis"), "NO")
    }

    @Test
    fun acceptSeqTest() {
        assertEquals(solution("nataliala kataliala vetra feinites"), "YES")
    }
}