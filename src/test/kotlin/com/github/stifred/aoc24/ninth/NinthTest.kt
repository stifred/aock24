package com.github.stifred.aoc24.ninth

import com.github.stifred.aoc24.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NinthTest {
  @Test
  fun test() = runTest(ninth) {
    expected1 = 1928L
    expected2 = 2858L
  }
}