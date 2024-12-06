package com.github.stifred.aoc24.fourth

import com.github.stifred.aoc24.shared.Direction
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.parallels
import com.github.stifred.aoc24.shared.solution

val fourth = solution(day = 4) {
  val lh = parseInput { raw ->
    val lines = raw.lines()
    val array = Array(lines.size) { lines[it].toCharArray() }

    LetterHolder(array)
  }

  part1 { lh.findWord("XMAS").count() }

  part2 {
    val firstHalf = lh.findWord("MAS", setOf(Direction.UpRight, Direction.DownLeft))
    val secondHalf = lh.findWord("MAS", setOf(Direction.LeftUp, Direction.RightDown))

    firstHalf.count { it.parallels("MAX", lh.width, lh.height).any(secondHalf::contains) }
  }
}

class LetterHolder(private val map: Array<CharArray>) {
  val width = map[0].size
  val height = map.size

  fun findWord(
    word: String,
    directions: Collection<Direction> = Direction.entries,
  ): List<Pair<Position, Direction>> {
    return buildList {
      for ((pos, char) in map.iterate()) {
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

  companion object {
    operator fun Array<CharArray>.get(position: Position) = this[position.y][position.x]

    private fun Array<CharArray>.iterate(): Iterator<Pair<Position, Char>> {
      val width = this[0].size
      val height = size

      return iterator {
        for (x in 0 until width) {
          for (y in 0 until height) {
            yield(Position(x, y) to this@iterate[y][x])
          }
        }
      }
    }
  }
}
