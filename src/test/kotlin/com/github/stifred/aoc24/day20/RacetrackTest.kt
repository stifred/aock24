package com.github.stifred.aoc24.day20

import com.github.stifred.aoc24.day16.asDijkstraMaze
import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RacetrackTest {
  @Test
  fun `basic racetrack`() {
    val track = """
      ###############
      #...#...#.....#
      #.#.#.#.#.###.#
      #S#...#.#.#...#
      #######.#.#.###
      #######.#.#...#
      #######.#.###.#
      ###..E#...#...#
      ###.#######.###
      #...###...#...#
      #.#####.#.###.#
      #.#...#.#.#...#
      #.#.#.#.#.#.###
      #...#...#...###
      ###############
    """.trimIndent().asDijkstraMaze().asRacetrack()

    assertEquals(85, track.steps.size)
    assertEquals(Position(1, 3), track.start)
    assertEquals(Position(5, 7), track.end)

    val cheats = track.cheats(2, 2).toSet()

    assertEquals(44, cheats.size)
    assertEquals(14, cheats.count { it.saving == 2 })
    assertEquals(14, cheats.count { it.saving == 4 })
    assertEquals(2, cheats.count { it.saving == 6 })
    assertEquals(4, cheats.count { it.saving == 8 })
    assertEquals(2, cheats.count { it.saving == 10 })
    assertEquals(3, cheats.count { it.saving == 12 })
    assertEquals(1, cheats.count { it.saving == 20 })
    assertEquals(1, cheats.count { it.saving == 36 })
    assertEquals(1, cheats.count { it.saving == 38 })
    assertEquals(1, cheats.count { it.saving == 40 })
    assertEquals(1, cheats.count { it.saving == 64 })

    val sc = track.cheats(20, 50).toSet()

    assertEquals(0, sc.count { it.saving < 50 })
    assertEquals(32, sc.count { it.saving == 50 })
    assertEquals(31, sc.count { it.saving == 52 })
    assertEquals(29, sc.count { it.saving == 54 })
    assertEquals(39, sc.count { it.saving == 56 })
    assertEquals(25, sc.count { it.saving == 58 })
    assertEquals(23, sc.count { it.saving == 60 })
    assertEquals(20, sc.count { it.saving == 62 })
    assertEquals(19, sc.count { it.saving == 64 })
    assertEquals(12, sc.count { it.saving == 66 })
    assertEquals(14, sc.count { it.saving == 68 })
    assertEquals(12, sc.count { it.saving == 70 })
    assertEquals(22, sc.count { it.saving == 72 })
    assertEquals(4, sc.count { it.saving == 74 })
    assertEquals(3, sc.count { it.saving == 76 })
    assertEquals(0, sc.count { it.saving > 76 })
  }
}