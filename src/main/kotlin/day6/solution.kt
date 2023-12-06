package day6


val digits = Regex("\\d+")
fun main() {
    val input = """
Time:      71530
Distance:  940200
    """.trimIndent()

    val (lineTimes, lineDistances) = input.lines()
    val times = lineTimes.extractNumbers()
    val distances = lineDistances.extractNumbers()
    val result = times.zip(distances).map { (time, distance) ->
        val minWinningSpeed = (1..<time).firstNotNullOf { speed ->
            val leftover = time - speed
            val covered = speed * leftover
            speed.takeIf { covered > distance }
        }
        (time - 1) - (minWinningSpeed - 1) * 2
    }.reduce { acc, elem -> acc * elem }
    println(result)
}

fun String.extractNumbers(): List<Long> {
    return digits.findAll(this).map { it.value.toLong() }.toList()
}
