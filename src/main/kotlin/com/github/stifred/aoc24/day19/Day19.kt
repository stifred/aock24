package com.github.stifred.aoc24.day19

import com.github.stifred.aoc24.shared.*

val day19 = solution(day = 19) {
  val layout = parseInput { it.asLinenLayout() }

  part1 { layout.countPossibleDesigns() }
  part2 { layout.countAllCombos() }
}

data class LinenLayout(val towelPatterns: Set<String>, val designs: List<String>) {
  fun countPossibleDesigns(): Int = designs.count { it.possibleCombos(towelPatterns) > 0 }
  fun countAllCombos(): Long = designs.sumOf { it.possibleCombos(towelPatterns) }
}

fun String.possibleCombos(
  patterns: Set<String>,
  cache: MemoizationCache<String, Long> = MemoizationCache(),
): Long = if (isEmpty()) 1L else cache.runMemoized(this) {
  patterns.asSequence()
    .filter { startsWith(it) }
    .map { substring(it.length) }
    .sumOf { it.possibleCombos(patterns, cache) }
}

fun String.asLinenLayout() = split("\n\n").let { (towelStr, designStr) ->
  LinenLayout(
    towelPatterns = towelStr.split(", ").toSet(),
    designs = designStr.split('\n'),
  )
}
