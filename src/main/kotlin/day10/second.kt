package day10


fun main() {
    val lines = """
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
    """.trimIndent().lines().map { line ->
        line.map<State> { char ->
            State.Unknown(Tile.entries.first { it.sign == char })
        }.toMutableList()
    }

    val starting: Point = lines.withIndex().mapNotNull { (y, line) ->
        line.withIndex().find { (_, c) -> c.tile == Tile.Start }?.index?.let { x -> x to y }
    }.single()
    lines[starting.second][starting.first] = State.LoopPart(Tile.Start)

    val (entrance1, entrance2) = findPaths(starting, lines)
    var prev1 = starting
    var pointer1 = entrance1

    var prev2 = starting
    var pointer2 = entrance2

    fun setLoopPart(point: Point) {
        lines[point.second][point.first] = State.LoopPart(lines[point.second][point.first].tile)
    }
    setLoopPart(pointer1)
    setLoopPart(pointer2)

    while (pointer1 != pointer2) {
        pointer1 = run {
            val (next1, next2) = findPaths(pointer1, lines)
            (if (next1 == prev1) next2 else next1).also {
                prev1 = pointer1
            }
        }
        setLoopPart(pointer1)

        pointer2 = run {
            val (next1, next2) = findPaths(pointer2, lines)
            (if (next1 == prev2) next2 else next1).also {
                prev2 = pointer2
            }
        }
        setLoopPart(pointer2)
    }

    var insides = 0
    for ((y, line) in lines.withIndex()) {
        var x = 0
        val inIndices = mutableListOf<Int>()
        while (x < line.size) {
            val state = line[x]
            if (state is State.LoopPart) {
                x = lines.handleHooks(y, x) ?: break

                var j = line.asSequence().withIndex().drop(x + 1)
                    .firstOrNull { (_, it) -> it is State.LoopPart }?.index
                    ?: break
                j = lines.handleHooks(y, j) ?: break

                val inner = line.asSequence().withIndex().drop(x + 1).take(j - x).filter { it.value !is State.LoopPart }
                    .toList()
                inIndices.addAll(inner.map { it.index })
                insides += inner.size

                x = j + 1
                continue
            }
            x++
        }
        println(
            "${
                line.withIndex().joinToString("") { (i, it) ->
                    when (it.tile) {
                        Tile.Ground -> if (i in inIndices) 'I' else 'O'
                        else -> it.tile.sign
                    }.toString()
                }
            } -> $insides"
        )
    }

    println(insides)
}


fun List<List<State>>.handleHooks(y: Int, ix: Int): Int? {
    val line = this[y]

    var x = ix
    while (x < line.size && line[x] !is State.LoopPart) {
        x++
    }
    if (x >= line.size) return null

    val state = line[x]
    if (state !is State.LoopPart) {
        return null
    }

    if (!state.tile.canGoTo(Direction.Right)) {
        return x
    }

    while (x < line.size && line[x].tile.canGoTo(Direction.Right)) {
        x++
    }

    val opposite = line[x]
    when (state.tile) {
        Tile.BendNorthEast -> {
            if (opposite.tile == Tile.BendNorthWest) {
                x++
                return handleHooks(y, x)
            }
        }

        Tile.BendSouthEast -> {
            if (opposite.tile == Tile.BendSouthWest) {
                x++
                return handleHooks(y, x)
            }
        }

        Tile.Start -> {
            val upper = getOrNull(y - 1)?.get(x)?.takeIf { it is State.LoopPart }
            if (upper != null
                && upper.tile.canGoTo(Direction.Down)
                && opposite.tile == Tile.BendSouthWest
            ) {
                x++
                return handleHooks(y, x)
            }

            val lower = getOrNull(y + 1)?.get(x)?.takeIf { it is State.LoopPart }
            if (lower != null
                && lower.tile.canGoTo(Direction.Up)
                && opposite.tile == Tile.BendNorthWest
            ) {
                x++
                return handleHooks(y, x)
            }
        }

        else -> {
        }
    }

    return x
}

sealed interface State {

    val tile: Tile

    data class Unknown(override val tile: Tile) : State
    data class LoopPart(override val tile: Tile) : State

}

private fun findPaths(point: Point, lines: List<List<State>>): Pair<Point, Point> {
    fun Point.isConnected(direction: Direction): Boolean {
        val (x, y) = this
        if (y < 0 || y >= lines.size) {
            return false
        }
        val line = lines[y]
        if (x < 0 || x >= line.size) {
            return false
        }
        val path = line[x]
        return path.tile.canBeAccessedFrom(direction)
    }

    val adjacent = mutableListOf<Point>()
    val (x, y) = point
    val current = lines[y][x]
    for ((next, direction) in listOf(
        ((x - 1) to y) to Direction.Left,
        ((x + 1) to y) to Direction.Right,
        (x to (y - 1)) to Direction.Up,
        (x to (y + 1)) to Direction.Down,
    )) {
        if (!current.tile.canGoTo(direction)) continue
        if (next.isConnected(direction)) adjacent.add(next)
    }

    val (first, second) = adjacent
    return first to second
}
