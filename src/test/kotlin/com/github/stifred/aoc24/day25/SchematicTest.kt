package com.github.stifred.aoc24.day25

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SchematicTest {
  @Test
  fun `parsing first lock`() {
    val expected = Schematic(SchematicKind.Lock, listOf(0, 5, 3, 4, 3))
    val actual = """
      #####
      .####
      .####
      .####
      .#.#.
      .#...
      .....
    """.trimIndent().toSchematic()

    assertEquals(expected, actual)
  }

  @Test
  fun `parsing one key`() {
    val expected = Schematic(SchematicKind.Key, listOf(5, 0, 2, 1, 3))
    val actual = """
      .....
      #....
      #....
      #...#
      #.#.#
      #.###
      #####
    """.trimIndent().toSchematic()

    assertEquals(expected, actual)
  }

  @Test
  fun `the test data`() {
    val list = """
      #####
      .####
      .####
      .####
      .#.#.
      .#...
      .....

      #####
      ##.##
      .#.##
      ...##
      ...#.
      ...#.
      .....

      .....
      #....
      #....
      #...#
      #.#.#
      #.###
      #####

      .....
      .....
      #.#..
      ###..
      ###.#
      ###.#
      #####

      .....
      .....
      .....
      #....
      #.#..
      #.#.#
      #####
    """.trimIndent().toSchematicList()

    assertEquals(5, list.size)
    assertEquals(3, list.countCombos())
  }
}
