package com.github.stifred.aoc24.sixth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val sixth = solution(day = 6) {
  val map = parseInput { raw ->
    val guards = mutableListOf<Guard>()
    val obstructions = mutableListOf<Obstruction>()
    val lines = raw.lines()

    lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, char ->
        val pos = Position(x, y)

        when (char) {
          '#' -> obstructions += Obstruction(pos)
          '^' -> guards += Guard(pos, Direction.Up)
          '>' -> guards += Guard(pos, Direction.Right)
          'v' -> guards += Guard(pos, Direction.Down)
          '<' -> guards += Guard(pos, Direction.Left)
        }
      }
    }

    Map(
      guard = guards.first(),
      obstructions = obstructions,
      max = Position(x = lines[0].length - 1, y = lines.size - 1)
    )
  }

  part1 { map.simulatePatrol().size }
}

data class Map(
  val guard: Guard,
  val obstructions: List<Obstruction>,
  val max: Position,
) {
  private val min = Position(0, 0)

  fun simulatePatrol(): Set<Position> = buildSet {
    val startPos = guard.pos
    val startDir = guard.dir

    add(startPos)

    while (true) {
      val next = guard.pos.move(guard.dir)
      if (next.isObstructed()) {
        guard.dir = guard.dir.hardRight()
      } else {
        guard.pos = next
      }

      if (guard.pos.isWithin(min, max)) {
        add(guard.pos)
      } else {
        guard.pos = startPos
        guard.dir = startDir
        break
      }
    }
  }

  private fun Position.isObstructed() = obstructions.any { it.pos == this }
}

data class Guard(var pos: Position, var dir: Direction)

data class Obstruction(val pos: Position)
