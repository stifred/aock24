package com.github.stifred.aoc24.day18

import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MemorySpaceTest {
  private val before = """
      5,4
      4,2
      4,5
      3,0
      2,1
      6,3
      2,4
      1,5
      0,6
      3,3
      2,6
      5,1
      1,2
      5,5
      2,5
      6,5
      1,4
      0,4
      6,4
      1,1
      6,1
      1,0
      0,5
      1,6
      2,0
    """.trimIndent().asMemorySpace()

  @Test
  fun `example from part 1`() {
    val after = before.after(12)

    assertEquals(22, after.shortestPathLength)
  }

  @Test
  fun `example from part 2`() {
    val firstBlocked = (12 until before.maxTime).asSequence()
      .map { before.after(it) }
      .filter { it.shortestPathLength == null }
      .map { it.lastAddedByte }
      .first()

    assertEquals(Position(6, 1), firstBlocked)
  }

  @Test
  fun `unit is unit`() {
    val a = Unit
    val b = Unit

    assertEquals(a, b)
  }
}