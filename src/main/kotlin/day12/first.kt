package day12

fun main() {
    val input = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
""".trimIndent()

    val result = solution(input)
    println(result)
}

fun solution(input: String): Long {
    return input.lineSequence().withIndex().sumOf { (i, line) ->
        val (pattern, nums) = line.split(" ")
        val expecteds = nums.split(",").map { it.toInt() }
        val cache = mutableMapOf<String, MutableMap<String, Long>>()
        val patterns = List(5) { pattern }.joinToString(separator = "?")
        val extendedExpecteds = List(5) { expecteds }.flatten()
        count(patterns, extendedExpecteds, cache).also {
            println("$i => $it")
        }
    }
}

fun count(pattern: String, expecteds: List<Int>, cache: MutableMap<String, MutableMap<String, Long>>): Long {
    cache[pattern]
        ?.let { cached -> cached[expecteds.joinToString()] }
        ?.let { return it }
    if (expecteds.isEmpty()) return 0L

    val expected = expecteds.first()
    var sum = 0L
    var i = 0
    while (i < pattern.length) {
        if (pattern[i] == '?' || pattern[i] == '#') {
            val next = pattern.asSequence().drop(i).take(expected).toList()
            if (next.size == expected && next.all { it == '?' || it == '#' }) {
                if (next.all { it == '#' }) {
                    val further = i + next.size
                    if (further < pattern.length) {
                        val afterNext = pattern[further]
                        if (afterNext != '#') {
                            val leftover = expecteds.drop(1)
                            val splitIndex = further + 1
                            if (leftover.isEmpty()) {
                                if (splitIndex < pattern.length) {
                                    val afterSplit = pattern.substring(splitIndex)
                                    if (afterSplit.none { it == '#' }) {
                                        sum += 1
                                    }
                                } else {
                                    sum += 1
                                }
                            } else if (splitIndex < pattern.length) {
                                sum += count(pattern.substring(splitIndex), leftover, cache)
                            }
                        }
                    } else if (expecteds.size == 1) {
                        sum += 1
                    }
                } else {
                    val further = i + next.size
                    if (further < pattern.length) {
                        val afterNext = pattern[further]
                        if (afterNext != '#') {
                            val leftover = expecteds.drop(1)
                            val splitIndex = further + 1
                            if (leftover.isEmpty()) {
                                if (splitIndex < pattern.length) {
                                    val afterSplit = pattern.substring(splitIndex)
                                    if (afterSplit.none { it == '#' }) {
                                        sum += 1
                                    }
                                } else {
                                    sum += 1
                                }
                            } else if (splitIndex < pattern.length) {
                                sum += count(pattern.substring(splitIndex), leftover, cache)
                            }
                        }
                    } else if (expecteds.size == 1) {
                        sum += 1
                    }
                }
            }
        }
        if (pattern[i] == '#') {
            break
        }
        i++
    }
    return sum.also {
        val entry = cache.getOrPut(pattern) { mutableMapOf() }
        entry.putIfAbsent(expecteds.joinToString(), sum)
    }
}
