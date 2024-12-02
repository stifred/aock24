package com.github.stifred.aoc24.first

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FirstTest {
  private val solution = first.apply { run() }

  @Test
  fun `test of part 1`() = assertEquals(11, solution.last1)

  @Test
  fun `test of part 2`() = assertEquals(31, solution.last2)
}
