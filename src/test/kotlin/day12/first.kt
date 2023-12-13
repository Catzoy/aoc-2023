package day12

import kotlin.test.Test
import kotlin.test.assertEquals

class TestsDay12 {
    @Test
    fun `should position single broken`() {
        val input = """
            ???? 1
        """.trimIndent()
        val result = solution(input)
        val expected = 4L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken`() {
        val input = """
            ??.? 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 2L
        assertEquals(expected, result)
    }

    @Test
    fun `should position single broken in multiple available`() {
        val input = """
            ??.? 1
        """.trimIndent()
        val result = solution(input)
        val expected = 3L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in single available`() {
        val input = """
            ????? 2,2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in multiple available`() {
        val input = """
            ???.??? 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 11L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in excessive available`() {
        val input = """
            ???.???.??? 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 30L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in different available`() {
        val input = """
            ???.? 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 4L
        assertEquals(expected, result)
    }

    @Test
    fun `should position single broken in left preoccupied available`() {
        val input = """
            #? 2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should position single broken in right preoccupied available`() {
        val input = """
            ?# 2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should count filled preoccupied`() {
        val input = """
            ## 2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in right preoccupied available`() {
        val input = """
            ???# 1,2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should position multiple broken in left preoccupied available`() {
        val input = """
            #??? 1,2
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should account for already present in preoccupied`() {
        val input = """
            #.??? 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 3L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 1`() {
        val input = """
            ???.### 1,1,3
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 2`() {
        val input = """
            .??..??...?##. 1,1,3
        """.trimIndent()
        val result = solution(input)
        val expected = 4L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 3`() {
        val input = """
            ?#?#?#?#?#?#?#? 1,3,1,6
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 4`() {
        val input = """
            ????.#...#... 4,1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 1L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 5`() {
        val input = """
            ????.######..#####. 1,6,5
        """.trimIndent()
        val result = solution(input)
        val expected = 4L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 6`() {
        val input = """
            ?###???????? 3,2,1
        """.trimIndent()
        val result = solution(input)
        val expected = 10L
        assertEquals(expected, result)
    }

    @Test
    fun `should pass test input 7`() {
        val input = """
            ???.# 1,1
        """.trimIndent()
        val result = solution(input)
        val expected = 3L
        assertEquals(expected, result)
    }


}