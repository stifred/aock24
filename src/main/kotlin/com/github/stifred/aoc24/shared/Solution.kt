package com.github.stifred.aoc24.shared

import java.time.Duration
import java.time.Instant

fun solution(func: SolutionDsl.() -> Unit): Solution {
  val sol = SolutionDsl()
  sol.runner = func

  return Solution(sol)
}

class Solution(private val dsl: SolutionDsl) {
  val day get() = dsl.day
  val last1 get() = dsl.lastPart1
  val last2 get() = dsl.lastPart2

  fun run() {
    dsl.runner(dsl)
  }
}

class SolutionDsl {
  lateinit var runner: SolutionDsl.() -> Unit

  var day = 1

  lateinit var lastPart1: Any
  lateinit var lastPart2: Any

  fun parseInput(): String = parseInput(timed = false) { it }

  fun <T> parseInput(timed: Boolean = true, parser: (String) -> T): T {
    val text = javaClass.getResourceAsStream("/day-$day.txt")!!
      .readAllBytes()
      .toString(Charsets.UTF_8)

    val before = Instant.now()
    val ret = parser(text)
    val after = Instant.now()

    if (timed) {
      println("Parse time:    " + Duration.between(before, after).prettyPrint())
    }

    return ret
  }

  fun part1(act: () -> Any) {
    val before = Instant.now()
    val ret = act()
    val after = Instant.now()

    println("Part 1 time:   ${Duration.between(before, after).prettyPrint()}")
    println("Part 1 output: $ret")

    lastPart1 = ret
  }

  fun part2(act: () -> Any) {
    val before = Instant.now()
    val ret = act()
    val after = Instant.now()

    println("Part 2 time:   ${Duration.between(before, after).prettyPrint()}")
    println("Part 2 output: $ret")

    lastPart2 = ret
  }

  private fun Duration.prettyPrint() = when {
    this > Duration.ofMillis(10) -> "${toMillis()} ms"
    this > Duration.ofNanos(10_000) -> "${toNanos() / 1000} µs"
    else -> "${toNanos()} ns"
  }
}
