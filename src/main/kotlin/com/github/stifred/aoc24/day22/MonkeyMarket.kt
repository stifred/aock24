package com.github.stifred.aoc24.day22

import com.github.stifred.aoc24.shared.solution

val monkeyMarket = solution(day = 22) {
  val secretNumbers = parseInput { it.asSecretNumbers() }

  part1 { secretNumbers.sumOf(SecretNumber::last) }
  part2 { secretNumbers.priceByDiffs().bestOption() }
}

typealias SecretNumber = Long
typealias Price = Int
data class PriceDiffs(val a: Price, val b: Price, val c: Price, val d: Price)

fun SecretNumber.asPrice(): Price = (this % 10).toInt()

fun SecretNumber.priceSequence(): Sequence<Price> = sequence().map(SecretNumber::asPrice)

fun SecretNumber.last(): SecretNumber = sequence().last()

fun SecretNumber.priceByDiffs() = buildMap {
  priceSequence()
    .windowed(5)
    .map { (a, b, c, d, e) -> PriceDiffs(b - a, c - b, d - c, e - d) to e }
    .filter { (diffs) -> diffs !in this }
    .forEach { (diffs, price) -> put(diffs, price) }
}

fun List<SecretNumber>.priceByDiffs() = map(SecretNumber::priceByDiffs)

fun List<Map<PriceDiffs, Price>>.bestOption() = asSequence()
  .flatMap { it.keys }
  .distinct()
  .maxOf { diffs -> sumOf { it[diffs] ?: 0 } }

fun SecretNumber.sequence(): Sequence<SecretNumber> =
  (0 until 2000).asSequence().runningFold(this) { acc, _ -> acc.next() }

fun SecretNumber.next(): SecretNumber {
  val step1 = (this xor (this * 64)) % 16_777_216
  val step2 = (step1 xor (step1 / 32) % 16_777_216)
  return (step2 xor (step2 * 2048) % 16_777_216)
}

fun String.asSecretNumbers(): List<SecretNumber> = lines().filter(String::isNotBlank).map(String::toLong)
