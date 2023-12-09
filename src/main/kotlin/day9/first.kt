package day9

fun main() {
    val input = """
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
    """.trimIndent()

    val result = input.lineSequence()
        .map { line -> line.split(" ").map { it.toInt() } }
        .map { numbers ->
            mutableListOf(numbers.toMutableList()).apply {
                do {
                    val next = last().zipWithNext { a, b -> b - a }.toMutableList()
                    add(next)
                } while (!last().all { it == 0 })
            }
        }
        .map { sequences ->
            sequences.apply {
                last().add(0)
                for ((prev, next) in asReversed().zipWithNext()) {
                    next.add(next.last() + prev.last())
                }
            }
        }.sumOf {
            it.first().last()
        }

    println(result)
}