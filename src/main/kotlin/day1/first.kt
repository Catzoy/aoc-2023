package day1
fun main() {
    val input = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent()
    val numbers = input.lineSequence()
        .map { lines -> lines.filter { char -> char.isDigit() } }
        .map { str -> "${str.first()}${str.last()}" }
        .map { num -> num.toInt() }
        .sum()

    println(numbers)
}