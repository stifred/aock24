package com.github.stifred.aoc24.day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WarehouseTest {
  @Test
  fun `smaller warehouse`() {
    val before = """
      ########
      #..O.O.#
      ##@.O..#
      #...O..#
      #.#.O..#
      #...O..#
      #......#
      ########

      <^^>>>vv<v>>v<<
    """.trimIndent().asWarehouse()
    val after = before.withAllTicks()

    assertEquals(2028, after.gpsSum)
  }

  @Test
  fun `smaller wider warehouse`() {
    val original = """
      #######
      #...#.#
      #.....#
      #..OO@#
      #..O..#
      #.....#
      #######

      <vv<<^^<<^^
    """.trimIndent().asWarehouse()
    val widerBefore = """
      ##############
      ##......##..##
      ##..........##
      ##....[][]@.##
      ##....[]....##
      ##..........##
      ##############

      <vv<<^^<<^^
    """.trimIndent().asWarehouse()

    assertEquals(widerBefore, original.widen())

    val widerAfter = widerBefore.withAllTicks()

    assertEquals(105 + 207 + 306, widerAfter.gpsSum)
  }
}