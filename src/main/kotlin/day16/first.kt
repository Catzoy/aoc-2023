package day16

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

inline fun runIf(condition: Boolean, crossinline action: () -> List<Beam>): List<Beam> {
    return if (condition) action() else emptyList()
}

data class Beam(
    val x: Int,
    val y: Int,
    val direction: Direction,
) {

    private fun goUp(matrix: List<MutableList<Cell>>): List<Beam> {
        val upY = (y - 1).takeUnless { it < 0 } ?: return emptyList()
        return listOf(Beam(x, upY, Direction.UP))
    }

    private fun goDown(matrix: List<MutableList<Cell>>): List<Beam> {
        val downY = (y + 1).takeIf { it < matrix.size } ?: return emptyList()
        return listOf(Beam(x, downY, Direction.DOWN))
    }

    private fun goRight(matrix: List<MutableList<Cell>>): List<Beam> {
        val rightX = (x + 1).takeIf { it < matrix[y].size } ?: return emptyList()
        return listOf(Beam(rightX, y, Direction.RIGHT))
    }

    private fun goLeft(matrix: List<MutableList<Cell>>): List<Beam> {
        val rightX = (x - 1).takeUnless { it < 0 } ?: return emptyList()
        return listOf(Beam(rightX, y, Direction.LEFT))
    }

    fun proceed(matrix: List<MutableList<Cell>>): List<Beam> {
        val cell = matrix[y][x]
        val didSplitPreviously = when (cell) {
            is Cell.Empty -> false
            is Cell.Energized -> cell.didSplit
        }
        val beams = when (direction) {
            Direction.UP -> when (cell.char) {
                '\\' -> goLeft(matrix)
                '/' -> goRight(matrix)
                '-' -> runIf(!didSplitPreviously) { goLeft(matrix) + goRight(matrix) }
                else -> goUp(matrix)
            }

            Direction.DOWN -> when (cell.char) {
                '\\' -> goRight(matrix)
                '/' -> goLeft(matrix)
                '-' -> runIf(!didSplitPreviously) { goLeft(matrix) + goRight(matrix) }
                else -> goDown(matrix)
            }

            Direction.LEFT -> when (cell.char) {
                '\\' -> goUp(matrix)
                '/' -> goDown(matrix)
                '|' -> runIf(!didSplitPreviously) { goUp(matrix) + goDown(matrix) }
                else -> goLeft(matrix)
            }

            Direction.RIGHT -> when (cell.char) {
                '\\' -> goDown(matrix)
                '/' -> goUp(matrix)
                '|' -> runIf(!didSplitPreviously) { goUp(matrix) + goDown(matrix) }
                else -> goRight(matrix)
            }
        }
        matrix[y][x] = matrix[y][x].energize(didSplit = beams.size == 2)
        return beams
    }
}

sealed interface Cell {
    val char: Char
    fun energize(didSplit: Boolean): Energized
    data class Empty(override val char: Char) : Cell {
        override fun energize(didSplit: Boolean): Energized {
            return Energized(char, didSplit)
        }
    }

    data class Energized(
        override val char: Char,
        val didSplit: Boolean
    ) : Cell {
        override fun energize(didSplit: Boolean): Energized {
            return when {
                this.didSplit == didSplit -> this
                !didSplit -> this
                else -> copy(didSplit = true)
            }
        }
    }
}

fun main() {
    val input = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()
    val matrix = input.split("\n")
        .map { line -> MutableList<Cell>(line.length) { i -> Cell.Empty(line[i]) } }
    var beams = listOf(Beam(0, 0, Direction.RIGHT))
    do {
        beams = beams.flatMap { beam -> beam.proceed(matrix) }
    } while (beams.isNotEmpty())

    val result = matrix.sumOf { line ->
        line.sumOf {
            when (it) {
                is Cell.Empty -> 0L
                is Cell.Energized -> 1L
            }
        }
    }
    println(result)
}