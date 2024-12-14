package com.github.stifred.aoc24.day14

import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AreaTest {
  @Test
  fun `test input`() {
    val robotStr = """
      p=0,4 v=3,-3
      p=6,3 v=-1,-3
      p=10,3 v=-1,2
      p=2,0 v=2,-1
      p=0,0 v=1,3
      p=3,0 v=-2,-2
      p=7,6 v=-1,-3
      p=3,0 v=-1,-2
      p=9,3 v=2,3
      p=7,3 v=-1,2
      p=2,4 v=2,-3
      p=9,5 v=-3,-3
    """.trimIndent()

    val before = Area(Position(11, 7)).withRobotString(robotStr)
    val after = before.after(100)
    val split = after.splitIntoQuadrants()

    val expected = 12
    val actual = split.fold(1) { acc, a -> acc * a.safetyFactor }

    assertEquals(expected, actual)
  }
}