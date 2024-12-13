package com.github.stifred.aoc24.shared


data class Position(val x: Int, val y: Int) {
  fun move(dir: Direction) = copy(x = x + dir.x, y = y + dir.y)

  fun move(dir: Direction, width: Int, height: Int) =
    move(dir).takeIf { it.x in (0 until width) && it.y in (0 until height) }

  fun isWithin(topLeft: Position, rightBottom: Position): Boolean {
    return x in (topLeft.x..rightBottom.x) && y in (topLeft.y..rightBottom.y)
  }

  operator fun minus(other: Position) = Position(x = x - other.x, y = y - other.y)
  operator fun plus(other: Position) = Position(x = x + other.x, y = y + other.y)
  operator fun times(int: Int) = Position(x = int * x, y = int * y)

  infix fun touches(other: Position) = when {
    x == other.x -> y in (other.y - 1)..(other.y + 1)
    y == other.y -> x in (other.x - 1)..(other.x + 1)
    else -> false
  }
}

data class LongPosition(val x: Long, val y: Long) {
  fun move(dir: Direction) = copy(x = x + dir.x, y = y + dir.y)

  operator fun minus(other: LongPosition) = LongPosition(x = x - other.x, y = y - other.y)
  operator fun plus(other: LongPosition) = LongPosition(x = x + other.x, y = y + other.y)
  operator fun times(long: Long) = LongPosition(x = long * x, y = long * y)
}

enum class Direction(val x: Int, val y: Int) {
  Left(-1, 0),
  LeftUp(-1, -1),
  Up(0, -1),
  UpRight(1, -1),
  Right(1, 0),
  RightDown(1, 1),
  Down(0, 1),
  DownLeft(-1, 1),
  ;

  fun flipHorizontal() = when (this) {
    LeftUp -> UpRight
    UpRight -> LeftUp
    RightDown -> DownLeft
    DownLeft -> RightDown
    Left -> Right
    Right -> Left
    else -> this
  }

  fun flipVertical() = when (this) {
    LeftUp -> DownLeft
    DownLeft -> LeftUp
    UpRight -> RightDown
    RightDown -> UpRight
    Up -> Down
    Down -> Up
    else -> this
  }

  fun opposite() = change(4)

  fun softLeft(): Direction = change(-1)
  fun softRight(): Direction = change(1)

  fun hardLeft(): Direction = change(-2)
  fun hardRight(): Direction = change(2)

  fun normals() = when {
    isHorizontal() -> verticals
    isVertical() -> horizontals
    else -> error("")
  }
  fun isHorizontal() = this in horizontals
  fun isVertical() = this in verticals

  private fun change(diff: Int): Direction {
    val index = entries.indexOf(this) + diff
    val size = entries.size
    return when {
      index >= size -> entries[index - size]
      index < 0 -> entries[index + size]
      else -> entries[index]
    }
  }

  companion object {
    val horizontals by lazy { setOf(Left, Right) }
    val verticals by lazy { setOf(Up, Down) }
    val nonDiagonals by lazy { setOf(Left, Up, Right, Down) }
  }
}

fun Pair<Position, Direction>.parallels(word: String, width: Int, height: Int): List<Pair<Position, Direction>> {
  return buildList {
    val offset = word.length - 1

    val horizontal = Pair(
      Position(first.x + (offset * second.x), first.y),
      second.flipHorizontal()
    )
    if (horizontal.first.x in (0 until width)) {
      add(horizontal)
    }

    val vertical = Pair(
      Position(first.x, first.y + (offset * second.y)),
      second.flipVertical()
    )
    if (vertical.first.y in (0 until height)) {
      add(vertical)
    }
  }
}
