package com.github.stifred.aoc24.day17

import com.github.stifred.aoc24.day17.Computer.Companion.twoExp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComputerTest {
  @Test
  fun `example 1`() {
    val before = Computer(c = 9L, program = listOf(2, 6))
    val after = before.runAll()

    assertEquals(1, after.b)
  }

  @Test
  fun `example from part 2`() {
    val before = Computer(a = 117440L, program = listOf(0, 3, 5, 4, 3, 0))
    val after = before.runAll()

    assertEquals(before.program, after.outputs)
  }

  @Test
  fun `part 2 reverse engineering`() {
    val before = Computer(a = 2024L, program = listOf(0, 3, 5, 4, 3, 0))
    val after = before.reverseEngineer()

    assertEquals(117440L, after.a)
    assertEquals(before.program, after.runAll().outputs)
  }

  @Test
  fun `two exp`() {
    assertEquals(1, twoExp(0))
    assertEquals(2, twoExp(1))
    assertEquals(4, twoExp(2))
    assertEquals(8, twoExp(3))
    assertEquals(16, twoExp(4))
    assertEquals(32, twoExp(5))
  }

  @Test
  fun `with xor`() {
    for (i in 1..1000) {
      for (j in 1..1000) {
        assertEquals(i, (i xor j) xor j)
      }
    }
  }
}
