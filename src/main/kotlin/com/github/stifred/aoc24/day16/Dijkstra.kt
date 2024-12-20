package com.github.stifred.aoc24.day16

import com.github.stifred.aoc24.shared.*
import com.github.stifred.aoc24.shared.PositionWithDirection.Companion.towards
import com.github.stifred.aoc24.shared.search.SearchStateWithKey
import com.github.stifred.aoc24.shared.search.dijkstra

val day16 = solution(day = 16) {
  val maze = parseInput { it.asDijkstraMaze() }

  part1 { maze.lowestScore }
  part2 { maze.goodSittingPlaces }
}

class DijkstraMaze(
  val vertices: Set<Position>,
  val start: Position,
  val end: Position,
) {
  private val search = dijkstra(Reindeer::class)
  private val finishingReindeer: List<Reindeer> get() {
    search.reset()
    search.continueWith(Reindeer(position = start))

    var bestScore = Int.MAX_VALUE
    return search.findAll { reindeer ->
      val (position, direction, score, turnPositions) = reindeer

      if (score > bestScore) {
        skip()
      }


      if (position == end) {
        bestScore = score
        found(reindeer.copy(turnPositions = turnPositions + end))
      } else {
        val next = position.move(direction)
        if (next in vertices) {
          continueWith(reindeer.copy(position = next, score = score + 1))
        }

        for (turn in sequenceOf(direction.hardLeft(), direction.hardRight())) {
          val nextAfterTurn = position.move(turn)
          if (nextAfterTurn in vertices) {
            val afterTurn = reindeer.copy(
              position = nextAfterTurn,
              direction = turn,
              turnPositions = turnPositions + position,
              score = score + 1001,
            )

            continueWith(afterTurn)
          }
        }
      }
    }
  }

  val lowestScore get() = finishingReindeer.first().score
  val goodSittingPlaces get() = finishingReindeer.flatMap { it.touchedFrom(start) }.distinct().count()

  data class Reindeer(
    val position: Position,
    val direction: Direction = Direction.Right,
    val score: Int = 0,
    val turnPositions: List<Position> = emptyList(),
  ) : SearchStateWithKey<PositionWithDirection> {
    override val key: PositionWithDirection = position towards direction
    override val cost: Int = score

    fun touchedFrom(start: Position) = (sequenceOf(start) + turnPositions).windowed(2).flatMap { (a, b) ->
      Position.between(a, b)
    }.toSet()
  }
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
