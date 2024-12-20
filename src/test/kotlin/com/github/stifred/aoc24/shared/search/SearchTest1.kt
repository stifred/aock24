package com.github.stifred.aoc24.shared.search

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class SearchTest {
  data class PositionState(val pos: Position) : SearchState

  @Test
  fun `test search performance`() {
    val start = Position(0, 0)
    val end = Position(999, 999)

    val search = dfs<PositionState>().apply { push(PositionState(Position(0, 0))) }

    val f = search.findAll { state ->
      if (state.pos == end) {
        found(state)
      }

      for (dir in Direction.nonDiagonals) {
        val next = state.pos.move(dir)
        if (next.isWithin(start, end)) {
          push(state.copy(pos = next))
        }
      }
    }

    assertTrue { f.isNotEmpty() }
  }
}