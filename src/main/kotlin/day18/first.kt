package day18

import day16.Direction

fun main() {
    val input = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()
    val (matrix, _) = input.lineSequence()
        .map { line ->
            val (d, n) = line.split(" ")
            val direction = when (d.single()) {
                'R' -> Direction.RIGHT
                'U' -> Direction.UP
                'L' -> Direction.LEFT
                'D' -> Direction.DOWN
                else -> throw IllegalArgumentException("Unknown direction $d")
            }
            (direction to n.toInt())
        }
        .fold(mutableListOf(mutableListOf('.')) to (0 to 0)) { params, operation ->
            val (matrix, position) = params
            val (direction, num) = operation
            val (x, y) = position
            val nextPosition = when (direction) {
                Direction.UP -> {
                    var upY = y
                    repeat(num) { i ->
                        upY -= 1
                        if (upY < 0) {
                            matrix.add(0, MutableList(matrix.first().size) { '.' })
                            upY = 0
                        }
                        matrix[upY][x] = '#'
                    }
                    x to upY
                }

                Direction.DOWN -> {
                    var downY = y
                    repeat(num) {
                        downY += 1
                        if (downY == matrix.size) {
                            matrix.add(MutableList(matrix.first().size) { '.' })
                        }
                        matrix[downY][x] = '#'
                    }
                    x to downY
                }

                Direction.LEFT -> {
                    var leftX = x
                    repeat(num) {
                        leftX -= 1
                        if (leftX < 0) {
                            for (line in matrix) {
                                line.add(0, '.')
                            }
                            leftX = 0
                        }
                        matrix[y][leftX] = '#'
                    }
                    leftX to y
                }

                Direction.RIGHT -> {
                    var rightX = x
                    repeat(num) {
                        rightX += 1
                        if (rightX == matrix.first().size) {
                            for (line in matrix) {
                                line.add('.')
                            }
                        }
                        matrix[y][rightX] = '#'
                    }
                    rightX to y
                }
            }
            matrix to nextPosition
        }

    val ranges = matrix.withIndex().map { (i, line) ->
        val prev = matrix.getOrNull(i - 1)
        val next = matrix.getOrNull(i + 1)
        val digged = line.asSequence().withIndex().filter { it.value == '#' }.map { it.index }.toMutableList()
        buildList {
            while (digged.isNotEmpty()) {
                val start = digged.findOpening(prev, next) ?: continue
                var end: Int?
                do {
                    end = digged.findOpening(prev, next)
                    if (end != null) {
                        add(start + 1..<end)
                        break
                    }
                } while (digged.isNotEmpty())
            }
        }
    }

    for ((fillings, line) in ranges.zip(matrix)) {
        for (range in fillings) {
            for (i in range) {
                line[i] = '#'
            }
        }
    }

    val result = matrix.sumOf { line -> line.sumOf { if (it == '#') 1L else 0L } }
    println(result)
}

fun MutableList<Int>.findOpening(prev: List<Char>?, next: List<Char>?): Int? {
    val start = removeFirst()
    var end = start
    while (isNotEmpty() && first() - 1 == end) {
        end = removeFirst()
    }
    return when {
        start == end -> end
        prev != null
                && prev[start] == '#'
                && prev[end] == '#' -> null

        next != null
                && next[start] == '#'
                && next[end] == '#' -> null

        else -> end
    }
}