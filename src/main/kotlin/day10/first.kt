package day10

typealias Point = Pair<Int, Int>

fun main() {
    val lines = """
        7-F7-
        .FJ|7
        SJLL7
        |F--J
        LJ.LJ
    """.trimIndent().lines().map { line ->
        line.map { char ->
            Tile.entries.first { it.sign == char }
        }
    }

    val starting = lines.withIndex().mapNotNull { (y, line) ->
        line.withIndex().find { (_, c) -> c == Tile.Start }?.index?.let { x -> x to y }
    }.single()
    val (entrance1, entrance2) = findPaths(starting, lines)
    var prev1 = starting
    var pointer1 = entrance1

    var prev2 = starting
    var pointer2 = entrance2

    var steps = 0
    while (pointer1 != pointer2) {
        pointer1 = run {
            val (next1, next2) = findPaths(pointer1, lines)
            (if (next1 == prev1) next2 else next1).also {
                prev1 = pointer1
            }
        }
        pointer2 = run {
            val (next1, next2) = findPaths(pointer2, lines)
            (if (next1 == prev2) next2 else next1).also {
                prev2 = pointer2
            }
        }
        steps++
    }

    println(steps + 1)
}

enum class Direction {
    Up,
    Down,
    Left,
    Right,
}

enum class Tile(val sign: Char) {
    Start('S') {
        override fun canGoTo(direction: Direction): Boolean {
            return true
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return true
        }
    },
    VerticalPipe('|') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Up || direction == Direction.Down
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return canGoTo(direction)
        }
    },
    HorizontalPipe('-') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Left || direction == Direction.Right
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return canGoTo(direction)
        }
    },
    BendNorthEast('L') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Up || direction == Direction.Right
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return direction == Direction.Down || direction == Direction.Left
        }
    },
    BendNorthWest('J') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Up || direction == Direction.Left
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return direction == Direction.Down || direction == Direction.Right
        }
    },
    BendSouthWest('7') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Down || direction == Direction.Left
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return direction == Direction.Up || direction == Direction.Right
        }
    },
    BendSouthEast('F') {
        override fun canGoTo(direction: Direction): Boolean {
            return direction == Direction.Down || direction == Direction.Right
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return direction == Direction.Up || direction == Direction.Left
        }
    },
    Ground('.') {
        override fun canGoTo(direction: Direction): Boolean {
            return false
        }

        override fun canBeAccessedFrom(direction: Direction): Boolean {
            return false
        }
    },
    ;

    abstract fun canGoTo(direction: Direction): Boolean
    abstract fun canBeAccessedFrom(direction: Direction): Boolean
}


fun findPaths(point: Point, lines: List<List<Tile>>): Pair<Point, Point> {
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
        return path.canBeAccessedFrom(direction)
    }

    val adjacent = mutableListOf<Point>()
    val (x, y) = point
    val tile = lines[y][x]
    for ((next, direction) in listOf(
        ((x - 1) to y) to Direction.Left,
        ((x + 1) to y) to Direction.Right,
        (x to (y - 1)) to Direction.Up,
        (x to (y + 1)) to Direction.Down,
    )) {
        if (!tile.canGoTo(direction)) continue
        if (next.isConnected(direction)) adjacent.add(next)
    }

    val (first, second) = adjacent
    return first to second
}
