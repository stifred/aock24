package com.github.stifred.aoc24

import com.github.stifred.aoc24.first.first
import com.github.stifred.aoc24.fourth.fourth
import com.github.stifred.aoc24.second.second
import com.github.stifred.aoc24.third.third

private val days = listOf(
  first,
  second,
  third,
  fourth,
)

fun main() {
  val last = days.maxBy { it.day }

  last.run()
}
