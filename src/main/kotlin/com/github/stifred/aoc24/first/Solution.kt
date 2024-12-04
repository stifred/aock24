package com.github.stifred.aoc24.first

import com.github.stifred.aoc24.shared.solution
import kotlin.math.abs

val first = solution(1) {
  data class Input(val left: List<Int>, val right: List<Int>)

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

    sortedLeft.indices.sumOf { abs(sortedLeft[it] - sortedRight[it]) }
  }

  part2 {
    val rightCounts = input.right
      .groupBy { it }
      .map { it.key to it.value.size }
      .toMap()

    input.left.asSequence()
      .filter { rightCounts.containsKey(it) }
      .sumOf { it * rightCounts[it]!! }
  }
}

