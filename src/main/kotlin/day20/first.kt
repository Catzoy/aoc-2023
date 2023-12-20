package day20

const val broadcaster = "broadcaster"
const val button = "button"

enum class Pulse {
    LOW,
    HIGH
}

interface Module {
    fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>>

    fun connectConjunctionsIn(modules: Map<String, Module>)
}

class Button : Module {
    override fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
        return listOf(Triple(button, Pulse.LOW, broadcaster))
    }

    override fun connectConjunctionsIn(modules: Map<String, Module>) {
        // DO NOTHING
    }
}

class Broadcaster(
    private val outputs: List<String>,
) : Module {
    override fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
        return outputs.map { output -> Triple(broadcaster, pulse, output) }
    }

    override fun connectConjunctionsIn(modules: Map<String, Module>) {
        for (output in outputs) {
            val module = modules[output]
            if (module is Conjunction) {
                module.connect(broadcaster)
            }
        }
    }
}

class FlipFlop(
    private val name: String,
    private val outputs: List<String>,
) : Module {
    private enum class State {
        ON,
        OFF,
    }

    private var state = State.OFF
    override fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
        return when (pulse) {
            Pulse.LOW -> when (state) {
                State.ON -> {
                    state = State.OFF
                    outputs.map { output -> Triple(name, Pulse.LOW, output) }
                }

                State.OFF -> {
                    state = State.ON
                    outputs.map { output -> Triple(name, Pulse.HIGH, output) }
                }
            }

            Pulse.HIGH -> emptyList()
        }
    }

    override fun connectConjunctionsIn(modules: Map<String, Module>) {
        for (output in outputs) {
            val module = modules[output]
            if (module is Conjunction) {
                module.connect(name)
            }
        }
    }

    fun isOff(): Boolean {
        return state == State.OFF
    }
}

class Conjunction(
    private val name: String,
    private val outputs: List<String>,
) : Module {

    private val memory = mutableMapOf<String, Pulse>()

    fun connect(name: String) {
        memory[name] = Pulse.LOW
    }

    override fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
        memory[origin] = pulse
        return if (memory.values.all { memPulse -> memPulse == Pulse.HIGH }) {
            outputs.map { output -> Triple(name, Pulse.LOW, output) }
        } else {
            outputs.map { output -> Triple(name, Pulse.HIGH, output) }
        }
    }

    override fun connectConjunctionsIn(modules: Map<String, Module>) {
        for (output in outputs) {
            val module = modules[output]
            if (module is Conjunction) {
                module.connect(name)
            }
        }
    }
}

class Unknown : Module {
    override fun pulsate(origin: String, pulse: Pulse): List<Triple<String, Pulse, String>> {
        return emptyList()
    }

    override fun connectConjunctionsIn(modules: Map<String, Module>) {

    }
}

fun main() {
    val scheme = Regex("""(.+) -> (.+)""")
    val input = """
broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
    """.trimIndent()

    val modules = input.lineSequence()
        .map { line ->
            val (name, outputs) = scheme.find(line)!!.destructured
            name to outputs.split(", ")
        }
        .associate { (name, outputs) ->
            when {
                name == broadcaster -> name to Broadcaster(outputs)
                name.startsWith("%") -> name.drop(1).let { name ->
                    name to FlipFlop(name, outputs)
                }

                name.startsWith("&") -> name.drop(1).let { name ->
                    name to Conjunction(name, outputs)
                }

                else -> name to Unknown()
            }
        }
        .toMutableMap()
    modules[button] = Button()

    for ((_, module) in modules) {
        module.connectConjunctionsIn(modules)
    }

    val stack = mutableListOf<Triple<String, Pulse, String>>()
    var lowPulses = 0L
    var highPulses = 0L
    repeat(1000) {
        val start = modules[button]!!.pulsate("user", Pulse.LOW)
        stack.addAll(start)
        do {
            val (origin, pulse, name) = stack.removeFirst()
            when (pulse) {
                Pulse.LOW -> lowPulses++
                Pulse.HIGH -> highPulses++
            }
            val next = modules.getOrPut(name) { Unknown() }.pulsate(origin, pulse)
            stack.addAll(next)
        } while (stack.isNotEmpty())
    }
    println(lowPulses * highPulses)
}
