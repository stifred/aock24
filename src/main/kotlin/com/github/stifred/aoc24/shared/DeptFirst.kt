package com.github.stifred.aoc24.shared

interface Search<V, S> {
  fun bestPathsBetween(start: V, end: V, initialState: S): List<Path<V, S>>
}

class DepthFirst<V, S>(private val options: MutableList<Step<V, S>>.(V, S) -> Unit) : Search<V, S> {
  override fun bestPathsBetween(start: V, end: V, initialState: S): List<Path<V, S>> {
    val paths = mutableListOf<PathBuilder<V, S>>()
    val toCheck = ArrayDeque<PathBuilder<V, S>>(setOf(PathBuilder(start)))
    val seenMap = mutableMapOf<Pair<V, S>, Int>()

    fun leastScore(vertex: V, state: S) = seenMap[vertex to state] ?: Int.MAX_VALUE

    while (toCheck.isNotEmpty()) {
      val checkPath = toCheck.removeFirst()

      if (checkPath.totalCost >= (paths.minOfOrNull { it.totalCost } ?: Int.MAX_VALUE)) {
        continue
      }

      val lastStep = checkPath.steps.lastOrNull()
      val vertex = lastStep?.vertex ?: start
      val state = lastStep?.state ?: initialState
      val max = paths.minOfOrNull { it.totalCost } ?: Int.MAX_VALUE

      for ((next, nextState) in buildList { options(vertex, state) }) {
        val nextTotal = checkPath.totalCost + 1
        if (nextTotal > leastScore(next, nextState) || nextTotal > max) continue

        seenMap[next to nextState] = nextTotal
        val newPath = checkPath.copy(steps = checkPath.steps + Step(next, nextState))

        if (next == end) {
          paths += newPath
        } else {
          toCheck.addFirst(newPath)
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
