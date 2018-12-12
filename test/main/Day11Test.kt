package main

import kotlin.test.assertEquals

/**
Fuel cell at  122,79, grid serial number 57: power level -5.
Fuel cell at 217,196, grid serial number 39: power level  0.
Fuel cell at 101,153, grid serial number 71: power level  4.

 */
fun main(args: Array<String>) {
    day11Test(300, 57, 122, 79, -5)
    day11Test(300, 39, 217, 196, 0)
    day11Test(300, 71, 101, 153, 4)
    println("All tests for day 11 finished")
}

fun day11Test(arraySize: Int, serial: Int, x: Int, y: Int, expected: Int) {
    val matrix = createMatrix(arraySize, serial)
    assertEquals(matrix[x][y], expected)
}
