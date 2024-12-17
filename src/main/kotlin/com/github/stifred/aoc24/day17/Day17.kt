package com.github.stifred.aoc24.day17

import com.github.stifred.aoc24.shared.solution

val day17 = solution(day = 17) {
  val computer = parseInput { it.toComputer() }

  part1 { computer.runAll().outputs.joinToString(",") }
  part2 { computer.reverseEngineer().a }
}

data class Computer(
  val a: Long = 0,
  val b: Long = 0,
  val c: Long = 0,
  val program: List<Short> = emptyList(),
  val counter: Int = 0,
  val outputs: List<Short> = emptyList(),
) {
  fun runAll(): Computer {
    var current = this
    while (current.counter < program.size) {
      current = current.runNext()
    }
    return current
  }

  fun reverseEngineer(): Computer {
    val possibleInputs = mutableListOf(0L)
    for (expectedOutput in program.reversed()) {
      val toCheck = possibleInputs.toList()
      possibleInputs.clear()

      for (aValue in toCheck) {
        for (attempt in 0L..7L) {
          val candidate = (aValue shl 3) + attempt

          if (copy(a = candidate).runOnceAndGetOutput() == expectedOutput) {
            possibleInputs.add(candidate)
          }
        }
      }
    }

    return copy(a = possibleInputs.min())
  }

  private fun runOnceAndGetOutput(): Short = runNext().let { it.outputs.firstOrNull() ?: it.runOnceAndGetOutput() }

  private val next get() = counter + 2
  private fun runNext(): Computer {
    val op = program[counter]
    val combo = program[counter + 1]

    return when (op) {
      ADV -> copy(a = a / twoExp(combo.toLiteral()), counter = next)
      BXL -> copy(b = b xor combo.toLong(), counter = next)
      BST -> copy(b = combo.toLiteral() % 8, counter = next)
      JNZ -> if (a == 0L) copy(counter = next) else copy(counter = combo.toInt())
      BXC -> copy(b = b xor c, counter = next)
      OUT -> copy(outputs = outputs + (combo.toLiteral() % 8).toShort(), counter = next)
      BDV -> copy(b = a / twoExp(combo.toLiteral()), counter = next)
      CDV -> copy(c = a / twoExp(combo.toLiteral()), counter = next)
      else -> error("Unknown OP $op")
    }
  }

  private fun Short.toLiteral(): Long = when {
    this < A -> toLong()
    this == A -> a
    this == B -> b
    this == C -> c
    else -> -1
  }

  companion object {
    private const val ADV: Short = 0
    private const val BXL: Short = 1
    private const val BST: Short = 2
    private const val JNZ: Short = 3
    private const val BXC: Short = 4
    private const val OUT: Short = 5
    private const val BDV: Short = 6
    private const val CDV: Short = 7

    private const val A: Short = 4
    private const val B: Short = 5
    private const val C: Short = 6

    fun twoExp(to: Long): Long = if (to == 0L) 1 else 2L shl (to.toInt() - 1)
  }
}

fun String.toComputer(): Computer {
  val (a, b, c, _, program) = lines()

  return Computer(
    a = a.substring(12).toLong(),
    b = b.substring(12).toLong(),
    c = c.substring(12).toLong(),
    program = program.substring(9).split(',').map(String::toShort),
  )
}
