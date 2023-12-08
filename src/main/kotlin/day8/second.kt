package day8

fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun main() {
    val rule = Regex("""(\w+) = \((\w+), (\w+)\)""")
    val input ="""
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)""".trimIndent()

    val linesIterator = input.lineSequence().iterator()
    val instructions = linesIterator.next()
    linesIterator.next() // skip empty

    val rules = linesIterator.asSequence().fold(mutableMapOf<String, Pair<String, String>>()) { acc, line ->
        acc.apply {
            val (key, left, right) = rule.matchEntire(line)!!.destructured
            put(key, left to right)
        }
    }

    val startingPositions = rules.keys.filter { it.endsWith("A") }

    val instructionsStream: Sequence<Char> = sequence {
        var index = 0
        while (true) {
            yield(instructions[index])
            index++
            if (index == instructions.length) {
                index = 0
            }
        }
    }

    val turns = startingPositions.map { start ->
        instructionsStream.runningFold(start) { position, instruction ->
            val (left, right) = rules[position]!!
            when (instruction) {
                'R' -> right
                'L' -> left
                else -> throw IllegalArgumentException("Unknown instruction $instruction")
            }
        }.withIndex().first { (_, position) ->
            position.endsWith("Z")
        }
    }.map { (index, _) -> index.toLong() }.reduce { acc, i -> lcm(acc, i) }
    println(turns)
}