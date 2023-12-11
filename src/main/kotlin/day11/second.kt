package day11

import kotlin.math.absoluteValue

data class Point(val x: Long, val y: Long, val tile: Char)

fun main() {
    val coef = 1_000_000
    val input = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """.trimIndent()
        .lines()
        .withIndex()
        .map { (y, line) ->
            line.withIndex().map { (x, c) -> Point(x.toLong(), y.toLong(), c) }
        }
        .expandHorizontallyBy(coef)
        .expandVerticallyBy(coef)

    val galaxies = input.flatMap { line ->
        line.filter { point -> point.tile == '#' }.map { point -> point.x to point.y }
    }

    val distances = galaxies.asSequence()
        .withIndex()
        .flatMap { (i, galaxy) ->
            galaxies.asSequence().drop(i + 1).map { neighbour -> galaxy to neighbour }
        }
        .map { (a, b) ->
            val (x1, y1) = a
            val (x2, y2) = b
            val dx = x2 - x1
            val dy = y2 - y1
            dx.absoluteValue + dy.absoluteValue
        }
        .sum()

    println(distances)
}

fun List<List<Point>>.expandHorizontallyBy(coef: Int): List<List<Point>> {
    val width = first().size
    val accum = map { it.toMutableList() }.toMutableList()
    var i = 0
    while (i < width) {
        val isEmpty = accum.fold(true) { acc, line -> acc && line[i].tile == '.' }
        if (isEmpty) {
            for (line in accum) {
                for ((j, point) in line.withIndex().drop(i)) {
                    line[j] = point.copy(x = point.x + coef - 1)
                }
            }
        }
        i++
    }
    return accum
}

fun List<List<Point>>.expandVerticallyBy(coef: Int): List<List<Point>> {
    val height = size
    val accum = map { it.toMutableList() }.toMutableList()
    var i = 0
    while (i < height) {
        val isEmpty = accum[i].fold(true) { acc, point -> acc && point.tile == '.' }
        if (isEmpty) {
            for (j in i..<height) {
                for ((k, point) in accum[j].withIndex()) {
                    accum[j][k] = point.copy(y = point.y + coef - 1)
                }
            }
        }
        i++
    }
    return accum
}