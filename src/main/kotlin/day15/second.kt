package day15

typealias Lens = Pair<String, Int>

sealed interface Operation {
    val label: String

    data class Push(val lens: Lens) : Operation {
        override val label: String
            get() = lens.first
    }

    data class Pop(override val label: String) : Operation
}

fun main() {
    val boxes = List(256) { mutableListOf<Lens>() }
    val steps = Regex("""([a-z]+)(?:=(\d)|-)""")
    val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
    val result = steps.findAll(input)
        .map { matches ->
            val (label, digits) = matches.destructured
            digits.toIntOrNull()?.let { Operation.Push(label to it) } ?: Operation.Pop(label)
        }
        .fold(boxes) { b, operation ->
            val i = hash(operation.label.asSequence())
            val box = b[i]
            when (operation) {
                is Operation.Pop -> {
                    box.removeAll { (label, _) -> label == operation.label }
                }
                is Operation.Push -> {
                    val j = box.indexOfFirst { (label, _) -> label == operation.label }
                    if (j < 0) {
                        box.add(operation.lens)
                    } else {
                        box[j] = operation.lens
                    }
                }
            }
            b
        }
        .asSequence()
        .withIndex()
        .flatMap { (i, lenses) ->
            lenses.withIndex().asSequence().map { (j, lens) -> Triple(i, j, lens.second) }
        }
        .onEach { println(it) }
        .sumOf { (boxNum, lensPos, length) ->
            ((boxNum + 1) * (lensPos + 1) * length).also { println(it) }
        }
    println(result)
}
