package com.github.stifred.aoc24.day19

import com.github.stifred.aoc24.shared.*

val day19 = solution(day = 19) {
  val layout = parseInput { it.asLinenLayout() }

  part1 { layout.countPossibleDesigns() }
  part2 { layout.countAllCombos() }
}

enum class Color { White, Blue, Black, Red, Green }

typealias Pattern = List<Color>
typealias Design = List<Color>

data class LinenLayout(val patterns: Set<Pattern>, val designs: List<Design>) {
  fun countPossibleDesigns(): Int = possibleCombos().count { it > 0 }
  fun countAllCombos(): Long = possibleCombos().sum()

  private fun possibleCombos() = MemoizationCache<Design, Long>().let { cache ->
    designs.map { it.possibleCombos(cache) }
  }

  private fun Design.possibleCombos(
    cache: MemoizationCache<Design, Long>,
  ): Long = if (isEmpty()) 1L else cache.runMemoized(this) {
    patterns.asSequence()
      .filter { it.size <= size }
      .filter { subList(0, it.size) == it }
      .map { subList(it.size, size) }
      .sumOf { it.possibleCombos(cache) }
  }
}

fun String.asLinenLayout() = split("\n\n").let { (towelStr, designStr) ->
  LinenLayout(
    patterns = towelStr.split(", ").map(String::asColors).toSet(),
    designs = designStr.split('\n').map(String::asColors),
  )
}

private fun String.asColors(): List<Color> = map { CHAR_MAP.getValue(it) }

private val CHAR_MAP = mapOf(
  'w' to Color.White,
  'u' to Color.Blue,
  'b' to Color.Black,
  'r' to Color.Red,
  'g' to Color.Green,
)
