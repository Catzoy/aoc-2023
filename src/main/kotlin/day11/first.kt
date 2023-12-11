package day11

import kotlin.math.absoluteValue

fun main() {
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
        .map { it.toMutableList() }
        .expandHorizontally()
        .expandVertically()

    val galaxies = input.withIndex().flatMap { (y, line) ->
        line.withIndex().filter { (_, c) -> c == '#' }.map { (x, _) -> x to y }
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

fun List<List<Char>>.expandHorizontally(): List<List<Char>> {
    var width = first().size
    val accum = map { it.toMutableList() }.toMutableList()
    var i = 0
    while (i < width) {
        val isEmpty = accum.fold(true) { acc, line -> acc && line[i] == '.' }
        if (isEmpty) {
            for (line in accum) {
                line.add(i, '.')
            }
            i++
            width++
        }
        i++
    }
    return accum
}

fun List<List<Char>>.expandVertically(): List<List<Char>> {
    var height = size
    val accum = map { it.toMutableList() }.toMutableList()
    var i = 0
    while (i < height) {
        val isEmpty = accum[i].fold(true) { acc, c -> acc && c == '.' }
        if (isEmpty) {
            accum.add(i, MutableList(accum[i].size) { '.' })
            i++
            height++
        }
        i++
    }
    return accum
}