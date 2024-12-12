package com.github.stifred.aoc24.twelfth

import com.github.stifred.aoc24.twelfth.PlantMap.Companion.asPlantMap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PlantMapTest {
  @Test
  fun `tiny map`() {
    val map = """
      AAAA
      BBCD
      BBCC
      EEEC
    """.trimIndent().asPlantMap()

    val regions = map.asRegions()

    assertEquals(5, regions.size)
    assertEquals(140, regions.totalPrice)
    assertEquals(80, regions.totalDiscountedPrice)
  }

  @Test
  fun `oxo map`() {
    val map = """
      OOOOO
      OXOXO
      OOOOO
      OXOXO
      OOOOO
    """.trimIndent().asPlantMap()

    val regions = map.asRegions()

    assertEquals(5, regions.size)
    assertEquals(772, regions.totalPrice)
    assertEquals(436, regions.totalDiscountedPrice)
  }

  @Test
  fun `bigger map`() {
    val map = """
      RRRRIICCFF
      RRRRIICCCF
      VVRRRCCFFF
      VVRCCCJFFF
      VVVVCJJCFE
      VVIVCCJJEE
      VVIIICJJEE
      MIIIIIJJEE
      MIIISIJEEE
      MMMISSJEEE
    """.trimIndent().asPlantMap()

    val regions = map.asRegions()

    assertEquals(11, regions.size)
    assertEquals(1930, regions.totalPrice)
    assertEquals(1206, regions.totalDiscountedPrice)
  }

  @Test
  fun `exe map`() {
    val map = """
      EEEEE
      EXXXX
      EEEEE
      EXXXX
      EEEEE
    """.trimIndent().asPlantMap()

    val regions = map.asRegions()

    val e = regions.first { it.name == 'E' }
    assertEquals(17, e.area)
    assertEquals(12, e.sideCount)

    assertEquals(236, regions.totalDiscountedPrice)
  }

  @Test
  fun `abba map`() {
    val map = """
      AAAAAA
      AAABBA
      AAABBA
      ABBAAA
      ABBAAA
      AAAAAA
    """.trimIndent().asPlantMap()

    val regions = map.asRegions()

    val a = regions.first { it.name == 'A' }
    assertEquals(28, a.area)
    assertEquals(12, a.sideCount)

    assertEquals(368, regions.totalDiscountedPrice)
  }
}
