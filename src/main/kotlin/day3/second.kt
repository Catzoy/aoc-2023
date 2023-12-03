package day3

import kotlin.math.max
import kotlin.math.min


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
        val indicesOfGears = current.withIndex().filter { (_, c) -> c == '*' }.map { it.index }
        val partNumbersInPrevious = previous?.filterGears(indicesOfGears) ?: emptyList()
        val partNumbersInNext = next?.filterGears(indicesOfGears) ?: emptyList()
        val partNumbersInCurrent = current.filterGears(indicesOfGears)
        buildList {
            addAll(partNumbersInPrevious)
            addAll(partNumbersInNext)
            addAll(partNumbersInCurrent)
        }
    }.map { gearsWithPartNumbers ->
        buildMap<Int, List<Int>> {
            for ((gearId, partNumbers) in gearsWithPartNumbers) {
                merge(gearId, partNumbers) { a, b -> a + b }
            }
        }
    }.map { gearsWithPartNumbers ->
        gearsWithPartNumbers.values.filter { it.size == 2 }
    }.flatten().sumOf {
        it.first() * it.last()
    }

    println(result)
}

fun String.filterGears(indicesOfGears: List<Int>): List<Pair<Int, List<Int>>> {
    return indicesOfGears.map { i ->
        val gearRange = max(0, i - 1)..min(i + 1, length - 1)
        val digitsInLine = digits.findAll(this).map { it.range to it.value.toInt() }.toList()
        i to digitsInLine.filter { (range, _) ->
            gearRange.any { it in range }
        }.map { (_, value) -> value }
    }
}