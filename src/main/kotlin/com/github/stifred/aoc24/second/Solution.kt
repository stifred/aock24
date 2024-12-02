package com.github.stifred.aoc24.second

import com.github.stifred.aoc24.shared.solution
import kotlin.math.abs

val second = solution(2) {
  val reports = parseInput { it.lines().map(::Report) }

  part1 {
    reports.count { it.isSafe() }
  }

  part2 {
    reports.count { it.isMaybeSafe() }
  }
}

data class Report(val text: String) {
  fun isSafe() = text.split(' ').asSequence()
    .map { it.toInt() }
    .windowed(3)
    .all { (a, b, c) ->
      when {
        a - b == 0 -> false
        b - c == 0 -> false
        abs(a - b) > 3 -> false
        abs(b - c) > 3 -> false
        a < b != b < c -> false
        else -> true
      }
    }

  fun isMaybeSafe(): Boolean {
    if (isSafe()) return true

    val words = text.split(' ')

    for (i in words.indices) {
      if (unsafeLevel(words.filterIndexed { index, _ -> index != i }) == null) {
        return true
      }
    }

    return false
  }

  companion object {
    private fun unsafeLevel(levels: List<String>): Int? {
      var last: Int? = null
      var descending: Boolean? = null

      for (levelIndex in levels.indices) {
        val level = levels[levelIndex]

        val num = level.toInt()
        if (last == null) {
          last = num
          continue
        }

        val diff = num - last
        if (diff == 0 || abs(diff) > 3) {
          return levelIndex
        }

        if (descending == null) {
          descending = diff < 0
        } else if (descending != diff < 0) {
          return levelIndex
        }

        last = num
      }

      return null
    }
  }
}
