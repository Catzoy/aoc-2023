package day8

fun main() {
    val rule = Regex("""(\w+) = \((\w+), (\w+)\)""")
    val input = """
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    val linesIterator = input.lineSequence().iterator()
    val instructions = linesIterator.next()
    linesIterator.next() // skip empty

    val rules = linesIterator.asSequence().fold(mutableMapOf<String, Pair<String, String>>()) { acc, line ->
        acc.apply {
            val (key, left, right) = rule.matchEntire(line)!!.destructured
            put(key, left to right)
        }
    }

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

    val target = "ZZZ"
    val (i, _) = instructionsStream.runningFold("AAA") { position, instruction ->
        val (left, right) = rules[position]!!
        when (instruction) {
            'R' -> right
            'L' -> left
            else -> throw IllegalArgumentException("Unknown instruction $instruction")
        }
    }.withIndex().first { (_, position) -> position == target }

    println(i)
}