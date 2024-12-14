package com.github.stifred.aoc24.day14

import com.github.stifred.aoc24.shared.*

val day14 = solution(day = 14) {
  val area = parseInput { Area(Position(101, 103)).withRobotString(it) }

  part1 { area.after(100).splitIntoQuadrants().totalSafetyFactor }
  part2 { area.secondsAsSequence().filter(Area::isFestive).map(Area::time).first() }
}

data class Robot(val pos: Position, val velocity: Position)

data class Area(val size: Position, val robots: List<Robot> = listOf(), val time: Int = 0) {
  val safetyFactor get() = robots.size

  val isFestive get() = robots.asSequence().map(Robot::pos).toSet().isFestivelyArranged()

  fun secondsAsSequence() = sequence { for (i in 1..Int.MAX_VALUE) yield(after(i)) }

  fun splitIntoQuadrants(): List<Area> {
    val half = Position(size.x / 2, size.y / 2)
    val offsets = sequenceOf(
      Position(0, 0),
      Position(half.x + 1, 0),
      Position(0, half.y + 1),
      Position(half.x + 1, half.y + 1),
    ).map { it to Position(x = it.x + half.x - 1, y = it.y + half.y - 1) }

    return offsets
      .map { (tl, br) -> Area(half, robots.filter { it.pos.isWithin(tl, br) }) }
      .toList()
  }

  fun after(seconds: Int) = copy(
    robots = robots.map {
      val newX = (it.pos.x + (seconds * it.velocity.x)) % size.x
      val newY = (it.pos.y + (seconds * it.velocity.y)) % size.y

      it.copy(
        pos = Position(
          x = if (newX >= 0) newX else newX + size.x,
          y = if (newY >= 0) newY else newY + size.y,
        ),
      )
    },
    time = time + seconds,
  )

  fun withRobotString(str: String) = copy(
    robots = buildList {
      str.lineSequence()
        .map { it.split(' ') }
        .map { (p, v) -> Robot(p.substring(2).asPosition(), v.substring(2).asPosition()) }
        .forEach { add(it) }
    },
  )
}

val Iterable<Area>.totalSafetyFactor get() = fold(1) { acc, a -> acc * a.safetyFactor }

private fun Collection<Position>.isFestivelyArranged() = any { pos ->
  (1..16).none { Position(pos.x, pos.y + it) !in this }
}
