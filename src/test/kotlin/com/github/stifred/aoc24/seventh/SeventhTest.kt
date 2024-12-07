package com.github.stifred.aoc24.seventh

import com.github.stifred.aoc24.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SeventhTest {
  @Test
  fun test() = runTest(seventh) {
    expected1 = 3749L
    expected2 = 11387L
  }
}