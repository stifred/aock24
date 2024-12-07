package com.github.stifred.aoc24.seventh

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EquationTest {
  val eq = Equation(
    testValue = 7290,
    operands = listOf(6, 8, 6, 15),
  )

  @Test
  fun `test without concat`() = assertFalse { eq.trySolve(Operator.allButConcat) }

  @Test
  fun `test with concat`() = assertTrue { eq.trySolve(Operator.all) }
}