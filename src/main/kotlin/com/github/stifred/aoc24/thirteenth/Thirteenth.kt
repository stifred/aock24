package com.github.stifred.aoc24.thirteenth

import com.github.stifred.aoc24.shared.LongPosition
import com.github.stifred.aoc24.shared.solution
import kotlin.math.roundToLong

val thirteenth = solution(day = 13) {
  val machines = parseInput { it.toMachines() }

  part1 {
    machines.sumOf { it.minimumTokensOrNullFast() ?: 0 }
  }

  part2 {
    machines.asSequence()
      .map { it.getReal() }
      .mapNotNull { it.minimumTokensOrNullFast() }
      .sum()
  }
}

data class Machine(
  val a: LongPosition,
  val b: LongPosition,
  val prize: LongPosition,
) {
  fun getReal() = copy(prize = prize + LongPosition(10000000000000, 10000000000000))

  fun minimumTokensOrNullFast(): Long? {
    // I'm keeping this variable names, since I used those to solve for two unknowns in Gedit.

    val x = a.x.toDouble()
    val y = a.y.toDouble()
    val X = b.x.toDouble()
    val Y = b.y.toDouble()
    val i = prize.x.toDouble()
    val j = prize.y.toDouble()

    val B = (i - (j * x / y)) / (X - (Y * x / y))
    val A = (j/y) - ((B * Y)/y)

    val aCount = A.roundToLong()
    val bCount = B.roundToLong()

    return if (a * aCount + b * bCount == prize) (aCount * 3) + bCount else null
  }

  fun minimumTokensOrNullSlow(): Long? {
    // This was my original solution that worked for part 1, but was too slow for part 2.

    val maxA = maxOf(prize.x / a.x, prize.y / a.y)
    val maxB = maxOf(prize.x / b.x, prize.y / b.y)

    for (aCount in 0..maxA) {
      for (bCount in 0..maxB) {
        val current = a * aCount + b * bCount

        if (current == prize) {
          return (aCount * 3) + bCount
        }

        if (current.x > prize.x || current.y > prize.y) {
          break
        }
      }
    }

    return null
  }
}

fun String.toMachines() = split("\n\n").map { it.toMachine() }

fun String.toMachine(): Machine {
  val (l1, l2, l3) = lines()

  return Machine(
    a = LongPosition(
      x = l1.substring(l1.indexOf("A: X+") + 5, l1.indexOf(",")).toLong(),
      y = l1.substring(l1.indexOf(", Y+") + 4).toLong(),
    ),
    b = LongPosition(
      x = l2.substring(l2.indexOf("B: X+") + 5, l2.indexOf(",")).toLong(),
      y = l2.substring(l2.indexOf(", Y+") + 4).toLong(),
    ),
    prize = LongPosition(
      x = l3.substring(9, l3.indexOf(", ")).toLong(),
      y = l3.substring(l3.indexOf(", Y=") + 4).toLong(),
    ),
  )
}
