package com.github.stifred.aoc24.fourth

import com.github.stifred.aoc24.shared.solution

val fourth = solution(day = 4) {
  val lh = parseInput { raw ->
    LetterHolder(
      buildMap {
        raw.lines().forEachIndexed { y, line ->
          line.forEachIndexed { x, char ->
            put(Position(x, y), char)
          }
        }
      },
    )
  }

  part1 { lh.findWord("XMAS").count() }

  part2 {
    val firstHalf = lh.findWord("MAS", setOf(Direction.UpRight, Direction.DownLeft))
    val secondHalf = lh.findWord("MAS", setOf(Direction.LeftUp, Direction.RightDown))

    firstHalf.count { it.parallels("MAX", lh.width, lh.height).any(secondHalf::contains) }
  }
}

class LetterHolder(private val map: Map<Position, Char>) {
  val width = map.keys.maxOf { it.x } + 1
  val height = map.keys.maxOf { it.y } + 1

  fun findWord(
    word: String,
    directions: Collection<Direction> = Direction.entries,
  ): List<Pair<Position, Direction>> {
    return buildList {
      for ((pos, char) in map) {
        if (char == word[0]) {
          for (dir in directions) {
            var score = 1
            var cursor = pos
            for (offset in (1 until word.length)) {
              cursor = cursor.move(dir, width, height) ?: break
              if (word[offset] != map[cursor]) break

              score++
            }

            if (score == word.length) {
              add(pos to dir)
            }
          }
        }
      }
    }
  }
}

data class Position(val x: Int, val y: Int) {
  fun move(dir: Direction, width: Int, height: Int) =
    copy(x = x + dir.x, y = y + dir.y)
      .takeIf { it.x in (0 until width) && it.y in (0 until height) }
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
