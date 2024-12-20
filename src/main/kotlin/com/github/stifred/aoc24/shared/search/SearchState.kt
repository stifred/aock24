package com.github.stifred.aoc24.shared.search

interface SearchState {
  val cost: Int get() = -1
}

interface SearchStateWithKey<K> : SearchState {
  val key: K
}
