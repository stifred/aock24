package com.github.stifred.aoc24.day15

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Direction.Companion.asDirectionOrNull
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val day15 = solution(day = 15) {
  val warehouse = parseInput { it.asWarehouse() }

  part1 { warehouse.withAllTicks().gpsSum }
  part2 { warehouse.widen().withAllTicks().gpsSum }
}

enum class Entity {
  Robot, Box, BoxLeft, BoxRight, Wall, Floor;

  val isBox get() = this in boxes

  companion object {
    private val boxes by lazy { arrayOf(Box, BoxLeft, BoxRight) }
  }
}

data class Warehouse(
  private val entities: List<Entity>,
  private val moves: List<Direction>,
  private val width: Int,
  private val time: Int = 0,
) {
  val gpsSum: Int
    get() = entities.withIndex().asSequence()
      .filter { (_, e) -> e == gpsSource }
      .map { (i, _) -> i.asPosition() }
      .sumOf { (x, y) -> x + (y * 100) }

  fun withAllTicks(): Warehouse {
    var current = this
    while (!current.done) {
      current = current.withTick()
    }
    return current
  }

  fun widen() = copy(
    entities = entities.flatMap { entity ->
      when (entity) {
        Entity.Box -> sequenceOf(Entity.BoxLeft, Entity.BoxRight)
        Entity.Floor -> sequenceOf(Entity.Floor, Entity.Floor)
        Entity.Wall -> sequenceOf(Entity.Wall, Entity.Wall)
        Entity.Robot -> sequenceOf(Entity.Robot, Entity.Floor)
        else -> error("Already a second warehouse?")
      }
    },
    width = width * 2,
  )

  private fun withTick(): Warehouse {
    if (done) return this

    val robotIndex = entities.indexOf(Entity.Robot)
    val nextMove = moves[time]

    val mutable = entities.toMutableList()

    return if (mutable.swap(robotIndex.asPosition(), nextMove)) {
      copy(entities = mutable.toList(), time = time + 1)
    } else {
      copy(time = time + 1)
    }
  }

  private fun MutableList<Entity>.swap(fromPos: Position, dir: Direction): Boolean {
    return swap(withNeeded(fromPos, dir).toSet(), dir)
  }

  private fun List<Entity>.withNeeded(fromPos: Position, dir: Direction) = when (get(fromPos.asIndex())) {
    Entity.BoxLeft -> sequence {
      yield(fromPos)
      if (dir.isVertical()) yield(fromPos.move(Direction.Right))
    }
    Entity.BoxRight -> sequence {
      yield(fromPos)
      if (dir.isVertical()) yield(fromPos.move(Direction.Left))
    }
    else -> sequenceOf(fromPos)
  }

  private fun MutableList<Entity>.swap(positions: Set<Position>, dir: Direction): Boolean {
    val afterPositions = positions.asSequence()
      .map { it.move(dir) }
      .flatMap { withNeeded(it, dir) }
      .toSet()

    if (afterPositions.any { get(it.asIndex()) == Entity.Wall }) {
      return false
    }

    val needCheck = afterPositions.filter { get(it.asIndex()).isBox }.toSet()
    if (needCheck.isNotEmpty() && !swap(needCheck, dir)) {
      return false
    }

    if (afterPositions.all { get(it.asIndex()) == Entity.Floor }) {
      for (pos in positions) {
        val fromIndex = pos.asIndex()
        val toIndex = pos.move(dir).asIndex()

        val current = get(fromIndex)
        val next = get(toIndex)

        set(fromIndex, next)
        set(toIndex, current)
      }

      return true
    }

    return false
  }

  private val done get() = time >= moves.size
  private val gpsSource = if (Entity.BoxLeft in entities) Entity.BoxLeft else Entity.Box

  private fun Int.asPosition() = Position(this % width, this / width)
  private fun Position.asIndex() = this.y * width + this.x
}

fun String.asWarehouse(): Warehouse {
  val (roomStr, moveStr) = split("\n\n")

  val entities = roomStr.mapNotNull { char ->
    when (char) {
      '.' -> Entity.Floor
      '#' -> Entity.Wall
      'O' -> Entity.Box
      '@' -> Entity.Robot
      '[' -> Entity.BoxLeft
      ']' -> Entity.BoxRight
      else -> null
    }
  }
  val moves = moveStr.mapNotNull { it.asDirectionOrNull() }
  val width = roomStr.indexOf('\n')

  return Warehouse(entities, moves, width)
}
