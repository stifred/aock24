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
      max = Position(x = lines[0].length - 1, y = lines.size - 1),
    )
  }

  part1 { map.simulatePatrol().size }
  part2 { map.findExtraObstructionPositions().size }
}

data class Map(val guard: Guard, val obstructions: List<Obstruction>, val max: Position) {
  private val min = Position(0, 0)

  fun simulatePatrol(): Set<Position> = buildSet {
    val patroller = guard.copy()

    add(patroller.pos)

    var health = (max.x * max.y)
    while (health >= 0) {
      val next = patroller.pos.move(patroller.dir)
      if (next.isObstructed()) {
        patroller.dir = patroller.dir.hardRight()
      } else {
        patroller.pos = next
      }

      if (patroller.pos.isWithin(min, max)) {
        add(patroller.pos)

        if (--health <= 0) clear()
      } else break
    }
  }

  fun findExtraObstructionPositions(): Set<Position> =
    simulatePatrol().asSequence()
      .filter { copy(obstructions = obstructions + Obstruction(it)).simulatePatrol().isEmpty() }
      .toSet()

  private fun Position.isObstructed() = obstructions.any { it.pos == this }
}

data class Guard(var pos: Position, var dir: Direction)

data class Obstruction(val pos: Position)
