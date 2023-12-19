package day19

sealed interface Operation {
    data class Check(
        val param: String,
        val operand: String,
        val num: Int,
        val next: String,
    ) : Operation

    data class Proceed(val next: String) : Operation
}

fun main() {
    val step1 = Regex("""(\w+)\{(.+)}""")
    val step2 = Regex("""(\w)([<>])(\d+):(\w+)""")
    val step3 = Regex("""(\w)=(\d+)""")
    val input = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()
    val lines = input.lineSequence().iterator()
    val operations = buildMap {
        do {
            val line = lines.next()
            if (line.isEmpty()) break
            val (name, rules) = step1.find(line)!!.destructured
            val operations = rules.split(',')
                .map { it to step2.find(it)?.groupValues }
                .map { (str, values) ->
                    if (values == null) {
                        Operation.Proceed(str)
                    } else {
                        val (_, param, operand, num, next) = values
                        Operation.Check(param, operand, num.toInt(), next)
                    }
                }
            put(name, operations)
        } while (lines.hasNext())
    }
    val parts = buildList {
        do {
            val line = lines.next()
            val params = step3.findAll(line).associate { result ->
                val (name, value) = result.destructured
                name to value.toInt()
            }
            add(params)
        } while (lines.hasNext())
    }

    val processed = parts.groupBy { part ->
        var result = "in"
        while (result != "A" && result != "R") {
            result = operations.getValue(result).process(part)
        }
        result
    }

    val accepted = processed.getValue("A")
    val result = accepted.sumOf { it.values.sum().toLong() }
    println(result)
}

fun List<Operation>.process(part: Map<String, Int>): String {
    forEach { operation ->
        when (operation) {
            is Operation.Proceed -> {
                return operation.next
            }

            is Operation.Check -> {
                val param = part.getValue(operation.param)
                val didSatisfy = when (operation.operand) {
                    ">" -> param > operation.num
                    "<" -> param < operation.num
                    else -> throw Exception("Unknown operand ${operation.operand}")
                }
                if (didSatisfy) return operation.next
            }
        }
    }
    throw Exception("Cannot process part by this workflow")
}