package com.github.stifred.aoc24.twelfth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FenceTest {
  @Test
  fun `merge to above`() {
    val fence = Region.Fence(Direction.Up, mutableSetOf(Position(5, 5)))
    val left = Region.Fence(Direction.Up, mutableSetOf(Position(4, 5)))
    val right = Region.Fence(Direction.Up, mutableSetOf(Position(6, 5)))
    assertTrue { fence.attemptMerge(left) }
    assertTrue { fence.attemptMerge(right) }

    val otherDirection = Region.Fence(Direction.Left, mutableSetOf(Position(4, 5)))
  }
}