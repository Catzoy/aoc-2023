package day21

fun main() {
    val input = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
    """.trimIndent().lines()

    val startingPos = input.asSequence().mapIndexedNotNull { y, line ->
        line.indexOf('S').takeUnless { it < 0 }?.let { x -> y to x }
    }.first()

    var positions = setOf(startingPos)
    var steps = 64
    val whereabouts = listOf(
        -1 to 0,
        0 to -1,
        0 to 1,
        1 to 0,
    )
    while (steps > 0) {
        steps--
        positions = positions.flatMap { (y, x) ->
            whereabouts.mapNotNull { (dx, dy) ->
                val ny = y + dy
                val nx = x + dx
                val sign = input.getOrNull(ny)?.getOrNull(nx)
                if (sign == '.' || sign == 'S') {
                    ny to nx
                } else {
                    null
                }
            }
        }.toSet()
    }

    val m = input.map {
        it.toMutableList()
    }.toMutableList()
    for ((y, x) in positions) {
        m[y][x] = '0'
    }
    println(m.joinToString(separator = "\n") { it.joinToString(separator = "") })
    println(positions.count())
}