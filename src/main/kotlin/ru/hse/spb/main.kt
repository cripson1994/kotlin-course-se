package ru.hse.spb

enum class Gender {
    MASC, FEM
}

val maleEnds = listOf("lios$", "etr$", "initis$").map { x -> x.toRegex() }
val femaleEnds = listOf("liala$", "etra", "inites$").map { x -> x.toRegex() }

fun main(args: Array<String>) {
    val inp = readLine()
    print(solution(inp!!))
}

fun solution(seq: String): String {
    val words = seq.split(" ")
    return beautyOut(if (words.size == 1) isWordFromLanguage(words[0]) else isSequenceFromLanguage(words))
}

fun beautyOut(res: Boolean): String {
    return if (res) "YES" else "NO"
}

fun isSequenceFromLanguage(seq: List<String>): Boolean {
    val gender = identifyGender(seq[0]) ?: return false
    val ends = createEnds(gender)
    var ind = 0
    var nounsCount = 0
    for (end in ends) {
        while (ind < seq.size && end.find(seq[ind]) != null) {
            if (end == ends[1]) nounsCount++
            ind++
        }
    }
    return nounsCount == 1 && ind == seq.size
}

fun isWordFromLanguage(word: String): Boolean {
    return identifyGender(word) != null
}

fun identifyGender(word: String): Gender? {
    for (gender in Gender.values()) {
        if (createEnds(gender).any { it.find(word) != null }) {
            return gender
        }
    }
    return null
}

fun createEnds(gender: Gender): List<Regex> {
    return when (gender) {
        Gender.MASC -> maleEnds
        Gender.FEM -> femaleEnds
    }
}