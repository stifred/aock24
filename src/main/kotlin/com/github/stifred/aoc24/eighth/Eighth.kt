package com.github.stifred.aoc24.eighth

import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val eighth = solution(day = 8) {
  val city = parseInput { it.toCity() }

  part1 { city.antinodes.size }
  part2 { city.harmonicAntinodes.size }
}

data class Antenna(val pos: Position, val frequency: Char)

data class Antinode(val pos: Position)

data class City(val antennas: List<Antenna>, val max: Position) {
  val min = Position(0, 0)

  val antennaPairs by lazy {
    antennas.flatMapIndexed { i, antenna ->
      antennas.asSequence()
        .drop(i + 1)
        .filter { it.frequency == antenna.frequency }
        .map { antenna to it }
    }
  }

  val antinodes by lazy {
    antennaPairs
      .flatMap { it.makeAntinodes() }
      .distinct()
  }

  val harmonicAntinodes by lazy {
    antennaPairs
      .flatMap { it.makeHarmonicAntinodes() }
      .distinct()
  }

  private fun Pair<Antenna, Antenna>.makeAntinodes(): Set<Antinode> {
    val diff = second.pos - first.pos

    return sequenceOf(first.pos - diff, second.pos + diff)
      .filter { it.isWithin(min, max) }
      .map { Antinode(it) }
      .toSet()
  }

  private fun Pair<Antenna, Antenna>.makeHarmonicAntinodes(): Set<Antinode> {
    val diff = second.pos - first.pos

    val backwards = sequence {
      var pos = second.pos
      while (true) {
        pos -= diff
        yield(pos)
      }
    }.takeWhile { it.isWithin(min, max) }

    val forwards = sequence {
      var pos = first.pos
      while (true) {
        pos += diff
        yield(pos)
      }
    }.takeWhile { it.isWithin(min, max) }

    return sequenceOf(backwards, forwards)
      .flatten()
      .map { Antinode(it) }
      .toSet()
  }
}

fun String.toCity() = City(
  antennas = lineSequence().flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c -> if (c == '.') null else Antenna(Position(x, y), c) }
  }.toList(),
  max = Position(x = indexOf('\n') - 1, y = (length / indexOf('\n')) - 1),
)
