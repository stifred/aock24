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
  private val trailHeads = chars.withIndex().filter { (_, c) -> c == START }

  val score get(): Int = trailHeads.sumOf { (i, c) -> scan(i.toPos(), c).distinct().count() }
  val sumOfRatings get(): Int = trailHeads.sumOf { (i, c) -> scan(i.toPos(), c).count() }

  private fun scan(position: Position, value: Char): Sequence<Position> =
    when {
      chars[position.toIndex()] != value -> sequenceOf()
      value == END -> sequenceOf(position)
      else -> Direction.nonDiagonals.asSequence().flatMap { scan(position.move(it), value + 1) }
    }

  private fun Int.toPos() = Position(this % width, this / width)
  private fun Position.toIndex() = (y * width) + x

  companion object {
    const val START = '0'
    const val END = '9'
  }
}

fun String.toTrailMap(): TrailMap {
  val width = indexOf('\n') + 2
  val chars = this

  return TrailMap(
    chars = buildList {
      repeat(width) { add('.') }
      for (char in chars) {
        if (char == '\n') {
          repeat(2) { add('.') }
        } else add(char)
      }
      repeat(width) { add('.') }
    },
    width = width,
  )
}
