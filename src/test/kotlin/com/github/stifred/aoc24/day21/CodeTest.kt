package com.github.stifred.aoc24.day21

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CodeTest {
  @Test
  fun `example 1 from part 1`() = codeTest("029A", 68 * 29)
  @Test
  fun `example 2 from part 1`() = codeTest("980A", 60 * 980)
  @Test
  fun `example 3 from part 1`() = codeTest("179A", 68 * 179)
  @Test
  fun `example 4 from part 1`() = codeTest("456A", 64 * 456)
  @Test
  fun `example 5 from part 1`() = codeTest("379A", 64 * 379)

  private fun codeTest(code: String, expected: Long) {
    val before = code.asCode()
    assertEquals(expected, before.complexity(2))
  }
}
