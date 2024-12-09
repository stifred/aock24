package com.github.stifred.aoc24.ninth

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DiskMapTest {
  @Test
  fun `parse and rearrange`() {
    val file0 = File(0, 1)
    val file1 = File(1, 3)
    val file2 = File(2, 5)

    val expectedBefore = buildList {
      repeat(1) { add(file0) }
      repeat(2) { add(FreeSpace) }
      repeat(3) { add(file1) }
      repeat(4) { add(FreeSpace) }
      repeat(5) { add(file2) }
    }
    val actualBefore = "12345".toDiskMap()
    assertEquals(expectedBefore, actualBefore)

    val expectedAfter = buildList {
      repeat(1) { add(file0) }
      repeat(2) { add(file2) }
      repeat(3) { add(file1) }
      repeat(3) { add(file2) }
      repeat(6) { add(FreeSpace) }
    }
    val actualAfter = actualBefore.rearranged()
    assertEquals(expectedAfter, actualAfter)
  }
}
