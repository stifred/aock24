package com.github.stifred.aoc24.shared

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PositionTest {
  @Test
  fun `test between`() {
    val topLeft = Position(10, 10)
    val topRight = Position(12, 10)
    val bottomLeft = Position(10, 12)

    val expectedHorizontal = setOf(Position(10, 10), Position(11, 10), Position(12, 10))
    val actualHorizontal = Position.between(topLeft, topRight)
    assertEquals(expectedHorizontal, actualHorizontal)

    val expectedVertical = setOf(Position(10, 10), Position(10, 11), Position(10, 12))
    val actualVertical = Position.between(topLeft, bottomLeft)
    assertEquals(expectedVertical, actualVertical)

    val expectedBanana = setOf(Position(12, 10), Position(11, 10), Position(10, 10), Position(10, 11), Position(10, 12))
    val actualBanana = Position.between(topRight, bottomLeft, alt = true)
    assertEquals(expectedBanana, actualBanana)
  }

  @Test
  fun `manhattan distance`() {
    assertEquals(10, Position(10, 10) manhattanTo Position(20, 10))
    assertEquals(10, Position(10, 10) manhattanTo Position(10, 20))
    assertEquals(15, Position(10, 10) manhattanTo Position(15, 20))
    assertEquals(20, Position(10, 10) manhattanTo Position(20, 20))
  }
}