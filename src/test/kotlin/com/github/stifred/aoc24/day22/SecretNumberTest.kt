package com.github.stifred.aoc24.day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SecretNumberTest {
  @Test
  fun `mixing works`() = assertEquals(37, 42 xor 15)

  @Test
  fun `pruning works`() = assertEquals(16113920, 100_000_000 % 16_777_216)

  @Test
  fun `next tests to preserve sanity`() {
    assertEquals(15887950, 123L.next())
  }

  @Test
  fun `sequence of numbers`() {
    val expected = listOf<SecretNumber>(
      15887950,
      16495136,
      527345,
      704524,
      1553684,
      12683156,
      11100544,
      12249484,
      7753432,
      5908254,
    )
    val actual = 123L.sequence().drop(1).take(10).toList()

    assertEquals(expected, actual)
  }

  @Test
  fun `price sequence works as expected`() {
    val expected = listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2)
    val actual = (123L).priceSequence().take(10).toList()

    assertEquals(expected, actual)
  }

  @Test
  fun `best changes`() {
    val bestChanges = (123L).priceByDiffs()
    assertEquals(6, bestChanges[PriceDiffs(-1, -1, 0, 2)])
  }

  @Test
  fun `unified best changes`() {
    val (first, second, fourth, third) = listOf<SecretNumber>(1, 2, 2024, 3).priceByDiffs()
    val priceDiffs = PriceDiffs(-2, 1, -1, 3)

    assertEquals(7, first[priceDiffs])
    assertEquals(7, second[priceDiffs])
    assertEquals(9, fourth[priceDiffs])
    assertEquals(null, third[priceDiffs])
  }
}
