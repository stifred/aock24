package com.github.stifred.aoc24.fifth

import com.github.stifred.aoc24.shared.solution

val fifth = solution(day = 5) {
  val set = parseInput { raw ->
    val rules = mutableListOf<Rule>()
    val updates = mutableListOf<Update>()

    for (line in raw.lines()) {
      if ('|' in line) {
        val (a, b) = line.split('|')
        rules += Rule(a.toInt(), b.toInt())
      } else if (',' in line) {
        val bits = line.split(',')
        updates += Update(bits.map(String::toInt))
      }
    }

    UpdateSet(rules, updates)
  }

  part1 { set.valid.sumOf(Update::middle) }
  part2 { set.fixedInvalid.sumOf(Update::middle) }
}

data class UpdateSet(val rules: List<Rule>, val updates: List<Update>) {
  val valid get() = updates.asSequence().filter { it fits rules }
  val fixedInvalid get() = updates.asSequence()
    .filter { u -> rules.any { !(u fits it) } }
    .map { it fixedBy rules }
}

data class Rule(val before: Int, val after: Int)

data class Update(val numbers: List<Int>) {
  val middle get() = numbers[numbers.size / 2]

  infix fun fits(rules: List<Rule>): Boolean = rules.all(::fits)

  infix fun fits(rule: Rule): Boolean {
    val before = numbers.indexOf(rule.before)
    if (before < 0) return true

    val after = numbers.indexOf(rule.after)
    return after < 0 || after > before
  }

  infix fun fixedBy(rules: List<Rule>): Update {
    var fixed = this
    do {
      fixed = rules.fold(fixed) { update, rule -> update adaptedTo rule }
    } while (!(fixed fits rules))

    return fixed
  }

  private infix fun adaptedTo(rule: Rule): Update {
    if (fits(rule)) return this

    val before = numbers.indexOf(rule.before)
    val after = numbers.indexOf(rule.after)

    val updated = numbers.toMutableList()
    updated.removeAt(before)
    updated.add(after, numbers[before])

    return Update(updated)
  }
}
