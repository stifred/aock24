package com.github.stifred.aoc24.day18

import com.github.stifred.aoc24.shared.*
import com.github.stifred.aoc24.shared.search.SearchStateWithKey
import com.github.stifred.aoc24.shared.search.dijkstra

val day18 = solution(day = 18) {
  val memSpace = parseInput { it.asMemorySpace() }

  part1 { memSpace.after(1024).shortestPathLength!! }
  part2 {
    (1024 until memSpace.maxTime).asSequence()
      .map { memSpace.after(it) }
      .filter { it.shortestPathLength == null }
      .map { it.lastAddedByte }
      .map { "${it.x},${it.y}" }
      .first()
  }
}

data class MemorySpace(
  private val incomingBytes: List<Position>,
  private val nanos: Int = 0,
  private val exit: Position = Position(incomingBytes.maxOf(Position::x), incomingBytes.maxOf(Position::y))
) {
  private val fallen = incomingBytes.subList(0, nanos).toSet()

  fun after(nanos: Int) = copy(nanos = this.nanos + nanos)

  val shortestPathLength: Int? get() = floodFill()[topLeft]
  val maxTime get() = incomingBytes.size
  val lastAddedByte get() = incomingBytes[nanos - 1]

  private fun floodFill() = buildMap<Position, Int> {
    put(exit, 0)
    val toCheck = ArrayDeque(setOf(exit))

    while (toCheck.isNotEmpty()) {
      val current = toCheck.removeFirst()
      val currentScore = getValue(current).takeUnless { get(topLeft) != null } ?: continue

      for (dir in Direction.nonDiagonals) {
        val next = current.move(dir).takeIf { it.isSafe() } ?: continue
        val nextScore = get(next)

        if (nextScore == null || nextScore > currentScore + 1) {
          put(next, currentScore + 1)
          toCheck.addLast(next)
        }
      }
    }
  }

  private fun Position.isSafe() = isWithin(topLeft, exit) && this !in fallen

  data class PositionState(override val key: Position, override val cost: Int) : SearchStateWithKey<Position>

  companion object {
    private val topLeft: Position = Position(0, 0)
  }
}

fun String.asMemorySpace() = lineSequence().map(String::asPosition).toList().let { MemorySpace(it) }
