package com.github.stifred.aoc24

import com.github.stifred.aoc24.shared.Solution
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import kotlin.test.assertEquals

fun runTest(
  solution: Solution,
  build: TestBuilder.() -> Unit,
) {
  TestBuilder().apply {
    build()

    fileName
      ?.let { javaClass.getResourceAsStream(it) }
      ?.let(::InputStreamReader)
      ?.let(::BufferedReader)
      ?.let { FileOutputStream("/tmp/day-${solution.day}.txt") }

    solution.run()

    expected1?.let { assertEquals(it, solution.last1) }
    expected2?.let { assertEquals(it, solution.last2) }
  }
}

class TestBuilder {
  var fileName: String? = null
  var expected1: Any? = null
  var expected2: Any? = null
}
