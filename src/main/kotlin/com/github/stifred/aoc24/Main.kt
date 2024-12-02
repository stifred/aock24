package com.github.stifred.aoc24

import com.github.stifred.aoc24.first.first
import com.github.stifred.aoc24.second.second

private val days = listOf(
  first,
  second,
)

fun main() {
  val last = days.maxBy { it.day }

  last.run()
}
