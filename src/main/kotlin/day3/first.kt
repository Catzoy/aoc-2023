package day3

import kotlin.math.max
import kotlin.math.min

val digits = Regex("\\d+")
val specialSymbol = Regex("[^\\d.]")
fun main() {
    val input = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...${'$'}.*....
.664.598..
    """.trimIndent()

    val lines = buildList {
        val lines = input.lines()

        add(Triple(null, lines[0], lines[1]))
        for (i in 1..<lines.size - 1) {
            add(Triple(lines[i - 1], lines[i], lines[i + 1]))
        }
        add(Triple(lines[lines.size - 2], lines[lines.size - 1], null))
    }

    val result = lines.asSequence().map { (previous, current, next) ->
        val digitsInLine = digits.findAll(current).map { it.range to it.value.toInt() }.toList()
        val partNumbersInPrevious = previous?.filterPartNumbers(digitsInLine) ?: emptyList()
        val partNumbersInNext = next?.filterPartNumbers(digitsInLine) ?: emptyList()
        val partNumbersInCurrent = current.filterPartNumbers(digitsInLine)
        buildList {
            addAll(partNumbersInPrevious)
            addAll(partNumbersInNext)
            addAll(partNumbersInCurrent)
        }
    }.sumOf {
        it.sum()
    }

    println(result)
}

fun String.filterPartNumbers(digitsInLine: List<Pair<IntRange, Int>>): List<Int> {
    return digitsInLine.map { (range, value) ->
        val adjacentRange = max(0, range.first - 1)..min(range.last + 1, length - 1)
        adjacentRange to value
    }.filter { (range, _) ->
        specialSymbol.containsMatchIn(substring(range))
    }.map { (_, value) ->
        value
    }
}