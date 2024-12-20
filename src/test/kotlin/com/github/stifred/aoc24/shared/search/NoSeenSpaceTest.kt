package com.github.stifred.aoc24.shared.search

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NoSeenSpaceTest {
  object SomeState : SearchState {
    override val cost: Int get() = 1
  }

  @Test
  fun `no seen space`() {
    val nss = NoSeenSpace<SomeState>()

    assertFalse { nss.hasSeen(SomeState) }
    assertTrue { nss.tryMarkSeen(SomeState) }
    assertFalse { nss.hasSeen(SomeState) }
  }
}