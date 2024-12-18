package com.github.stifred.aoc24.shared

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DijkstraTest {
  @Test
  fun `performance test`() {
    val start = Position(0, 0)
    val end = Position(9, 9)
    val d = Dijkstra<Position, Unit> { pos, _ ->
      Direction.nonDiagonals.asSequence()
        .map { pos.move(it) }
        .filter { (x, y) -> (x in 0..9 && y in 0..9) && !(x in 1..8 && y in 1..8) }
        .forEach { add(Step(it, Unit, 10)) }
    }

    val paths = d.bestPathsBetween(start, end, Unit)

    assertEquals(2, paths.size)
  }
}