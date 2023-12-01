package day1

val numbers = setOf(
    "one" to '1',
    "two" to '2',
    "three" to '3',
    "four" to '4',
    "five" to '5',
    "six" to '6',
    "seven" to '7',
    "eight" to '8',
    "nine" to '9'
).map { (spelled, digit) ->
    Regex.fromLiteral(spelled) to digit
}

fun main() {
    val input = """
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
    """.trimIndent()
    val numbers = input.lineSequence()
        .map { line -> line.allDigits.sortedBy { it.index } }
        .map { str -> "${str.first().value}${str.last().value}" }
        .map { num -> num.toInt() }
        .sum()

    println(numbers)
}

val String.allDigits
    get() = digitsFromWords() + digitsFromChars()

fun String.digitsFromWords(): List<IndexedValue<Char>> {
    return numbers.flatMap { (spelled, num) ->
        spelled.findAll(this).map {
            IndexedValue(it.range.first, num)
        }
    }
}

fun String.digitsFromChars(): List<IndexedValue<Char>> {
    return withIndex().filter { (_, char) -> char.isDigit() }
}