package day13

fun main() {
    val input = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#""".trimIndent()

    val result = input.splitToSequence("\n\n")
        .map { pattern -> pattern.lines() }
        .map { lines ->
            Pair(
                lines.asSequence().map { it.findReflectionsStarts() },
                lines.transponed().asSequence().map { it.findReflectionsStarts() }
            )
        }
        .map { (horizontal, vertical) ->
            Pair(
                horizontal.reduce { acc, positions -> acc.intersect(positions) },
                vertical.reduce { acc, positions -> acc.intersect(positions) }
            )
        }
        .map { (h, v) -> Pair(h.firstOrNull(), v.firstOrNull()) }
        .sumOf { (h, v) ->
            if (h != null) h.first + h.second
            else if (v != null) (v.first + v.second) * 100
            else error("no reflection")
        }

    println(result)
}

private fun String.findReflectionsStarts(): Set<Pair<Int, Int>> {
    val beginnings = mutableSetOf<Pair<Int, Int>>()
    for (i in 0..<length - 1) {
        val current = substring(0, i + 1)
        val next = substring(i + 1).reversed()
        if (current.endsWith(next)) {
            val start = current.length - next.length
            beginnings.add(start to next.length)
        } else if (next.endsWith(current)) {
            beginnings.add(0 to current.length)
        }
    }
    return beginnings
}

private fun List<String>.transponed(): List<String> {
    val len = first().length
    val result = List(len) { i ->
        buildString {
            for (element in this@transponed) {
                append(element[i])
            }
        }
    }
    return result
}