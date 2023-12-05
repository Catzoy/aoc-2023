package day5

val digits = Regex("\\d+")
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
    val seeds = iterator.next().extractNumbers()
    iterator.next() // skip empty line

    val seedToSoilMap = iterator.readNumbers().foldRespectiveMap()
    val soilToFertilizerMap = iterator.readNumbers().foldRespectiveMap()
    val fertilizerToWaterMap = iterator.readNumbers().foldRespectiveMap()
    val waterToLightMap = iterator.readNumbers().foldRespectiveMap()
    val lightToTemperatureMap = iterator.readNumbers().foldRespectiveMap()
    val temperatureToHumidityMap = iterator.readNumbers().foldRespectiveMap()
    val humidityToLocationMap = iterator.readNumbers().foldRespectiveMap()

    val locations = seeds.map { seed ->
        val soil = seedToSoilMap.getOrDefault(seed, seed)
        val fertilizer = soilToFertilizerMap.getOrDefault(soil, soil)
        val water = fertilizerToWaterMap.getOrDefault(fertilizer, fertilizer)
        val light = waterToLightMap.getOrDefault(water, water)
        val temperature = lightToTemperatureMap.getOrDefault(light, light)
        val humidity = temperatureToHumidityMap.getOrDefault(temperature, temperature)
        humidityToLocationMap.getOrDefault(humidity, humidity)
    }

    println(locations.min())
}

fun String.extractNumbers(): List<Long> {
    return digits.findAll(this).map { it.value.toLong() }.toList()
}

fun Iterator<String>.readNumbers(): List<List<Long>> {
    next() // skip title
    val result = mutableListOf<List<Long>>()
    var line = next()
    while (line != "" && hasNext()) {
        result.add(line.extractNumbers())
        line = next()
    }
    return result
}

fun List<List<Long>>.foldRespectiveMap(): Map<LongRange, LongRange> {
    return fold(mutableMapOf()) { acc, (first, second, len) ->
        acc[second..<(second + len)] = first..(first + len)
        acc
    }
}

fun Map<LongRange, LongRange>.getOrDefault(value: Long, default: Long): Long {
    return entries.firstOrNull { (range, _) -> value in range }
        ?.let { (range, mapped) -> mapped.first + (value - range.first) }
        ?: default
}