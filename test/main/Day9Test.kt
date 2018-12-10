package main

import kotlin.test.assertEquals


fun main(args: Array<String>) {
    testPart1()
}

/**
 * Tests for the first part of the marble solution
 *  10 players; last marble is worth 1618 points: high score is 8317
    13 players; last marble is worth 7999 points: high score is 146373
    17 players; last marble is worth 1104 points: high score is 2764
    21 players; last marble is worth 6111 points: high score is 54718
    30 players; last marble is worth 5807 points: high score is 37305
 */
fun testPart1(){
    doTest(32, "9 players; last marble is worth 25 points")
    doTest(8317, "10 players; last marble is worth 1618 points")
    doTest(146373, "13 players; last marble is worth 7999 points")
    doTest(2764, "17 players; last marble is worth 1104 points")
    doTest(54718, "21 players; last marble is worth 6111 points")
    doTest(37305, "30 players; last marble is worth 5807 points")
}

fun doTest(expected: Int, input: String){
    val game = readInput(input)
    assertEquals(expected.toBigInteger(), maxScore(calculateWithLinks(game.first, game.second)))
}