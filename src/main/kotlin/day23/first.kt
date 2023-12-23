package day23

val whereabouts = listOf(
    Triple(-1, 0, '<'),
    Triple(0, -1, '^'),
    Triple(0, 1, 'v'),
    Triple(1, 0, '>'),
)

fun main() {
    val input = """
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
""".trimIndent()

    val m = input.lines().map { line -> line.toMutableList() }.toMutableList()
    val start = m.first().indexOf('.') to 0
    val (ex, ey) = m.last().indexOf('.') to m.lastIndex
    val positions = mutableListOf(m to Triple(start.first, start.second, 0L))
    val paths = mutableListOf<Long>()
    do {
        val (matrix, pos) = positions.removeFirst()
        val (sx, sy, walked) = pos
        if (sx == ex && sy == ey) {
            println()
            for (y in matrix.indices) {
                for (x in matrix[y].indices) {
                    print(matrix[y][x])
                }
                println()
            }

            paths.add(walked)
            continue
        }

        val sign = matrix[sy][sx]
        matrix[sy][sx] = 'O'

        val sloped = whereabouts.firstOrNull { (_, _, slope) -> slope == sign }
            ?.let { (dx, dy, _) -> sx + dx to sy + dy }
        if (sloped != null) {
            positions.add(matrix.copy2() to Triple(sloped.first, sloped.second, walked + 1))
            continue
        }

        for ((dx, dy, slope) in whereabouts) {
            val (x, y) = sx + dx to sy + dy
            when (matrix.getOrNull(y)?.getOrNull(x)) {
                '.' -> {
                    positions.add(matrix.copy2() to Triple(x, y, walked + 1))
                }

                slope -> {
                    positions.add(matrix.copy2() to Triple(x, y, walked + 1))
                }

                else -> {
                    continue
                }
            }
        }
    } while (positions.isNotEmpty())

    println(paths.max())
}

fun <T> MutableList<T>.copy1() = MutableList(size) { get(it) }
fun <T> MutableList<MutableList<T>>.copy2() = MutableList(size) { get(it).copy1() }