package com.github.stifred.aoc24

import com.github.stifred.aoc24.shared.Solution
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.file.*
import kotlin.test.assertEquals

fun runTest(
  solution: Solution,
  build: TestBuilder.() -> Unit,
) {
  TestBuilder().apply {
    build()

    fileName
      ?.let { javaClass.getResourceAsStream(it) }
      ?.let { Files.copy(it, Paths.get("/tmp/day-${solution.day}.txt"), StandardCopyOption.REPLACE_EXISTING) }
      ?.also { println("Using filename:  $fileName") }

    solution.run(
      runFirst = expected1 != null,
      runSecond = expected2 != null,
    )

    expected1?.let { assertEquals(it, solution.last1) }
    expected2?.let { assertEquals(it, solution.last2) }
  }
}

class TestBuilder {
  var fileName: String? = null
  var expected1: Any? = null
  var expected2: Any? = null
}
