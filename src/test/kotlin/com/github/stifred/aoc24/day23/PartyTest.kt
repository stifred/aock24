package com.github.stifred.aoc24.day23

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PartyTest {
  @Test
  fun `test party`() {
    val party = """
      kh-tc
      qp-kh
      de-cg
      ka-co
      yn-aq
      qp-ub
      cg-tb
      vc-aq
      tb-ka
      wh-tc
      yn-cg
      kh-ub
      ta-co
      de-co
      tc-td
      tb-wq
      wh-td
      ta-ka
      td-qp
      aq-cg
      wq-ub
      ub-vc
      de-ta
      wq-aq
      wq-vc
      wh-yn
      ka-de
      kh-ta
      co-tc
      wh-qp
      tb-vc
      td-yn
    """.trimIndent().asParty()

    val threeWays = party.threeWays()
    val first = Connection(setOf(Computer('a' to 'q'), Computer('c' to 'g'), Computer('y' to 'n')))

    assertEquals(12, threeWays.size)
    assertTrue { first in threeWays }

    val historic = threeWays.filter { it.mightBeChiefHistorian() }
    assertEquals(7, historic.size)

    val map = party.computersByConnection()
    assertEquals(4, map[Computer('c' to 'o')])

    val largest = party.largest2()
    assertTrue { Computer('c' to 'o') in largest.computers }
    assertTrue { Computer('d' to 'e') in largest.computers }
    assertTrue { Computer('k' to 'a') in largest.computers }
    assertTrue { Computer('t' to 'a') in largest.computers }

    assertEquals("co,de,ka,ta", largest.password())
  }
}
