package com.github.stifred.aoc24.tenth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val tenth = solution(day = 10) {
  val map = parseInput { it.toTrailMap() }

  part1 { map.score }
  part2 { map.sumOfRatings }
}

data class TrailMap(private val chars: List<Char>, private val width: Int) {
  private val height = chars.size / width

  val score get(): Int = trailHeads.sumOf { (i, c) -> scan(i.toPos(), c).distinct().count() }
  val sumOfRatings get(): Int = trailHeads.sumOf { (i, c) -> scan(i.toPos(), c).count() }

  private val trailHeads get() = chars.withIndex().asSequence().filter { (_, c) -> c == START }

  private fun scan(position: Position, value: Char): Sequence<Position> =
    position.toIndexOrNull()?.let { index ->
      when {
        chars[index] != value -> sequenceOf()
        value == END -> sequenceOf(position)
        else -> Direction.nonDiagonals.asSequence().flatMap { scan(position.move(it), value + 1) }
      }
    } ?: sequenceOf()

  private fun Int.toPos() = Position(this % width, this / width)
  private fun Position.toIndexOrNull() = if (x in 0 until width && y in 0 until height) ((y * width) + x) else null

  companion object {
    const val START = '0'
    const val END = '9'
  }
}

fun String.toTrailMap() = TrailMap(
  chars = filter { it != '\n' }.toList(),
  width = indexOf('\n'),
)
