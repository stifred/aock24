package com.github.stifred.aoc24.day21

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class KeypadTest {
  @Test
  fun `movementBetween - example 1`() {
    val expected = listOf(Button.Down, Button.Down, Button.Right, Button.Right)
    val actual = Keypad.Numeric.movementBetween(Button.Seven, Button.Three)

    assertEquals(expected, actual)
  }

  @Test
  fun `movementBetween - example 2`() {
    val expected = listOf(Button.Right, Button.Up)
    val actual = Keypad.Directional.movementBetween(Button.Left, Button.Up)

    assertEquals(expected, actual)
  }

  @Test
  fun `movementBetween - example 3`() {
    val expected = listOf(Button.Right, Button.Down, Button.Down, Button.Down)
    val actual = Keypad.Numeric.movementBetween(Button.Seven, Button.Zero)

    assertEquals(expected, actual)
  }
}
