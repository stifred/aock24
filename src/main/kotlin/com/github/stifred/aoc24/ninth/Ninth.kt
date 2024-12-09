package com.github.stifred.aoc24.ninth

import com.github.stifred.aoc24.shared.solution
import java.util.concurrent.atomic.AtomicInteger

val ninth = solution(day = 9) {
  val map = parseInput { it.toDiskMap() }

  part1 { map.rearranged().checksum() }
  part2 { map.rearrangedWithoutFileSplits().checksum() }
}

sealed class DiskMapEntry
data class File(val id: Int, val size: Int) : DiskMapEntry()
data object FreeSpace : DiskMapEntry()

typealias DiskMap = List<DiskMapEntry>

fun DiskMap.rearranged(): DiskMap {
  val dm = this

  var offset = size - 1
  val freeSpaces = asSequence().filterIsInstance<FreeSpace>().count()

  return buildList {
    for ((i, entry) in dm.withIndex()) {
      if (entry is FreeSpace) {
        while (offset >= i) {
          val atEnd = dm[offset]
          offset--

          if (atEnd is File) {
            add(atEnd)
            break
          }
        }
      } else if (i <= offset) {
        add(entry)
      }
    }

    repeat(freeSpaces) { add(FreeSpace) }
  }
}

fun DiskMap.rearrangedWithoutFileSplits(): DiskMap {
  val copy = ArrayList(this)
  val files = asSequence().filterIsInstance<File>().distinct().toList().reversed()

  for (file in files) {
    val range = copy.freeSpaceFor(file) ?: continue
    val index = copy.indexOf(file)
    if (range.first >= index) continue

    for (i in range) {
      copy.removeAt(i)
      copy.add(i, file)
    }
    for (i in index until (index + file.size)) {
      copy.removeAt(i)
      copy.add(i, FreeSpace)
    }
  }

  return copy
}

private fun List<DiskMapEntry>.freeSpaceFor(file: File): IntRange? {
  var from = 0
  for (i in indices) {
    if (this[i] is File) from = i + 1
    else if (i >= from + file.size - 1) {
      return from..i
    }
  }

  return null
}

fun DiskMap.checksum() = foldIndexed(0L) { blockPos, sum, entry ->
  when (entry) {
    is File -> sum + (blockPos * entry.id)
    is FreeSpace -> sum
  }
}

fun String.toDiskMap(): DiskMap = buildList {
  val id = AtomicInteger(0)
  this@toDiskMap.forEachIndexed { ci, c ->
    val size = c.digitToInt()
    val entry = if (ci % 2 == 0) File(id.getAndIncrement(), size) else FreeSpace

    repeat(size) { add(entry) }
  }
}
