package com.github.stifred.aoc24.day16

import com.github.stifred.aoc24.shared.*
import com.github.stifred.aoc24.shared.PositionWithDirection.Companion.towards

val day16 = solution(day = 16) {
  val maze = parseInput { it.asDijkstraMaze() }

  part1 { maze.lowestScore }
  part2 { maze.goodSittingPlaces }
}

class DijkstraMaze(
  private val vertices: Set<Position>,
  private val start: Position,
  private val end: Position,
) {
  private val dijkstra = Dijkstra<Position, Direction> { pos, dir ->
    val next = pos.move(dir)
    if (next in vertices) {
      yield(Step(next, dir, 1))
    }

    for (turn in sequenceOf(dir.hardLeft(), dir.hardRight())) {
      val nextAfterTurn = pos.move(turn)
      if (nextAfterTurn in vertices) {
        yield(Step(nextAfterTurn, turn, 1001))
      }
    }
  }

  val lowestScore get() = dijkstra.bestPathsBetween(start, end, Direction.Right).first().totalCost
  val goodSittingPlaces get() = dijkstra.bestPathsBetween(start, end, Direction.Right).asSequence()
    .flatMap { it.steps }
    .map { it.vertex }
    .distinct()
    .count()
}

fun String.asDijkstraMaze(): DijkstraMaze {
  lateinit var start: Position
  lateinit var end: Position
  val vertices = buildSet {
    for ((y, line) in lines().withIndex()) {
      for ((x, chr) in line.withIndex()) {
        if (chr == '#') continue
        val pos = Position(x, y)

        if (chr == 'S') start = pos
        if (chr == 'E') end = pos

        add(pos)
      }
    }
  }

  return DijkstraMaze(vertices, start, end)
}
