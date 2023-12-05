package day5

fun main() {
    val input = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
""".trimIndent()

    val iterator = input.lineSequence().iterator()
    val seeds = iterator.next().extractNumbersFromPairs()
    iterator.next() // skip empty line

    val seedToSoilMap = iterator.readNumbers().foldRespectiveMap()
    val soilToFertilizerMap = iterator.readNumbers().foldRespectiveMap()
    val fertilizerToWaterMap = iterator.readNumbers().foldRespectiveMap()
    val waterToLightMap = iterator.readNumbers().foldRespectiveMap()
    val lightToTemperatureMap = iterator.readNumbers().foldRespectiveMap()
    val temperatureToHumidityMap = iterator.readNumbers().foldRespectiveMap()
    val humidityToLocationMap = iterator.readNumbers().foldRespectiveMap()

    val locations = seeds.map { seed ->
        val soils = seedToSoilMap.getBy(seed)
        val fertilizer = soilToFertilizerMap.getBy(soils)
        val water = fertilizerToWaterMap.getBy(fertilizer)
        val light = waterToLightMap.getBy(water)
        val temperature = lightToTemperatureMap.getBy(light)
        val humidity = temperatureToHumidityMap.getBy(temperature)
        humidityToLocationMap.getBy(humidity)
    }.flatten()

    println(locations.minByOrNull { it.first }!!.first)
}

fun String.extractNumbersFromPairs(): List<LongRange> {
    val numbers = extractNumbers()
    return (numbers.indices step 2).fold(mutableListOf()) { acc, i ->
        val elem = numbers[i]
        val range = elem..<(elem + numbers[i + 1])
        acc.add(range)
        acc
    }
}

private fun List<List<Long>>.foldRespectiveMap(): Map<LongRange, LongRange> {
    val map = fold(mutableMapOf<LongRange, LongRange>()) { acc, (first, second, len) ->
        acc[second..<(second + len)] = first..(first + len)
        acc
    }
    val sorted = map.keys.sortedBy { it.first }
    for ((first, second) in sorted.zipWithNext()) {
        if (first.last + 1 != second.first) {
            val range = first.last..<second.first
            map[range] = range
        }
    }

    return map
}

private fun Map<LongRange, LongRange>.getBy(value: LongRange): List<LongRange> {
    val entry = entries.firstOrNull { (key, _) -> value.first in key }
        ?: return listOf(value)

    val diff = entry.key.last - value.last
    return if (diff < 0) {
        val trailing = getBy(entry.key.last + 1..value.last)
        val offset = value.first - entry.key.first
        val covered = (entry.value.first + offset)..entry.value.last
        listOf(covered) + trailing
    } else {
        val offset = value.first - entry.key.first
        val covered = (entry.value.first + offset)..(entry.value.last - 1 - diff)
        listOf(covered)
    }
}

private fun Map<LongRange, LongRange>.getBy(value: List<LongRange>): List<LongRange> {
    return value.map { getBy(it) }.flatten()
}