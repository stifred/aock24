package com.github.stifred.aoc24.day20

import com.github.stifred.aoc24.day16.DijkstraMaze
import com.github.stifred.aoc24.day16.asDijkstraMaze
import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.search.SearchStateWithKey
import com.github.stifred.aoc24.shared.search.dfs
import com.github.stifred.aoc24.shared.solution

val day20 = solution(day = 20) {
  val track = parseInput { it.asDijkstraMaze().asRacetrack() }

  part1 { track.cheats(2, 100).count() }
  part2 { track.cheats(20, 100).count() }
}

data class Racetrack(val steps: List<Position>) {
  val start = steps.first()
  val end = steps.last()

  fun cheats(length: Int, threshold: Int) = sequence {
    for ((fromPico, from) in steps.subList(0, steps.size - threshold).withIndex()) {
      for ((i, to) in steps.subList(fromPico + threshold, steps.size).withIndex()) {
        val deltaPico = i + threshold
        val md = to manhattanTo from
        if (md > length || md > deltaPico - threshold) continue

        yield(Cheat(start = from, end = to, saving = deltaPico - md))
      }
    }
  }
}

data class Cheat(val start: Position, val end: Position, val saving: Int)

fun DijkstraMaze.asRacetrack(): Racetrack {
  data class Path(val positions: List<Position>): SearchStateWithKey<Position> {
    override val key get() = positions.last()
  }

  val dfs = dfs(Path::class).apply { push(Path(listOf(start))) }
  val path = dfs.find { path ->
    val current = path.key
    if (current == end) found(path)

    for (dir in Direction.nonDiagonals) {
      val next = current.move(dir)
      if (next in vertices && next !in path.positions) {
        continueWith(Path(positions = path.positions + next))
        break
      }
    }
  } ?: error("Path not found")

  return Racetrack(path.positions)
}
