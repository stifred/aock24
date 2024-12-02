package com.github.stifred.aoc24

import com.github.stifred.aoc24.first.first

private val days = listOf(
  first,
)

fun main() {
  val last = days.maxBy { it.day }

  last.run()
}
