package day4

sealed interface Points {
    fun score(): Points
    fun toInt(): Int

    data object Zero : Points {
        override fun score(): Points = One
        override fun toInt(): Int = 0
    }

    data object One : Points {
        override fun score(): Points = Many(points = 2)
        override fun toInt(): Int = 1
    }

    data class Many(val points: Int) : Points {
        override fun score(): Points = Many(points * 2)
        override fun toInt(): Int = points
    }
}

fun main() {
    val sections = Regex("Card\\s+\\d+:(?<winning>.+)\\|(?<yours>.+)")
    val input = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()
    val result = input.lineSequence()
        .map { line ->
            sections.matchEntire(line)!!.destructured
        }
        .map { (winning, yours) ->
            winning.extractNumbers().toSet() to yours.extractNumbers()
        }
        .map { (winning, yours) ->
            yours.fold<Int, Points>(Points.Zero) { acc, number ->
                if (winning.contains(number)) acc.score() else acc
            }
        }
        .sumOf { points ->
            points.toInt()
        }

    println(result)
}

fun String.extractNumbers() = split(" ").mapNotNull(String::toIntOrNull)