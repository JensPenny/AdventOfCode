package main

import main.onetoten.countMaxSize
import main.onetoten.fillField
import util.printMatrix
import kotlin.test.assertEquals

fun main(args : Array<String>) {
    testPuzzle1()
}

fun testPuzzle1() {
    val input = "1, 1\n" +
            "1, 6\n" +
            "8, 3\n" +
            "3, 4\n" +
            "5, 5\n" +
            "8, 9"


    val field = fillField(input.split("\n"), 10)
    printMatrix(field)
    val maxFieldSize = countMaxSize(field)
    assertEquals(17, maxFieldSize)
}