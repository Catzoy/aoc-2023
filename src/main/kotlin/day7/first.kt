package day7


const val ordering1 = "AKQJT98765432"

fun String.compareByOrdering1To(other: String): Int {
    return this.zip(other)
        .first { (a, b) -> a != b }
        .let { (a, b) -> ordering1.indexOf(b).compareTo(ordering1.indexOf(a)) }
}

sealed interface Type1 : Comparable<Type1> {
    companion object {
        fun parse(value: String): Type1 {
            val counts = value.groupingBy { it }.eachCount()
            if (FiveKind.satisfies(counts)) return FiveKind(value)
            if (FourKind.satisfies(counts)) return FourKind(value)
            if (FullHouse.satisfies(counts)) return FullHouse(value)
            if (ThreeKind.satisfies(counts)) return ThreeKind(value)
            if (TwoPair.satisfies(counts)) return TwoPair(value)
            if (OnePair.satisfies(counts)) return OnePair(value)
            return HighCard(value)
        }
    }

    val value: String

    override fun compareTo(other: Type1): Int {
        return value.compareByOrdering1To(other.value)
    }

    data class FiveKind(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts.values.singleOrNull { it == 5 } != null
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is FiveKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class FourKind(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts.values.singleOrNull { it == 4 } != null
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is FiveKind -> -1
                is FourKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class FullHouse(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                if (counts.size != 2) return false
                val (a, b) = counts.values.toList()
                return (a == 2 && b == 3) || (a == 3 && b == 2)
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is FiveKind,
                is FourKind -> -1

                is FullHouse -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class ThreeKind(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts.values.singleOrNull { it == 3 } != null
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is FiveKind,
                is FourKind,
                is FullHouse -> -1

                is ThreeKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class TwoPair(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts.values.count { it == 2 } == 2
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is OnePair,
                is HighCard -> 1

                is TwoPair -> super.compareTo(other)
                else -> -1
            }
        }
    }

    data class OnePair(override val value: String) : Type1 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts.values.singleOrNull { it == 2 } != null
            }
        }

        override fun compareTo(other: Type1): Int {
            return when (other) {
                is OnePair -> super.compareTo(other)
                is HighCard -> 1
                else -> -1
            }
        }
    }

    data class HighCard(override val value: String) : Type1 {
        override fun compareTo(other: Type1): Int {
            return when (other) {
                is HighCard -> super.compareTo(other)
                else -> -1
            }
        }
    }
}

fun main() {
    val input = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
""".trimIndent()

    val result = input.lineSequence()
        .map { line ->
            val (cards, value) = line.split(" ")
            val type = Type1.parse(cards)
            type to value.toInt()
        }
        .sortedWith { pair1, pair2 ->
            pair1.first.compareTo(pair2.first)
        }
        .withIndex()
        .toList()
        .also {
            println(it.joinToString("\n") { (index, pair) ->
                val (type, value) = pair
                "${index + 1}: $type $value"
            })
        }
        .sumOf { (index, pair) ->
            val (_, value) = pair
            value * (index + 1)
        }
    println(result)
}