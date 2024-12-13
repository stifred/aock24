package com.github.stifred.aoc24.thirteenth

import com.github.stifred.aoc24.shared.LongPosition
import com.github.stifred.aoc24.shared.solution
import kotlin.math.abs
import kotlin.math.roundToLong

val thirteenth = solution(day = 13) {
  val machines = parseInput { it.toMachines() }

  part1 {
    machines.sumOf { it.minimumTokensOrNullFast() ?: 0 }
  }

  part2 {
    machines.sumOf { it.getReal().minimumTokensOrNullFast() ?: 0 }
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

    // If B and A are whole numbers, then a prize can be won.

    val B = (i - (j * x / y)) / (X - (Y * x / y))
    val bCount = B.roundToLong()
    if (abs(bCount - B) > 0.001) return null

    val A = (j/y) - ((B * Y)/y)
    val aCount = A.roundToLong()
    if (abs(aCount - A) > 0.001) return null

    return (aCount * 3) + bCount
  }
}

fun String.toMachines() = split("\n\n").map(String::toMachine)

fun String.toMachine() = lines().let { (l1, l2, l3) ->
  Machine(a = l1.toPosition(12), b = l2.toPosition(12), prize = l3.toPosition(9))
}

private fun String.toPosition(offset: Int) = LongPosition(
  x = substring(offset, indexOf(',')).toLong(),
  y = substring(indexOf("Y") + 2).toLong(),
)
