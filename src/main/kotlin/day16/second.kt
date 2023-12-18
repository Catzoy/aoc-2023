package day16

fun main() {
    val input = """
\.|...|...-/..........-.............|./..........\...................\.-......-...../..../.........-../.......
................-...........................................\...................|......-......................
.\..........|...|............................|..............\................../......\..................|....
\...-..|......--..............|.................................|................\..........-........-......-.
............\......./.........................\........../..../............\......./.........................-
.........|............................/...................-................-../.\|.................-..\.......
.......................|............................/.../\-.....-....-\-|.........-/|....\........\.........\.
.\-............................../.....................................|................|....................|
....../...........................\......././-.........|............................................/.........
.............|................/......|......./.|...|.....................|....................................
.......|.............\...............\.....|.....\......................\.............-.......|...../.........
|........-........-...\...........|............|......-.|.......|.................|............/......-.......
..............\...............\......-........|../........................../..................|/.........|...
............\.../................\.....................\...|............/.........................\.....\.....
...../......./......../............|......\...\...........\.........-...............-..................\..|...
................\....\.|...............-......-..../.-\......................-.........|......................
.............../................\.............//.....\...................................-....\......../......
...............\.............................\...-..\.......\.\.................|..............||/............
.........../........./...././...........................\..../........-.......|.--.......................|.|..
..\............/..|......|.\.|/...............|......|........./.|.....................................\......
..........||........-...../.........................................-.............../.....................\...
................|........../..........................-....-../...................................\........-..
.............................-..-.................../..-...-.......|..................|......../............./
................\......................\........./...-...\|.............\..|.....|............|/...........-..
............|/../..-.................|./.......................\..............................................
.................................../.........-..-..-...-............-....-....\...........\..\.|....-.........
..../.......|..................-.....|./.-......-|............................................................
......../........................./.-./.............................................\.....\-\..|....../.......
-...........................-.\........-........|......|.........|....-....|......|......\..............-./...
./....-/.........-.................\.......\......./..............|.........||.|...........-..........-..|...|
...........................|........||................|..../.-.|...............-..............................
../.../................................../...-..........-.................../.../.............................
.................-.../..............\-........-/...../........................................./.....|../.....
....................\..//.....|...\............/................./...................-.......\\..........-....
....-.\..-...........\.............................\.......................\.................|.......|.......|
.|.../.............................\.-......\.............../.../................/....|.......................
...................-.|.............|............../.......................................|.................|.
\......................../.....-......|....|..............\\............|....................\......./........
...../..........|.................\.....\.../..............--....|.|.............................-............
....\...|........./................\......................................................./....../...-...\...
......-......................|../.......\.....|../...|..\...................|..\.........../....\|...........-
/.........-..........-..\..................\....-....../.-.........|.................\......./...-.|......../.
......-..|\................../../...|.|...................../........................\.|-/....................
......../..............\...............................-....|/....../.\.......\..-..../...../....\..//........
..\..|./.......|................../...../.........\.........../..\\...|.........................\|............
|..|............../.....\..-................/................\............|\..................................
...\.../...\.................|.....................|.............|............................................
|.-...\.......-............../....-....../..-./..../.....-............|...|......|..-...\./....\/-....\.......
..........|...............\.\...............................\...../...........-\.........\./.............../..
.../....../....-.........-...\./...........\....|............-..........-|.....|...|..-./.........../.........
/\................\..|............./..-..../.....................\..............\...|.............|........../
\.............\...............................\.........-..\....../.-............\/..../.\...\...\\...........
.............\/...\.\................................\.-..................../....\....................//..-...
..|../.../....................................|........\............................|.............../.........
............/..............\..../..|.-......\.\..|...............\..-.....|.............../.../\.......|......
...............\....\...............................-.|....|..............-.......|..........-......\.........
..|.....\...............-........./.......................-...|..........\./.....\...../................|.....
..\.........|..............-.....|.-............\.................\.........................|........||.......
/...............-./..........|...\...................................-...\......-.../....../.....|............
................................./.-..................-.........................-.............|.........\./...
..|.|.....................-............|/...................\............/.-......-.....\../.|.....|..........
..-./......|............../....-......\..............|-.....................\................-............\...
............................/...........|.-.\..../.\.......\............|.....................................
........................|........................................................../....\..-........|...|.....
....-............\............................\......|.........|\.............../-...........|......\.-.|...|.
.....\........../.....|......-././.......\.-............-/..........-....././..-......|...|-........|.........
.........-.|......../....\|...-........||....../........|-....|.........................|................-.-..
..-.........--.........../...\...............................\..|../...\..../.../..................|...-....\.
.................\.../...........-..........|..../...............|.............|..........||.........\........
......./-..../................\../-................................/...............|.......|.\.......-.....|..
../..-........./.|..\..\..../.\............/......-.|....\.....-...................................../.-....|.
......./........../..........|...../....................\../...\....................-......\......|......../..
.....\...-.....-\...................|....../.....-.....................\......................................
.....-......./............-........................../....|.................-..........|..............\.......
....//.........................|.......|...|................./........-...........|......|-.....././..........
.........../.........|-....................................\.-....|...-....................|.../..........//..
.................................\/......................................\..||.......................\........
.............................../.........\.......-...|...................................\.|..........|.......
.......-\.........|......./.............................|..|...................-.../....../\.....\............
...................\.....\.-......../......|......-...\.|........|.../...................................\....
........................\.......-..|/....-../.........-..-....\............./../.........\................../.
..-.-......-....-..-.....-........|..../...........................||-/................./.|......./.../....-..
......|....|....|../...........-.|....-........|........-.......|.............\.......-|..-......-../..-......
...-......-|...\...................|..-..../...||....-..........|./.../.|...\........|./.......|./\|..........
.................-\|./..................|...........................\.............|...../.........|.....\.....
...\.\....................//..................-......-...................\.....\.......................\......
........./.................|..-........../.........|..|........../..|..\.........-/........\.-................
.../.\....|....../.../............................-.......\...........|.....|....-.-........./......../.......
........|...............|.......-...|..-.......\.-.........|......-...|..................-....................
......\./.../..................................\.............|......./../..\.......-....../...-..|..\..\......
/......./...-............/\..|.............-......\.....-.......-....|......../..........-....................
.\.|..../.....--........-....-.../.............|................................|.........-.\.....-..|........
.-.....................-.|.............................../..\...........|.................|./......-..........
......................................\.......\..............................-.........|......-........|......
........../....|...........|...|.\...\......................../.............|......../......../....-.-.....-..
....\....|.............................../\........................../......-......................|.......-..
\../..........|.....................|.../...-...../.\......\.\........-..............|........-....-..\.......
....................|...............-........./........-.-........||............\..................|.....-....
........-|...........|.-..|.................\../.............................\./.................\............
..........-.\..../.......\.....................-................../.............\........-....\|./............
....................................../...-...|.|...............................|......./.......-.............
............../............/............./............\...../..................../...\.-......................
.|/..........|.\....................|...../.........|...................................\...|\..\.............
.\......././|............/...\...\...............-......../../..........................-./.........-.........
.-............|.........................-|............|.....-............\...........|........-...............
..-...............-......../............../.........././....../......-..............-......\.//-..............
...-.|................................../.....\....-...............-............-..........-........../...\...
....................-.....-.........-\....\....-./......................................./..............-.\\./
.............../.|........-......-..........................\./........\...\...|.-..............\.....-......|
...../......\................//........-..............-...........-...................\./....\..............-.
    """.trimIndent()
    val lines = input.lines()
    val vertical = lines.size
    val horizontal = lines.first().length
    val starters = sequence {
        for (x in 0..<horizontal) {
            yield(Triple(x, 0, Direction.DOWN))
            yield(Triple(x, vertical - 1, Direction.UP))
        }

        for (y in 0..<vertical) {
            yield(Triple(0, y, Direction.LEFT))
            yield(Triple(horizontal - 1, y, Direction.RIGHT))
        }
    }

    val result = starters.map { (x, y, direction) ->
        val matrix = lines.map { line -> MutableList<Cell>(line.length) { i -> Cell.Empty(line[i]) } }
        var beams = listOf(Beam(x, y, direction))
        do {
            beams = beams.flatMap { beam -> beam.proceed(matrix) }
        } while (beams.isNotEmpty())

        matrix.sumOf { line ->
            line.sumOf {
                when (it) {
                    is Cell.Empty -> 0L
                    is Cell.Energized -> 1L
                }
            }
        }
    }.max()
    println(result)
}