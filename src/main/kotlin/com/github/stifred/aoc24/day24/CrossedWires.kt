package com.github.stifred.aoc24.day24

import com.github.stifred.aoc24.shared.solution

val crossedWires = solution(day = 24) {
  val crosser = parseInput { it.asWireCrosser() }

  part1 { crosser.zValue() }
  part2 { crosser.findAdderMistakes().sorted().joinToString(",") }
}

data class Function(
  val a: String,
  val exp: String,
  val b: String,
  val out: String,
) {
  val operands = setOf(a, b)
  val areInputs = operands.any { it.startsWith('x') }
}

data class WireCrosser(
  private val inputs: Map<String, Boolean>,
  private val functions: Set<Function>,
) {
  private val byOut = functions.associateBy { it.out }

  fun zValue(): Long {
    var z = 0L
    for (i in 0..64) {
      val key = letterValue('z', i)
      if (byOut.containsKey(key)) {
        z += (if (valueOf(key)) 1L else 0L) shl i
      }
    }

    return z
  }

  fun findAdderMistakes() = buildSet {
    for (gate in functions) {
      val (left, right) = gate.operands.sorted()
      val exp = gate.exp
      val out = gate.out

      when {
        out.startsWith('z') && out != "z45" -> {
          if (exp != "XOR") add(out)
        }
        !gate.areInputs -> {
          if (exp == "XOR") add(out)
        }
        gate.areInputs -> {
          if (left.endsWith("00") || right.endsWith("00")) {
            continue
          }

          val next = functions.filter { out in it.operands }.map { it.exp }

          if ((exp == "XOR" && "XOR" !in next) || (exp == "AND" && "OR" !in next)) {
            add(out)
          }
        }
      }
    }
  }

  private fun valueOf(key: String): Boolean {
    inputs[key]?.let { return it }
    val func = byOut.getValue(key)

    val a = valueOf(func.a)
    val b = valueOf(func.b)

    return when (func.exp) {
      "AND" -> a && b
      "OR" -> a || b
      "XOR" -> a != b
      else -> error("Error")
    }
  }
}

fun String.asWireCrosser() = split("\n\n").let { (inputs, gates) ->
  WireCrosser(
    inputs = inputs.lineSequence()
      .map { it.split(": ") }
      .map { (key, value) -> key to (value == "1") }
      .toMap(),
    functions = gates.lineSequence()
      .map { it.split(" -> ") }
      .map { (before, after) -> before.split(' ') + after }
      .map { (a, exp, b, out) -> Function(a, exp, b, out) }
      .toSet()
  )
}

private fun letterValue(prefix: Char, num: Int) = buildString {
  append(prefix)
  if (num < 10) append('0')
  append(num)
}
