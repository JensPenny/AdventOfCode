package main

import util.printMatrix
import java.awt.Point
import kotlin.math.min

fun main(args: Array<String>) {
    val grid = createMatrix(300, 8561)
    firstPuzzle(grid)
    secondPuzzle(grid)
}

fun secondPuzzle(grid: Array<IntArray>) {
    calculateMax(grid, 1, grid.size)
}

private fun firstPuzzle(grid: Array<IntArray>) {
    printMatrix(grid)
    calculateMax(grid, 3, 3)
}

fun calculateMax(grid: Array<IntArray>, minGrid: Int, maxGrid: Int): Pair<Point, Int> {
    val sums = Array(grid.size + 1) { IntArray(grid.size + 1) }

    //State to keep the current maxes
    var max = 0
    var maxPoint = Point(-1, -1)
    var maxGridSize = -1

    for (y in 1..grid.size) {
        for (x in 1..grid.size) {
            for (gridSize in minGrid..maxGrid) {
                val xoffsetRange = x until min((x + gridSize), grid.size)
                val yOffsetRange = y until min((y + gridSize), grid.size)
                if (xoffsetRange.isEmpty() || yOffsetRange.isEmpty()) { //Would be surprising if a grid with so few numbers has the max, so quickbreak
                    sums[x][y] = 0
                    continue
                }

                //Bug or just dumb understanding of reduce and accumulators
                /*val currentValue = xoffsetRange.reduce { xAcc, innerX ->
                    xAcc + yOffsetRange.reduce { yAcc, innerY -> yAcc + grid[innerX][innerY] }
                }*/
                val currentValue = xoffsetRange.sumBy { xIndex ->
                    yOffsetRange.sumBy { yIndex -> grid[xIndex][yIndex] }
                }

                if (currentValue > max) {
                    max = currentValue
                    maxPoint = Point(x, y)
                    maxGridSize = gridSize
                    println("max updated $maxPoint - $max - $maxGridSize")
                }

                //Not really needed, but hopefully this is needed in part 2.......
                sums[x][y] = currentValue
            }
        }
    }

    println("Point: $maxPoint - Val: $max - Gridsize: $maxGridSize")
    return Pair(maxPoint, max)
}

fun createMatrix(matrixSize: Int, serialNumber: Int): Array<IntArray> {
    val matrix = Array(matrixSize + 1) { IntArray(matrixSize + 1) }

    for (y in 1..matrixSize) {
        for (x in 1..matrixSize) {
            val rackId = x + 10
            val powerLevel = rackId * y
            val multiplier = powerLevel + serialNumber
            val intermed = multiplier * rackId
            val hundred = (intermed / 100).toString().last()
            matrix[x][y] = Integer.parseInt(hundred.toString()) - 5
        }
    }
    return matrix
}