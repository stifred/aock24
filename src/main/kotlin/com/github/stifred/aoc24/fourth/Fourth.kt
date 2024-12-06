package com.github.stifred.aoc24.fourth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.parallels
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
