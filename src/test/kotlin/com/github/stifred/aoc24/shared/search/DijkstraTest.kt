package com.github.stifred.aoc24.shared.search

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DijkstraTest {
  data class CharState(val ch: Char, override val cost: Int) : SearchState

  @Test
  fun `priority queue`() {
    val queue = Dijkstra<CharState>()
    assertFalse { queue.hasNext() }
    assertThrows<NullPointerException> { queue.next() }

    queue.push(CharState('a', 1950))
    queue.push(CharState('b', 2500))
    queue.push(CharState('c', 1431))

    assertTrue { queue.hasNext() }
    assertEquals(CharState('c', 1431), queue.next())
    assertTrue { queue.hasNext() }
    assertEquals(CharState('a', 1950), queue.next())
    assertTrue { queue.hasNext() }
    assertEquals(CharState('b', 2500), queue.next())
    assertFalse { queue.hasNext() }
  }
}
