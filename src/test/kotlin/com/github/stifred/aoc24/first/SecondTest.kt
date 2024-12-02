package com.github.stifred.aoc24.first

import com.github.stifred.aoc24.second.second
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecondTest {
  private val solution = second.apply { run() }

  @Test
  fun `test of part 1`() = assertEquals(TODO(), solution.last1)

  @Test
  fun `test of part 2`() = assertEquals(TODO(), solution.last2)
}
