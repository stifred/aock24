package com.github.stifred.aoc24.shared

class MemoizationCache<K : Any, V : Any> {
  private val map = mutableMapOf<K, V>()

  fun clear() = map.clear()
  fun runMemoized(key: K, func: (K) -> V): V = map[key] ?: func(key).also { map[key] = it }
}
