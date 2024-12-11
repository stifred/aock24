package com.github.stifred.aoc24.eleventh

import com.github.stifred.aoc24.shared.solution

val eleventh = solution(day = 11) {
  val stones = parseInput { raw ->
    raw.split(' ').asSequence()
      .filter { it.isNotBlank() }
      .map { it.toLong() }
      .toList()
  }

  part1 { stones.multiBlink(25) }
  part2 { stones.multiBlink(75) }
}

fun List<Long>.multiBlink(times: Int): Long {
  val cache = mutableMapOf<Pair<Long, Int>, Long>()

  return sumOf { it.blinkWithCache(times, cache) }
}

fun Long.blinkWithCache(rounds: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long {
  // I admit that I did look at my brother's code, but I had the same idea myself.
  // The only thing I did wrong was not adding the 'remaining rounds' count to the cache.

  if (rounds == 0) {
    return 1
  }

  val cached = cache[this to rounds]
  if (cached != null) {
    return cached
  }

  return if (this == 0L) {
    1L.blinkWithCache(rounds - 1, cache)
  } else {
    val matcher = evenPairs.firstOrNull { (_, r) -> this in r }
    if (matcher != null) {
      val first = (this % matcher.first)
      val second = (this / matcher.first)

      first.blinkWithCache(rounds - 1, cache) + second.blinkWithCache(rounds - 1, cache)
    } else (this * 2024).blinkWithCache(rounds - 1, cache)
  }.also { cache[this to rounds] = it }
}

private val evenPairs = buildList {
  var i = 10L
  var j = 10L
  repeat(8) {
    add(i to (j until j * 10))
    i *= 10
    j *= 100
  }
}
