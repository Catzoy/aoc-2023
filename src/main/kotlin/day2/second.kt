package day2

fun main() {
    val red = Regex("(\\d+) red")
    val blue = Regex("(\\d+) blue")
    val green = Regex("(\\d+) green")
    val game = Regex("Game (\\d+)")

    val input = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent()

    val result = input.lineSequence()
        .map { line ->
            val gameId = game.find(line)!!.groupValues[1].toInt()
            val sets = line.substringAfter(":").split(";")
            val scores = sets.map { set ->
                val redScore = red.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                val blueScore = blue.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                val greenScore = green.find(set)?.groupValues?.get(1)?.toInt() ?: 0
                Triple(redScore, greenScore, blueScore)
            }
            gameId to scores
        }
        .map { (gameId, scores) ->
            val redMax = scores.maxOf { (red, _, _) -> red }
            val greenMax = scores.maxOf { (_, green, _) -> green }
            val blueMax = scores.maxOf { (_, _, blue) -> blue }
            gameId to Triple(redMax, greenMax, blueMax)
        }
        .sumOf { (_, colors) ->
            val (redMax, greenMax, blueMax) = colors
            redMax * greenMax * blueMax
        }

    print(result)
}