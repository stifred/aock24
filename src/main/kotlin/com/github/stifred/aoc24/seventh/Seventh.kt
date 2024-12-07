package com.github.stifred.aoc24.seventh

import com.github.stifred.aoc24.shared.solution
import java.util.concurrent.Executors

val seventh = solution(day = 7) {
  val equations = parseInput { it.lines().map(String::asEquation) }

  part1 {
    equations.asSequence()
      .filter { it.trySolve(Operator.allButConcat) }
      .sumOf(Equation::testValue)
  }
  part2 {
    equations.asSequence()
      .filter { it.trySolve(Operator.all) }
      .sumOf(Equation::testValue)
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
    Concat -> "$a$b".toLong()
  }

  companion object {
    val allButConcat = listOf(Addition, Multiplication)
    val all = entries

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