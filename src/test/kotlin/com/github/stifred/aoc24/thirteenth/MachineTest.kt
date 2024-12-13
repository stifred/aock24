package com.github.stifred.aoc24.thirteenth

import com.github.stifred.aoc24.shared.LongPosition
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MachineTest {
  @Test
  fun `first machine`() {
    val expected = Machine(
      a = LongPosition(94, 34),
      b = LongPosition(22, 67),
      prize = LongPosition(8400, 5400),
    )
    val machine = """
      Button A: X+94, Y+34
      Button B: X+22, Y+67
      Prize: X=8400, Y=5400
    """.trimIndent().toMachine()

    assertEquals(expected, machine)
    assertEquals(280, machine.minimumTokensOrNullFast())
  }

  @Test
  fun `third machine`() {
    val machine = """
      Button A: X+17, Y+86
      Button B: X+84, Y+37
      Prize: X=7870, Y=6450
    """.trimIndent().toMachine()

    assertEquals(200, machine.minimumTokensOrNullFast())
  }

  @Test
  fun `fourth machine`() {
    val expected = Machine(
      a = LongPosition(69, 23),
      b = LongPosition(27, 71),
      prize = LongPosition(18641, 10279),
    )
    val machine = """
      Button A: X+69, Y+23
      Button B: X+27, Y+71
      Prize: X=18641, Y=10279
    """.trimIndent().toMachine()

    assertEquals(expected, machine)
    assertEquals(null, machine.minimumTokensOrNullFast())
  }
}