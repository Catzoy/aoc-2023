package day7


const val ordering2 = "AKQT98765432J"

private fun String.compareByOrdering2To(other: String): Int {
    return this.zip(other)
        .first { (a, b) -> a != b }
        .let { (a, b) -> ordering2.indexOf(b).compareTo(ordering2.indexOf(a)) }
}

val Map<Char, Int>.exceptJokers: Map<Char, Int>
    get() = filterKeys { it != 'J' }

sealed interface Type2 : Comparable<Type2> {
    companion object {
        fun parse(value: String): Type2 {
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

    override fun compareTo(other: Type2): Int {
        return value.compareByOrdering2To(other.value)
    }

    data class FiveKind(override val value: String) : Type2 {

        companion object {

            private fun isMadeWithJokers(counts: Map<Char, Int>): Boolean {
                val jCount = counts['J'] ?: return false
                if (jCount == 5) return false
                if (counts.size != 2) return false
                return counts.exceptJokers.values.single() == (5 - jCount)
            }

            fun satisfies(counts: Map<Char, Int>): Boolean {
                return isMadeWithJokers(counts) || Type1.FiveKind.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is FiveKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class FourKind(override val value: String) : Type2 {

        companion object {

            private fun isMadeWithJokers(counts: Map<Char, Int>): Boolean {
                val jCount = counts['J'] ?: return false
                return counts.exceptJokers.values.any { it == (4 - jCount) }
            }

            fun satisfies(counts: Map<Char, Int>): Boolean {
                return isMadeWithJokers(counts) || Type1.FourKind.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is FiveKind -> -1
                is FourKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class FullHouse(override val value: String) : Type2 {

        companion object {

            private fun isMadeWithJokers(counts: Map<Char, Int>): Boolean {
                if (counts['J'] != 1) return false
                if (counts.size != 3) return false
                val (a, b) = counts.exceptJokers.values.toList()
                return a == 2 && b == 2
            }

            fun satisfies(counts: Map<Char, Int>): Boolean {
                return isMadeWithJokers(counts) || Type1.FullHouse.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is FiveKind,
                is FourKind -> -1

                is FullHouse -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class ThreeKind(override val value: String) : Type2 {

        companion object {

            private fun isMadeWithJokers(counts: Map<Char, Int>): Boolean {
                val jCount = counts['J'] ?: return false
                return counts.exceptJokers.values.any { it == (3 - jCount) }
            }

            fun satisfies(counts: Map<Char, Int>): Boolean {
                return isMadeWithJokers(counts) || Type1.ThreeKind.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is FiveKind,
                is FourKind,
                is FullHouse -> -1

                is ThreeKind -> super.compareTo(other)
                else -> 1
            }
        }
    }

    data class TwoPair(override val value: String) : Type2 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                // No way to incorporate Jokers here
                return Type1.TwoPair.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is OnePair,
                is HighCard -> 1

                is TwoPair -> super.compareTo(other)
                else -> -1
            }
        }
    }

    data class OnePair(override val value: String) : Type2 {

        companion object {
            fun satisfies(counts: Map<Char, Int>): Boolean {
                return counts['J'] == 1 || Type1.OnePair.satisfies(counts)
            }
        }

        override fun compareTo(other: Type2): Int {
            return when (other) {
                is OnePair -> super.compareTo(other)
                is HighCard -> 1
                else -> -1
            }
        }
    }

    data class HighCard(override val value: String) : Type2 {
        override fun compareTo(other: Type2): Int {
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
            val type = Type2.parse(cards)
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