package main

import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val testInput = mutableListOf(3,7)

    //First part tests
    assertEquals("5158916779", firstPuzzle(testInput, 9))
    assertEquals("0124515891", firstPuzzle(testInput, 5))
    assertEquals("9251071085", firstPuzzle(testInput, 18))
    assertEquals("5941429882", firstPuzzle(testInput, 2018))

    //Second part tests
    assertEquals(9, secondPuzzle(testInput, "51589"))
    assertEquals(5, secondPuzzle(testInput, "01245"))
    assertEquals(18, secondPuzzle(testInput, "92510"))
    assertEquals(2018, secondPuzzle(testInput, "59414"))
}