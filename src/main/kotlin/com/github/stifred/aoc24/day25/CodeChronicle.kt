package com.github.stifred.aoc24.day25

import com.github.stifred.aoc24.shared.solution

val codeChronicle = solution(day = 25) {
  val list = parseInput { it.toSchematicList() }

  part1 { list.countCombos() }
}

data class Schematic(
  val kind: SchematicKind,
  val columns: List<Int>,
) {
  fun fitWith(other: Schematic) =
    (kind != other.kind) && (columns.indices.none { columns[it] + other.columns[it] > 5 })
}

enum class SchematicKind { Key, Lock }

fun List<Schematic>.countCombos(): Int {
  val keys = filter { it.kind == SchematicKind.Key }.toSet()
  val locks = filter { it.kind == SchematicKind.Lock }.toSet()

  var count = 0
  for (key in keys) {
    count += locks.count { it.fitWith(key) }
  }

  return count
}

fun String.toSchematic(): Schematic {
  val kind = if (first() == '.') SchematicKind.Key else SchematicKind.Lock
  val columns = mutableListOf(0, 0, 0, 0, 0)
  val lines = lines().drop(1).reversed().drop(1)

  for (line in lines) {
    for ((x, char) in line.withIndex()) {
      if (char == '#') {
        columns[x] += 1
      }
    }
  }

  return Schematic(kind, columns)
}

fun String.toSchematicList() = splitToSequence("\n\n")
  .filter { it.isNotBlank() }
  .map { it.toSchematic() }
  .toList()
