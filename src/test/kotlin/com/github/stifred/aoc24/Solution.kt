package com.github.stifred.aoc24

import com.github.stifred.aoc24.shared.Solution
import kotlin.test.assertEquals

fun runTest(
  solution: Solution,
  build: TestBuilder.() -> Unit,
) {
  solution.run()

  TestBuilder().apply {
    build()

    expected1?.let { assertEquals(it, solution.last1) }
    expected2?.let { assertEquals(it, solution.last2) }
  }
}

class TestBuilder {
  var expected1: Any? = null
  var expected2: Any? = null
}
