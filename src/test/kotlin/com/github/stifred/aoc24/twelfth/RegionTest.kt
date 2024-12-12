package com.github.stifred.aoc24.twelfth

import com.github.stifred.aoc24.shared.Position
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegionTest {
  @Test
  fun `test region A`() {
    val region = Region('A', setOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)))

    assertEquals(4, region.area)
    assertEquals(10, region.perimeter)
    assertEquals(40, region.price)
    assertEquals(4, region.sideCount)
    assertEquals(16, region.discountedPrice)
  }

  @Test
  fun `test region B`() {
    val region = Region('B', setOf(Position(0, 0), Position(1, 0), Position(0, 1), Position(1, 1)))

    assertEquals(4, region.area)
    assertEquals(8, region.perimeter)
    assertEquals(32, region.price)
    assertEquals(4, region.sideCount)
    assertEquals(16, region.discountedPrice)
  }

  @Test
  fun `test region C`() {
    val region = Region('C', setOf(Position(0, 0), Position(0, 1), Position(1, 1), Position(1, 2)))

    assertEquals(4, region.area)
    assertEquals(10, region.perimeter)
    assertEquals(40, region.price)
    assertEquals(8, region.sideCount)
    assertEquals(32, region.discountedPrice)
  }

  @Test
  fun `test region D`() {
    val region = Region('D', setOf(Position(0, 0)))

    assertEquals(1, region.area)
    assertEquals(4, region.perimeter)
    assertEquals(4, region.price)
    assertEquals(4, region.sideCount)
    assertEquals(4, region.discountedPrice)
  }

  @Test
  fun `test region E`() {
    val region = Region('E', setOf(Position(3, 3), Position(4, 3), Position(5, 3)))

    assertEquals(3, region.area)
    assertEquals(8, region.perimeter)
    assertEquals(24, region.price)
    assertEquals(4, region.sideCount)
    assertEquals(12, region.discountedPrice)
  }

  @Test
  fun `test region O`() {
    val region = Region('O', buildSet {
      for (x in 1..5) {
        for (y in 1..5) {
          if (x !in arrayOf(2, 4) || y !in arrayOf(2, 4)) {
            add(Position(x, y))
          }
        }
      }
    })

    assertEquals(21, region.area)
    assertEquals(36, region.perimeter)
    assertEquals(756, region.price)
  }
}
