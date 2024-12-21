package com.github.stifred.aoc24.day21

import com.github.stifred.aoc24.shared.MemoizationCache
import com.github.stifred.aoc24.shared.Position
import com.github.stifred.aoc24.shared.solution

val keypadConundrum = solution(day = 21) {
  val codes = parseInput { it.asCodes() }

  part1 { codes.sumOf { it.complexity(robots = 2) } }
  part2 { codes.sumOf { it.complexity(robots = 25) } }
}

enum class Button(val label: Char) {
  Zero('0'), One('1'), Two('2'), Three('3'), Four('4'),
  Five('5'), Six('6'), Seven('7'), Eight('8'), Nine('9'),
  Activate('A'),
  Left('<'), Up('^'), Right('>'), Down('v'),
  Panic('A'),
  ;

  fun numericValueOrNull() = if (label in '0'..'9') (label - '0').toLong() else null

  companion object {
    val Numeric
      get() = listOf(
        Seven, Eight, Nine,
        Four, Five, Six,
        One, Two, Three,
        Panic, Zero, Activate,
      )
    val Directional
      get() = listOf(
        Panic, Up, Activate,
        Left, Down, Right,
      )
  }
}

data class ButtonCombo(val from: Button, val to: Button)

class Keypad private constructor(
  private val buttons: List<Button>,
  private val width: Int = 3,
) {
  fun Button.position() = buttons.indexOf(this).let { i -> Position(x = i % width, y = i / width) }
  private fun positionOf(button: Button) = button.position()

  private fun paths(from: Button, to: Button): List<List<Button>> {
    val fromPos = from.position()
    val toPos = to.position()
    val panicPos = Button.Panic.position()

    val paths = listOf(
      Position.between(fromPos, toPos, alt = false),
      Position.between(fromPos, toPos, alt = true),
    ).filter { panicPos !in it }

    return paths.map { path ->
      path.windowed(2).map { (current, next) ->
        when {
          next.x < current.x -> Button.Left
          next.x > current.x -> Button.Right
          next.y < current.y -> Button.Up
          next.y > current.y -> Button.Down
          else -> error("Invalid")
        }
      }
    }
  }

  fun movementBetween(from: Button, to: Button): List<Button> {
    val sequences = paths(from, to)

    val aPos = Directional.positionOf(Button.Activate)
    return sequences.maxBy { seq ->
      seq.firstOrNull()?.let { Directional.positionOf(it) }?.manhattanTo(aPos) ?: 0
    }
  }

  companion object {
    val Numeric = Keypad(Button.Numeric)
    val Directional = Keypad(Button.Directional)
  }
}

data class Code(val number: Long, val keypad: Keypad, val buttons: List<Button>) {
  fun complexity(robots: Int) = number * length(robots + 1)

  private fun length(depth: Int): Long {
    val cache = MemoizationCache<Pair<ButtonCombo, Int>, Long>()

    return (listOf(Button.Activate) + buttons)
      .windowed(2)
      .map { (a, b) -> ButtonCombo(a, b) }
      .sumOf { length(it, depth, Keypad.Numeric, cache) }
  }

  private fun length(
    combo: ButtonCombo,
    depth: Int,
    keypad: Keypad,
    cache: MemoizationCache<Pair<ButtonCombo, Int>, Long>,
  ): Long {
    if (depth == 0) return 1

    return cache.runMemoized(combo to depth) {
      (listOf(Button.Activate) + keypad.movementBetween(combo.from, combo.to) + Button.Activate)
        .windowed(2)
        .map { (a, b) -> ButtonCombo(a, b) }
        .sumOf { length(it, depth - 1, Keypad.Directional, cache) }
    }
  }
}

fun String.asCodes() = lines().map(String::asCode)

fun String.asCode(): Code {
  var number = 0L
  val buttons = buildList {
    for (ch in this@asCode) {
      val button = Button.entries.first { it.label == ch }
      add(button)
      button.numericValueOrNull()?.let { number = (number * 10) + it }
    }
  }

  return Code(number, Keypad.Numeric, buttons)
}
