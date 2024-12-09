package com.github.stifred.aoc24

import com.github.stifred.aoc24.eighth.eighth
import com.github.stifred.aoc24.fifth.fifth
import com.github.stifred.aoc24.first.first
import com.github.stifred.aoc24.fourth.fourth
import com.github.stifred.aoc24.ninth.ninth
import com.github.stifred.aoc24.second.second
import com.github.stifred.aoc24.seventh.seventh
import com.github.stifred.aoc24.sixth.sixth
import com.github.stifred.aoc24.tenth.tenth
import com.github.stifred.aoc24.third.third

private val days = listOf(
  first,
  second,
  third,
  fourth,
  fifth,
  sixth,
  seventh,
  eighth,
  ninth,
  tenth,
)

fun main() {
  val last = days.maxBy { it.day }

  last.run()
}
