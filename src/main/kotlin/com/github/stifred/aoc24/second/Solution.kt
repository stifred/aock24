package com.github.stifred.aoc24.second

import com.github.stifred.aoc24.shared.solution
import kotlin.math.abs

val second = solution(2) {
  val reports = parseInput { raw ->
    raw.lines()
      .map { line -> line.split(' ').map(String::toInt) }
      .map { Report(it) }
  }

  part1 { reports.count(Report::isSafe) }
  part2 { reports.count(Report::isSafeWithDampener) }
}

data class Report(val values: List<Int>) {
  val isSafe get() = values.windowed(3).all { (a, b, c) ->
      when {
        a - b == 0 -> false
        b - c == 0 -> false
        abs(a - b) > 3 -> false
        abs(b - c) > 3 -> false
        a < b != b < c -> false
        else -> true
      }
    }
  val isSafeWithDampener get(): Boolean =
    isSafe
      || values.indices.asSequence()
        .map { Report(values.filterIndexed { index, _ -> index != it }) }
        .any { it.isSafe }
}
