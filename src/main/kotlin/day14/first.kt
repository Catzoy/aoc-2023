package day14

fun main() {
    val input = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
    """.trimIndent().lines().map { it.toMutableList() }

    for (i in input.indices) {
        var offset = 1
        var previous = i - 1
        while (previous >= 0) {
            val current = i - offset + 1
            for ((k, c) in input[current].withIndex()) {
                if (c == 'O' && input[previous][k] == '.') {
                    input[previous][k] = 'O'
                    input[current][k] = '.'
                }
            }
            previous--
            offset++
        }
    }


    println()
    for (line in input) {
        println(line.joinToString(""))
    }

    val result = input.withIndex().sumOf { (i, line) ->
        line.sumOf { c ->
            if (c == 'O') input.size - i else 0
        }
    }

    println(result)
}