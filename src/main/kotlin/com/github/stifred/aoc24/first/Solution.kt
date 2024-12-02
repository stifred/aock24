package com.github.stifred.aoc24.first

import com.github.stifred.aoc24.shared.solution
import kotlin.math.abs

val first = solution {
  data class Input(val left: List<Int>, val right: List<Int>)

  day = 1

  val input = parseInput { raw ->
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    for (line in raw.split('\n')) {
      val (a, b) = line.split("   ").map { it.toInt() }
      left += a
      right += b
    }

    Input(left, right)
  }

  part1 {
    val sortedLeft = input.left.sorted()
    val sortedRight = input.right.sorted()

    var sum = 0
    for (i in sortedLeft.indices) {
      sum += abs(sortedLeft[i] - sortedRight[i])
    }

    sum
  }

  part2 {
    val rightCounts = mutableMapOf<Int, Int>()
    for (num in input.right) {
      rightCounts.merge(num, 1) { a, b -> a + b }
    }

    var sum = 0
    for (num in input.left) {
      sum += num * (rightCounts[num] ?: 0)
    }

    sum
  }
}

