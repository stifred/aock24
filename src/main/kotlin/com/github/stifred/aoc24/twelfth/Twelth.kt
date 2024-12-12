package com.github.stifred.aoc24.twelfth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution
import com.github.stifred.aoc24.twelfth.PlantMap.Companion.asPlantMap

val twelfth = solution(day = 12) {
  val map = parseInput { it.asPlantMap() }

  part1 { map.asRegions().totalPrice }
  part2 { map.asRegions().totalDiscountedPrice }
}

data class PlantMap(private val chars: List<Char>, private val width: Int) {
  fun asRegions(): List<Region> = buildList {
    for (char in chars.toSet()) {
      val positions = chars.withIndex()
        .filter { (_, c) -> c == char }
        .map { (i, _) -> Position(i % width, i / width) }
        .toMutableSet()

      while (positions.isNotEmpty()) {
        val inThisRegion = buildSet {
          add(positions.first())

          var toContinue: Boolean
          do {
            toContinue = false
            for (pos in positions) {
              if (any { pos touches it }) {
                add(pos)
                toContinue = true
              }
            }
            positions.removeAll(this)
          } while (toContinue)
        }

        add(Region(char, inThisRegion))
      }
    }
  }

  companion object {
    fun String.asPlantMap() = PlantMap(filter { it != '\n' }.toList(), indexOf('\n'))
  }
}

data class Region(val name: Char, private val positions: Set<Position>) {
  val area get() = positions.size

  val perimeter get() = positions.flatMap { it.fences() }.count()
  val price get() = area * perimeter

  val sideCount get(): Int = positions.flatMap { it.fences() }.mergeRepeatedly().count()
  val discountedPrice get() = area * sideCount

  private fun Position.fences() =
    Direction.nonDiagonals.filter { move(it) !in positions }.map { Fence(it, mutableSetOf(this)) }

  data class Fence(val direction: Direction, val positions: MutableSet<Position>) {
    fun attemptMerge(other: Fence): Boolean {
      if (direction != other.direction) return false

      for (dir in direction.normals()) {
        if (positions.any { it.move(dir) == other.positions.first() }) {
          positions += other.positions.first()
          return true
        }
      }

      return false
    }
  }

  private fun List<Fence>.mergeRepeatedly(): List<Fence> {
    var before = this
    var after = merge()
    while (before != after) {
      before = after
      after = after.map { it.copy(positions = HashSet(it.positions)) }.merge()
    }

    return after
  }

  private fun List<Fence>.merge() = buildList<Fence> {
    for (fence in this@merge) {
      if (none { it.attemptMerge(fence) }) add(fence)
    }
  }
}

val Collection<Region>.totalPrice get() = sumOf { it.price }
val Collection<Region>.totalDiscountedPrice get() = sumOf { it.discountedPrice }
