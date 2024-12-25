package com.github.stifred.aoc24.day24

import com.github.stifred.aoc24.shared.solution

val crossedWires = solution(day = 24) {
  val crosser = parseInput { it.asWireCrosser() }

  part1 { crosser.zValue() }
  part2 { crosser.checkAdder().sorted().joinToString(",") }
}

data class Function(
  val a: String,
  val exp: String,
  val b: String,
  val out: String,
) {
  val operands = setOf(a, b)
  val areInputs = operands.any { it.startsWith('x') }

  fun applyOn(map: MutableMap<String, Boolean>): Boolean {
    val aValue = map[a] ?: return false
    val bValue = map[b] ?: return false

    val outValue = when (exp) {
      "AND" -> aValue && bValue
      "OR" -> aValue || bValue
      "XOR" -> aValue != bValue
      else -> error("Unknown exp. $exp")
    }

    map[out] = outValue
    return true
  }
}

data class WireCrosser(
  private val inputs: Map<String, Boolean>,
  private val functions: Set<Function>,
) {
  private val byOut = functions.associateBy { it.out }

  fun withInputs(x: Long, y: Long) = copy(inputs = x.toMap('x') + y.toMap('y'))

  private fun Long.toMap(prefix: Char) = buildMap {
    for (i in 0..64) {
      put(letterValue(prefix, i), false)
    }

    var i = 0
    var x = this@toMap
    while (x > 0L) {
      put(letterValue(prefix, i), x % 2 > 0)
      x = x shr 1
      i++
    }
  }

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

  fun checkAdder() = buildSet<String> {
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

  fun checkAdders(): Set<String> {
    val set = emptySet<String>().toMutableSet()

    for (i in 1..64) {
      if (!inputs.containsKey(letterValue('x', i))) break
      val currentInputs = listOf(letterValue('x', i), letterValue('y', i))

      val z2 = byOut.getValue(letterValue('z', i))
      if (z2.exp != "XOR") {
        set += z2.out
        continue
      }

      val a2 = byOut.getValue(z2.a).takeIf { it.exp == "XOR" } ?: byOut.getValue(z2.b)
      if (a2.exp != "XOR" || a2.a !in currentInputs || a2.b !in currentInputs) {
        set += a2.out
        continue
      }

      val b2 = if (a2.out == z2.a) byOut.getValue(z2.b) else byOut.getValue(z2.a)
      if (b2.exp != "OR" || b2.a in currentInputs || b2.b in currentInputs) {
        set += b2.out
        continue
      }

      val lastInputs = listOf(letterValue('x', i -1), letterValue('y', i -1))
      val c2 = byOut.getValue(b2.a).takeIf { it.a !in lastInputs }  ?: byOut.getValue(b2.b)
      if (c2.exp != "AND") {
        set += c2.out
        continue
      }

      val d2 = if (c2.out == b2.a) byOut.getValue(b2.b) else byOut.getValue(b2.a)
      if (d2.exp != "AND" || d2.a !in lastInputs || d2.b !in lastInputs) {
        set += d2.out
        continue
      }
    }

    return set
  }

  private fun trackDependencies(key: String): Set<String> = buildSet {
    add(key)

    if (!key.startsWith('x') && !key.startsWith('y')) {
      val func = byOut.getValue(key)
      addAll(trackDependencies(func.a))
      addAll(trackDependencies(func.b))
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
