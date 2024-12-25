package com.github.stifred.aoc24.day23

import com.github.stifred.aoc24.shared.search.SearchState
import com.github.stifred.aoc24.shared.search.dfs
import com.github.stifred.aoc24.shared.solution

val lanParty = solution(day = 23) {
  val party = parseInput { it.asParty() }

  part1 { party.threeWays().count { it.mightBeChiefHistorian() } }
  part2 { party.largest2().password() }
}

data class Party(val connections: Set<Connection>) {
  fun largest2(): Connection {
    val max = computersByConnection().values.max()

    val dfs = dfs<Connection>()
    for (connection in connections) {
      dfs.push(connection)
    }

    return dfs.find { state ->
      if (state.computers.size == max) found(state)

      val connected = state.computers.asSequence()
        .drop(1)
        .fold(state.computers.first().connected()) { acc, comp ->
          if (acc.isEmpty()) emptySet() else (acc intersect comp.connected())
        }

      for (comp in connected) {
        continueWith(state.copy(computers = state.computers + comp))
      }
    }!!
  }

  fun threeWays() = connections.asSequence()
    .flatMap { it.findBigger(connections) }
    .toSet()

  fun computersByConnection() = connections.asSequence()
    .flatMap { it.computers }
    .fold(mutableMapOf<Computer, Int>()) { map, comp ->
      map.apply { merge(comp, 1, Int::plus) }
    }

  private fun Computer.connected() = connections.asSequence()
    .filter { this in it.computers }
    .flatMap { it.computers }
    .toSet() - this
}

data class Connection(val computers: Set<Computer>) : SearchState {
  fun mightBeChiefHistorian() = computers.any { it.mightBeChiefHistorian() }

  fun password() = computers.map { "${it.name.first}${it.name.second}" }
    .sorted()
    .joinToString(",")

  fun findBigger(others: Collection<Connection>) = buildSet {
    for (other in others) {
      val intersect = (computers + other.computers)
      if (intersect.size != computers.size + 1 || !(computers.all { it in intersect })) continue

      var still = true
      for (i in intersect) {
        if (Connection(intersect - i) !in others) {
          still = false
          break
        }
      }

      if (still) {
        add(Connection(intersect))
      }
    }
  }
}

data class Computer(val name: Pair<Char, Char>) {
  fun mightBeChiefHistorian() = name.first == 't'
}

fun String.asParty() = Party(
  connections = lineSequence()
    .filter { it.isNotEmpty() }
    .map { Connection(setOf(Computer(it[0] to it[1]), Computer(it[3] to it[4]))) }
    .toSet()
)
