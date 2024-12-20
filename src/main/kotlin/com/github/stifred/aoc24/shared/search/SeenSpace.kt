package com.github.stifred.aoc24.shared.search

sealed interface SeenSpace<S : SearchState> {
  fun reset()
  fun hasSeen(state: S): Boolean
  fun tryMarkSeen(state: S): Boolean
}

@Suppress("UNCHECKED_CAST", "FunctionName")
fun <S : SearchState> NoSeenSpace() = NoSeenSpace as SeenSpace<S>
private data object NoSeenSpace : SeenSpace<NoSeenSpace.BogusSearchState> {
  override fun reset() = Unit
  override fun hasSeen(state: BogusSearchState): Boolean = false
  override fun tryMarkSeen(state: BogusSearchState): Boolean = true

  object BogusSearchState : SearchState {
    override val cost: Int get() = 0
  }
}

class SetMapSeenSpace<S : SearchState> : SeenSpace<S> {
  private val set = mutableSetOf<S>()

  override fun reset() = set.clear()
  override fun hasSeen(state: S): Boolean = state in set
  override fun tryMarkSeen(state: S): Boolean = set.add(state)
}

class KeyMapSeenSpace<S : SearchStateWithKey<K>, K> : SeenSpace<S> {
  private val map = mutableMapOf<K, S>()

  override fun reset() = map.clear()

  override fun hasSeen(state: S): Boolean {
    val existing = map[state.key]
    return existing == null || state.cost >= existing.cost
  }

  override fun tryMarkSeen(state: S): Boolean {
    val old = map[state.key]
    if (old == null || state.cost <= old.cost) {
      map[state.key] = state
      return true
    }

    return false
  }
}
