package com.github.stifred.aoc24

import com.github.stifred.aoc24.day14.day14
import com.github.stifred.aoc24.day15.day15
import com.github.stifred.aoc24.day16.day16
import com.github.stifred.aoc24.day17.day17
import com.github.stifred.aoc24.day18.day18
import com.github.stifred.aoc24.day19.day19
import com.github.stifred.aoc24.day20.day20
import com.github.stifred.aoc24.day21.keypadConundrum
import com.github.stifred.aoc24.day22.monkeyMarket
import com.github.stifred.aoc24.day23.lanParty
import com.github.stifred.aoc24.day24.crossedWires
import com.github.stifred.aoc24.day25.codeChronicle
import com.github.stifred.aoc24.eighth.eighth
import com.github.stifred.aoc24.eleventh.eleventh
import com.github.stifred.aoc24.fifth.fifth
import com.github.stifred.aoc24.first.first
import com.github.stifred.aoc24.fourth.fourth
import com.github.stifred.aoc24.ninth.ninth
import com.github.stifred.aoc24.second.second
import com.github.stifred.aoc24.seventh.seventh
import com.github.stifred.aoc24.shared.Solution
import com.github.stifred.aoc24.sixth.sixth
import com.github.stifred.aoc24.tenth.tenth
import com.github.stifred.aoc24.third.third
import com.github.stifred.aoc24.thirteenth.thirteenth
import com.github.stifred.aoc24.twelfth.twelfth

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
  eleventh,
  twelfth,
  thirteenth,
  day14,
  day15,
  day16,
  day17,
  day18,
  day19,
  day20,
  keypadConundrum,
  monkeyMarket,
  lanParty,
  crossedWires,
  codeChronicle,
)

fun lastDay(day: Int? = null) = days
  .filter { day == null || it.day == day }
  .maxBy { it.day }

fun main() = lastDay().run(
  runFirst = true,
  runSecond = true,
  runBenchmark = false,
)
