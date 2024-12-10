package com.github.stifred.aoc24.tenth

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TrailMapTest {
  @Test
  fun `test 1`() {
    val map = """
      ..90..9
      ...1.98
      ...2..7
      6543456
      765.987
      876....
      987....
    """.trimIndent().toTrailMap()

    assertEquals(4, map.score)
  }

  @Test
  fun `test 2`() {
    val map = """
      10..9..
      2...8..
      3...7..
      4567654
      ...8..3
      ...9..2
      .....01
    """.trimIndent().toTrailMap()

    assertEquals(3, map.score)
  }
}