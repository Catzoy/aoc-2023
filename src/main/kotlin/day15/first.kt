package day15

fun main() {
    val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
    val result = input.splitToSequence(',')
        .map { it.asSequence() }
        .map { hash(it) }
        .sum()
    println(result)
}

fun hash(str: Sequence<Char>): Int {
    return str.fold(0) { acc, c ->
        (acc + c.code) * 17 % 256
    }
}