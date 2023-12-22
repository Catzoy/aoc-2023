package day22

data class Cube(
    val name: Int,
    val position: Pair<Int, Int>,
    val sizeX: Int,
    val sizeY: Int,
    val sizeZ: Int
)
// Z-Range to list of (X-Range with Y-Range)
typealias Pos = Pair<Int, List<Cube>>

fun main() {
    val cubeRegex = Regex("""(\d+),(\d+),(\d+)~(\d+),(\d+),(\d+)""")
    val input = """
1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
    """.trimIndent()

    val matrix = buildMatrix(input, cubeRegex)
    for ((i, data) in matrix.withIndex()) {
        val (z, cubes) = data
        if (z == 0) continue

        for (cube1 in cubes) {
            val (x1, y1) = cube1.position
            val supporters = mutableListOf<Pos>()
            for (j in i - 1 downTo 0) {
                val (_, cubes2) = matrix[j]
                for (cube2 in cubes2) {
                    val (x2, y2) = cube2.position
                    if (x1 + cube1.sizeX <= x2) continue
                    if (x1 >= x2 + cube2.sizeX) continue
                    if (y1 + cube1.sizeY <= y2) continue
                    if (y1 >= y2 + cube2.sizeY) continue
                    supporters.add(matrix[j])
                }
                if (supporters.isNotEmpty()) break
            }
        }
    }
}

private fun buildMatrix(input: String, cubeRegex: Regex): MutableList<Pos> {
    val m = mutableListOf<Pos>()
    for ((i, line) in input.lineSequence().withIndex()) {
        val (x1, y1, z1, x2, y2, z2) = cubeRegex.matchEntire(line)!!.destructured
        val zCoord = z1.toInt()
        val cube = Cube(
            name = i,
            position = x1.toInt() to y1.toInt(),
            sizeX = x2.toInt() - x1.toInt() + 1,
            sizeY = y2.toInt() - y1.toInt() + 1,
            sizeZ = z2.toInt() - z1.toInt() + 1
        )
        val originalIndex = m.indexOfFirst { (z, _) -> z >= zCoord }
        if (originalIndex == -1) {
            m.add(zCoord to listOf(cube))
            continue
        }
        val (z, cubes) = m[originalIndex]
        if (z == zCoord) {
            m[originalIndex] = z to (cubes + cube)
            continue
        }
        m.add(originalIndex, zCoord to listOf(cube))
    }
    return m
}