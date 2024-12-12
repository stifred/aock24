package com.github.stifred.aoc24.shared

import java.io.File
import java.io.FileOutputStream
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max

fun solution(day: Int, func: SolutionDsl.() -> Unit): Solution {
  val sol = SolutionDsl(day)
  sol.runner = func

  val file = File("/tmp/day-${day}.txt")
  if (!file.exists()) {
    sol.javaClass.getResourceAsStream("/day-$day.txt")?.readAllBytes()
      ?.let { FileOutputStream(file).write(it) }
  }

  return Solution(day, sol)
}

class Solution(val day: Int, private val dsl: SolutionDsl) {
  val last1 get() = dsl.lastPart1
  val last2 get() = dsl.lastPart2

  fun run(
    runFirst: Boolean = true,
    runSecond: Boolean = true,
    runBenchmark: Boolean = false,
  ) {
    dsl.runFirst.set(runFirst)
    dsl.runSecond.set(runSecond)
    dsl.runBenchmark.set(if (runBenchmark) 3 else 0)

    println("Day:            $day")
    dsl.runner(dsl)
  }
}

class SolutionDsl(private val day: Int) {
  lateinit var runner: SolutionDsl.() -> Unit

  lateinit var lastPart1: Any
  lateinit var lastPart2: Any

  val runFirst = AtomicBoolean(true)
  val runSecond = AtomicBoolean(true)
  val runBenchmark = AtomicInteger(0)

  fun parseInput(): String = parseInput(timed = false) { it }

  fun <T> parseInput(timed: Boolean = true, parser: (String) -> T): T {
    println(" ")

    val text = javaClass.getResourceAsStream("/day-$day.txt")!!
      .readAllBytes()
      .toString(Charsets.UTF_8)

    val before = System.nanoTime()
    val ret = parser(text)
    val after = System.nanoTime()

    if (timed) {
      val duration = Duration.ofNanos(after - before)
      println("Parse time:     ${duration.prettyPrint()}")

      if (runBenchmark.getAndDecrement() > 0) {
        val reps = duration.repeatCount()
        println("Parse BM reps:  $reps")
        val bmBefore = System.nanoTime()
        repeat(reps) { parser(text) }
        val bmAfter = System.nanoTime()
        println("Parse BM time:  ${Duration.ofNanos((bmAfter - bmBefore) / reps).prettyPrint()}")
      }
    }

    return ret
  }

  fun part1(act: () -> Any) = part(1, runFirst, act)
  fun part2(act: () -> Any) = part(2, runSecond, act)

  private fun part(int: Int, shouldRun: AtomicBoolean, act: () -> Any) {
    if (!shouldRun.getAndSet(true)) return
    println(" ")

    val before = System.nanoTime()
    val ret = act()
    val after = System.nanoTime()
    val duration = Duration.ofNanos(after - before)
    println("Part $int time:    ${duration.prettyPrint()}")

    println("Part $int output:  $ret")

    if (runBenchmark.getAndDecrement() > 0) {
      val reps = duration.repeatCount()
      println("Part $int BM reps: $reps")
      val bmBefore = System.nanoTime()
      repeat(reps) { act() }
      val bmAfter = System.nanoTime()
      println("Part $int BM time: ${Duration.ofNanos((bmAfter - bmBefore) / reps).prettyPrint()}")
    }

    when (int) {
      1 -> lastPart1 = ret
      2 -> lastPart2 = ret
    }
  }

  private fun Duration.repeatCount(): Int = max((30_000_000_000 / toNanos()).toInt(), 1)

  private fun Duration.prettyPrint() = when {
    this > Duration.ofSeconds(10) -> "${toSeconds()} s"
    this > Duration.ofMillis(10) -> "${toMillis()} ms"
    this > Duration.ofNanos(10_000) -> "${toNanos() / 1000} µs"
    else -> "${toNanos()} ns"
  }
}
