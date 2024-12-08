package com.github.stifred.aoc24.seventh

import com.github.stifred.aoc24.shared.solution

val seventh = solution(day = 7) {
  val equations = parseInput { it.lineSequence().map(String::asEquation).toList() }

  part1 {
    equations.asSequence()
      .filter { it.trySolve(Operator.allButConcat) }
      .sumOf { it.testValue }
  }
  part2 {
    equations.asSequence()
      .filter { it.trySolve(Operator.all) }
      .sumOf { it.testValue }
  }
}

data class Equation(
  val testValue: Long,
  val operands: List<Long>,
) {
  fun trySolve(operators: List<Operator>) =
    Operator.combo(operators, operands.size).map { oc ->
      oc.foldIndexed(operands[0]) { i, acc, op -> op.apply(acc, operands[i + 1]) }
    }.any { it == testValue }
}

enum class Operator {
  Addition,
  Multiplication,
  Concat,
  ;

  fun apply(a: Long, b: Long) = when (this) {
    Addition -> a + b
    Multiplication -> a * b
    Concat -> scales.first { it > b }.let { (a * it) + b }
  }

  companion object {
    val allButConcat = listOf(Addition, Multiplication)
    val all = entries
    private val scales: LongArray = buildList {
      var a = 1L
      do {
        a *= 10L
        add(a)
      } while (a < Long.MAX_VALUE / 10)
    }.toLongArray()

    private val cache = mutableMapOf<Pair<Int, Int>, List<List<Operator>>>()
    fun combo(operators: List<Operator>, operandCount: Int): Sequence<List<Operator>> {
      val key = operators.size to operandCount
      if (!cache.containsKey(key)) {
        cache[key] = sequence {
          var comboCount = operators.size
          repeat(maxOf(0, operandCount - 2)) {
            comboCount *= operators.size
          }

          for (i in 0 until comboCount) {
            yield(buildList {
              for (char in i.toString(operators.size).padStart(operandCount - 1, '0')) {
                add(operators["$char".toInt() % operators.size])
              }
            })
          }
        }.toList()
      }

      return cache[key]!!.asSequence()
    }
  }
}

private fun String.asEquation(): Equation = split(": ").let { (before, after) ->
  Equation(testValue = before.toLong(), operands = after.split(' ').map(String::toLong))
}
