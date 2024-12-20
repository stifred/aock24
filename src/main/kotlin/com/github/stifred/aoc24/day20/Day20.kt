package com.github.stifred.aoc24.day20

import com.github.stifred.aoc24.day16.DijkstraMaze
import com.github.stifred.aoc24.day16.asDijkstraMaze
import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val day20 = solution(day = 20) {
  val track = parseInput { it.asDijkstraMaze().asRacetrack() }

  part1 { track.cheats(2, 100).distinct().count() }
  part2 { track.cheats(20, 100).distinct().count() }
}

data class Racetrack(val steps: List<Position>) {
  val start = steps.first()
  val end = steps.last()

  fun cheats(length: Int, threshold: Int) = sequence {
    for ((fromPico, from) in steps.withIndex()) {
      for ((i, to) in steps.subList(fromPico + 1, steps.size).withIndex()) {
        val deltaPico = i + 1
        val md = to manhattanTo from
        if (md > length || md > deltaPico - threshold) continue

        yield(Cheat(start = from, end = to, saving = deltaPico - md))
      }
    }
  }
}

data class Cheat(val start: Position, val end: Position, val saving: Int)

fun DijkstraMaze.asRacetrack() = buildList {
  var current = start
  add(current)
  do {
    for (dir in Direction.nonDiagonals) {
      val next = current.move(dir)
      if (next in vertices && next !in this) {
        current = next
        break
      }
    }
    add(current)
  } while (current != end)
}.let { Racetrack(it) }
