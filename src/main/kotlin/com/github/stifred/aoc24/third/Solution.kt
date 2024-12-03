package com.github.stifred.aoc24.third

import com.github.stifred.aoc24.shared.solution

val third = solution(3) {
  val functions = parseInput { raw ->
    val regex = Regex("((mul)\\(([0-9]+),([0-9]+)\\)|(don't|do)\\(\\))")

    buildList {
      for (match in regex.findAll(raw)) {
        val nonEmpty = match.groupValues.filter { it.isNotBlank() && !it.contains("(") }
        add(
          Function(
            name = nonEmpty[0],
            values = nonEmpty.drop(1).map { it.toInt() },
          )
        )
      }
    }
  }

  part1 {
    functions
      .map { it.run() }
      .filterIsInstance<FunctionResult.Number>()
      .sumOf { it.value }
  }

  part2 {
    data class Status(val sum: Int = 0, val enabled: Boolean = true) {
      fun with(func: Function) = when (val res = func.run()) {
        is FunctionResult.Number -> if (this.enabled) copy(sum = this.sum + res.value) else this
        is FunctionResult.EnabledStatus -> copy(enabled = res.status)
      }
    }

    functions
      .fold(Status()) { acc, func -> acc.with(func) }
      .sum
  }
}

data class Function(val name: String, val values: List<Int>) {
  fun run(): FunctionResult = when (name.lowercase()) {
    "mul" -> FunctionResult.Number(values.reduce(Int::times))
    "do" -> FunctionResult.EnabledStatus(true)
    "don't" -> FunctionResult.EnabledStatus(false)
    else -> error("Unknown: $name")
  }
}

sealed class FunctionResult {
  data class Number(val value: Int) : FunctionResult()
  data class EnabledStatus(val status: Boolean) : FunctionResult()
}
