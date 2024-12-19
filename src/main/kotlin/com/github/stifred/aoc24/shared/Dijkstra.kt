package com.github.stifred.aoc24.shared

data class Step<V, S>(val vertex: V, val state: S, val cost: Int = 1)

data class Path<V, S>(val steps: List<Step<V, S>>) {
  val distinctVertices get() = steps.map { it.vertex }.toSet()
  val totalSteps get() = steps.count { it.cost > 0 }
  val totalCost get() = steps.sumOf { it.cost }
}

val <V> Collection<Path<V, *>>.distinctVertices get() = flatMap(Path<V, *>::distinctVertices).toSet()

class Dijkstra<V, S>(private val costs: suspend SequenceScope<Step<V, S>>.(V, S) -> Unit) : Search<V, S> {
  override fun bestPathsBetween(start: V, end: V, initialState: S): List<Path<V, S>> {
    val paths = mutableListOf<PathBuilder<V, S>>()
    val toCheck = mutableListOf<PathBuilder<V, S>>(PathBuilder(start))
    val seenMap = mutableMapOf<Pair<V, S>, Int>((start to initialState) to 0)

    fun leastScore(vertex: V, state: S) = seenMap[vertex to state] ?: Int.MAX_VALUE

    while (toCheck.isNotEmpty()) {
      val checkPath = toCheck.minBy { it.totalCost }
      toCheck.remove(checkPath)

      val lastStep = checkPath.steps.lastOrNull()
      val vertex = lastStep?.vertex ?: start
      val state = lastStep?.state ?: initialState
      val max = paths.minOfOrNull { it.totalCost } ?: Int.MAX_VALUE

      for ((next, nextState, nextCost) in sequence { costs(vertex, state) }) {
        val nextTotal = checkPath.totalCost + nextCost
        if (nextTotal > leastScore(next, nextState) || nextTotal > max) continue

        seenMap[next to nextState] = nextTotal
        val newPath = checkPath.copy(steps = checkPath.steps + Step(next, nextState, nextCost))

        if (next == end) {
          paths += newPath
          break
        } else {
          toCheck += newPath
        }
      }
    }

    val minMax = paths.minOfOrNull { it.totalCost } ?: return emptyList()
    return paths.asSequence()
      .filter { it.totalCost == minMax }
      .map { it.build(initialState) }
      .toList()
  }
}

data class PathBuilder<V, S>(val start: V, val steps: List<Step<V, S>> = listOf()) {
  val totalCost = steps.sumOf { it.cost }

  fun build(initialState: S) = Path(listOf(Step(start, initialState, 0)) + steps)
}
