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
    val previous = mutableSetOf(
        input.joined()
    )
    var k = -1
    for (i in 0..<1_000_000_000) {
        north(input)
        west(input)
        south(input)
        east(input)
        val current = input.joined()
        if (!previous.add(current)) {
            k = previous.indexOf(current)
            break
        }
    }

    val cycling = previous.size - k
    val iterations = (1_000_000_000 - k) % cycling
    val combination = previous.toList()[k + iterations]

    val result = combination.lines().withIndex().sumOf { (i, line) ->
        line.sumOf { c ->
            if (c == 'O') input.size - i else 0
        }
    }

    println(result)
}

fun List<List<Char>>.joined(): String {
    return this.joinToString(separator = "\n") { line ->
        line.joinToString("")
    }

}

fun north(input: List<MutableList<Char>>) {
    for (i in input.indices) {
        var previous = i - 1
        while (previous >= 0) {
            val offset = i - previous
            val current = i - offset + 1
            for ((k, c) in input[current].withIndex()) {
                if (c == 'O' && input[previous][k] == '.') {
                    input[previous][k] = 'O'
                    input[current][k] = '.'
                }
            }
            previous--
        }
    }
}

fun south(input: List<MutableList<Char>>) {
    for (i in input.indices.reversed()) {
        var next = i + 1
        while (next < input.size) {
            val offset = next - i
            val current = i + offset - 1
            for ((k, c) in input[current].withIndex()) {
                if (c == 'O' && input[next][k] == '.') {
                    input[next][k] = 'O'
                    input[current][k] = '.'
                }
            }
            next++
        }
    }
}

fun west(input: List<MutableList<Char>>) {
    for (line in input) {
        for ((i, c) in line.withIndex()) {
            var previous = i - 1
            while (previous >= 0) {
                val offset = i - previous
                val current = i - offset + 1
                if (c == 'O') {
                    if (line[previous] == '.') {
                        line[previous] = 'O'
                        line[current] = '.'
                    } else {
                        break
                    }
                }
                previous--
            }
        }
    }
}

fun east(input: List<MutableList<Char>>) {
    for (line in input) {
        for (i in line.indices.reversed()) {
            var next = i + 1
            while (next < line.size) {
                val offset = next - i
                val current = i + offset - 1
                if (line[current] == 'O') {
                    if (line[next] == '.') {
                        line[next] = 'O'
                        line[current] = '.'
                    } else {
                        break
                    }
                }
                next++
            }
        }
    }
}

//.....#....
//....#...O#
//.....##...
//..O#......
//.....OOO#.
//.O#...O#.#
//....O#...O
//.......OOO
//#...O###.O
//#.OOO#...O