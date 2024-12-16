package com.github.stifred.aoc24.day15

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Direction.Companion.asDirectionOrNull
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val day15 = solution(day = 15) {
  val warehouse = parseInput { it.asWarehouseMap() }

  part1 { warehouse.withAllTicks().gpsSum }
  part2 { warehouse.widen().withAllTicks().gpsSum }
}

data class WarehouseMap(
  private val objects: List<Object>,
  private val moves: List<Direction>,
  private val time: Int = 0,
) {
  val gpsSum: Int = objects.sumOf { it.gpsSum }

  fun widen(): WarehouseMap = copy(objects = objects.map(Object::widen))

  fun withAllTicks(): WarehouseMap {
    var current = this
    val totalTime = moves.size
    while (current.time < totalTime) {
      current = current.tick()
    }

    return current
  }

  private fun tick(): WarehouseMap {
    val allMoved = robot.attemptMove(moves[time])
    if (allMoved.isNotEmpty()) {
      val objMap = objects.associateBy { it.id }.toMutableMap()
      for (moved in allMoved) {
        objMap[moved.id] = moved
      }

      return copy(
        objects = objMap.values.toList(),
        time = time + 1,
      )
    } else return copy(time = time + 1)
  }

  private fun Object.attemptMove(dir: Direction): List<Object> {
    if (kind == Object.Kind.Wall) return emptyList()

    val moved = moved(dir)
    val conflicts = moved.positions.asSequence()
      .mapNotNull { at(it) }
      .filter { it.id != id }
      .toSet()

    val movedAndPushed = mutableListOf(moved)
    for (conflict in conflicts) {
      val pushed = conflict.attemptMove(dir)
      if (pushed.isNotEmpty()) {
        movedAndPushed += pushed
      } else return emptyList()
    }

    return movedAndPushed
  }

  private val robot get() = objects.first { it.kind == Object.Kind.Robot }
  private fun at(pos: Position) = objects.firstOrNull { pos in it.positions }
}

data class Object(
  val kind: Kind,
  val positions: Set<Position>,
  val id: Int = nextId.incrementAndGet(),
) {
  fun widen() = when (kind) {
    Kind.Robot -> copy(positions = positions.map { it.copy(x = it.x * 2, y = it.y) }.toSet())
    Kind.Box, Kind.Wall -> copy(
      positions = positions.flatMap {
        setOf(it.copy(x = it.x * 2, y = it.y), it.copy(x = (it.x * 2) + 1, y = it.y))
      }.toSet(),
    )
  }

  val position get() = positions.first()
  val gpsSum = if (this.kind == Kind.Box) position.let { (x, y) -> x + (y * 100) } else 0

  fun moved(dir: Direction) = copy(positions = positions.asSequence().map { it.move(dir) }.toSet())

  enum class Kind { Robot, Box, Wall }

  override fun equals(other: Any?): Boolean = other is Object && other.kind == kind && other.positions == positions
  override fun hashCode(): Int = Objects.hash(kind, positions)

  companion object {
    private val nextId = AtomicInteger(0)
  }
}

fun String.asWarehouseMap(): WarehouseMap {
  val (roomStr, moveStr) = split("\n\n")

  val wallPositions = mutableSetOf<Position>()
  val objects = mutableListOf(Object(Object.Kind.Wall, wallPositions))
  val moves = moveStr.mapNotNull { it.asDirectionOrNull() }

  for ((y, line) in roomStr.lines().withIndex()) {
    for ((x, char) in line.withIndex()) {
      val pos = Position(x, y)

      when (char) {
        '#' -> wallPositions += pos
        '[' -> objects += Object(Object.Kind.Box, setOf(pos, pos.move(Direction.Right)))
        'O' -> objects += Object(Object.Kind.Box, setOf(pos))
        '@' -> objects += Object(Object.Kind.Robot, setOf(pos))
      }
    }
  }

  return WarehouseMap(objects, moves)
}
