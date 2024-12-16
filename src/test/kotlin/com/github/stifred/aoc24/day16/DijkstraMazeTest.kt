package com.github.stifred.aoc24.day16

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DijkstraMazeTest {
  @Test
  fun `smaller maze`() {
    val maze = """
      ###############
      #.......#....E#
      #.#.###.#.###.#
      #.....#.#...#.#
      #.###.#####.#.#
      #.#.#.......#.#
      #.#.#####.###.#
      #...........#.#
      ###.#.#####.#.#
      #...#.....#.#.#
      #.#.#.###.#.#.#
      #.....#...#.#.#
      #.###.#.#.#.#.#
      #S..#.....#...#
      ###############
    """.trimIndent().asDijkstraMaze()

    assertEquals(7036, maze.lowestScore)
    assertEquals(45, maze.goodSittingPlaces)
  }
}