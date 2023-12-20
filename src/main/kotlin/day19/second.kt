package day19

fun main() {
    val step1 = Regex("""(\w+)\{(.+)}""")
    val step2 = Regex("""(\w)([<>])(\d+):(\w+)""")
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

    val paths = pathThrough("in", operations)
    val ranges = paths.map { path ->
        val ranges = mutableMapOf(
            "x" to 1..4000,
            "m" to 1..4000,
            "a" to 1..4000,
            "s" to 1..4000
        )
        path.asSequence().filterNotNull().fold(ranges) { m, step ->
            when (step) {
                is PathStep.Check -> {
                    val param = m.getValue(step.param)
                    val range = when (step.operand) {
                        ">" -> step.num + 1..param.last
                        "<" -> param.first..<step.num
                        else -> throw Exception("Cannot determine operation")
                    }
                    m[step.param] = range
                    m
                }

                is PathStep.UnCheck -> {
                    val param = m.getValue(step.param)
                    val range = when (step.operand) {
                        ">=" -> step.num..param.last
                        "<=" -> param.first..step.num
                        else -> throw Exception("Cannot determine operation")
                    }
                    m[step.param] = range
                    m
                }

                is PathStep.Skip -> m
            }
        }
    }
    val result = ranges.sumOf { params ->
        params.values.map { it.count.toLong() }.reduce { acc, l -> acc * l }
    }
    val a = 9223372036854775807
    val b = 167010937327821
    val c = 167409079868000
    println(result)
}

sealed interface PathStep {

    data class UnCheck(val param: String, val operand: String, val num: Int) : PathStep
    data class Check(val param: String, val operand: String, val num: Int, val next: String) : PathStep
    data class Skip(val next: String) : PathStep
}

fun pathThrough(
    name: String,
    operations: Map<String, List<Operation>>
): List<List<PathStep?>> {
    if (name == "A") {
        return listOf(listOf(null))
    }
    if (name == "R") {
        return emptyList()
    }
    val values = operations.getValue(name)
    return values.withIndex().flatMap { (i, v) ->
        val previous = values.take(i).negate()
        val paths = when (v) {
            is Operation.Proceed -> pathThrough(v.next, operations)
            is Operation.Check -> pathThrough(v.next, operations)
        }
        paths.map { s -> previous + listOf(v.toPathStep()) + s }
    }
}


fun Operation.toPathStep(): PathStep = when (this) {
    is Operation.Check -> PathStep.Check(param, operand, num, next)
    is Operation.Proceed -> PathStep.Skip(next)
}

fun List<Operation>.negate(): List<PathStep> {
    return map {
        when (it) {
            is Operation.Check -> {
                val operand = when (it.operand) {
                    ">" -> "<="
                    "<" -> ">="
                    else -> throw Exception("Cannot determine negative")
                }
                PathStep.UnCheck(it.param, operand, it.num)
            }

            is Operation.Proceed -> PathStep.Skip(it.next)
        }
    }
}

private val IntRange.count: Int
    get() = last - first + 1