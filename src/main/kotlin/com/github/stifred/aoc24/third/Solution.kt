package com.github.stifred.aoc24.third

import com.github.stifred.aoc24.shared.solution

val third = solution(3) {
  val functions = parseInput { raw ->
    Regex("((mul)\\(([0-9]+),([0-9]+)\\)|(don't|do)\\(\\))")
      .findAll(raw)
      .map { it.groupValues.filter(String::isFunctionNameOrNumber) }
      .map { Function(name = it[0], values = it.drop(1).map(String::toInt)) }
      .toList()
  }

  part1 {
    functions.asSequence()
      .map { it.run() }
      .filterIsInstance<FunctionResult.Number>()
      .sumOf { it.value }
  }

  part2 {
    functions
      .fold(Accumulator()) { status, func -> status with func }
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

data class Accumulator(val sum: Int = 0, val enabled: Boolean = true) {
  infix fun with(func: Function) = when (val res = func.run()) {
    is FunctionResult.Number -> if (this.enabled) copy(sum = this.sum + res.value) else this
    is FunctionResult.EnabledStatus -> copy(enabled = res.status)
  }
}

private fun String.isFunctionNameOrNumber() = isNotBlank() && !contains("(")
