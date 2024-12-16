package com.github.stifred.aoc24.day16

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.PositionWithDirection
import com.github.stifred.aoc24.shared.PositionWithDirection.Companion.towards
import com.github.stifred.aoc24.shared.solution

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
  val lowestScore get() = sptSet.filter { it.key.pos == end }.minOf { it.value }
  val goodSittingPlaces get(): Int {
    val lastPosWithDir = sptSet.filter { it.key.pos == end }.minBy { it.value }.key

    return buildSet {
      val toCheck = ArrayDeque(setOf(lastPosWithDir))
      add(end)

      while (toCheck.isNotEmpty()) {
        val pwd = toCheck.removeFirst()
        val score = sptSet.getValue(pwd)
        val (pos, dir) = pwd

        val previousPos = pos.move(dir.opposite())
        val previousCandidates = sptSet.filter { (pwd) -> pwd.pos == previousPos }

        for ((prevPwd, prevScore) in previousCandidates) {
          val (prevPos, prevDir) = prevPwd

          if (prevDir == dir && prevScore == score - 1
            || prevDir != dir && prevScore == score - 1001) {
            add(prevPos)
            toCheck.addLast(prevPos towards prevDir)
          }
        }
      }
    }.size
  }

  private val sptSet: Map<PositionWithDirection, Int> by lazy {
    buildMap {
      put(start towards Direction.Right, 0)

      fun getScore(pwd: PositionWithDirection) = this[pwd] ?: Int.MAX_VALUE

      val toCheck = mutableSetOf(start towards Direction.Right)

      while (true) {
        val pwd = toCheck.minByOrNull { getScore(it) } ?: break
        toCheck.remove(pwd)

        val next = pwd.pos.move(pwd.dir) towards pwd.dir
        val valueAfterNext = getScore(pwd) + 1
        if (next.pos in vertices && getScore(next) > valueAfterNext) {
          put(next, getValue(pwd) + 1)
          if (next.pos == end) break

          toCheck += next
        }

        for (turn in sequenceOf(pwd.dir.hardLeft(), pwd.dir.hardRight())) {
          val nextAfterTurn = pwd.pos.move(turn) towards turn
          val valueAfterTurn = getScore(pwd) + 1001
          if (nextAfterTurn.pos in vertices && getScore(nextAfterTurn) > valueAfterTurn) {
            put(nextAfterTurn, valueAfterTurn)
            if (nextAfterTurn.pos == end) break

            toCheck += nextAfterTurn
          }
        }
      }
    }
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
