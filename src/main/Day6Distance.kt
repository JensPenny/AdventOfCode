package main

import util.readAsResource
import java.awt.Point
import kotlin.math.abs
import kotlin.streams.asSequence

fun main(args: Array<String>) {
    val lines = readAsResource("day6")
    //firstPuzzle(lines)
    secondPuzzle(lines)
}

private fun secondPuzzle(lines: List<String>) {
    val fields = toFieldList(lines)
    var validPoints = 0

    val minX = 0 // fields.minBy { f -> f.point.x }!!.point.x
    val maxX = fields.maxBy { f -> f.point.x }!!.point.x + 1000
    val minY = 0 // fields.minBy { f -> f.point.y }!!.point.y
    val maxY = fields.maxBy { f -> f.point.y }!!.point.y + 1000

    for (x in minX until maxX) {
        for (y in minY until maxY) {
            val sum = fields.stream()
                .map { field ->
                    abs(field.point.x - x) + abs(field.point.y - y)
                }
                .asSequence()
                .sum()

            if (sum < 10000){
                validPoints++
            }
        }
    }

    println( "Valid points: $validPoints")
}

private fun firstPuzzle(lines: List<String>) {
    val field = fillField(lines, 100)
    val maxSize = countMaxSize(field)
    println("$maxSize is the max field size")
}

fun fillField(input: List<String>, extraOffset: Int): Array<IntArray> {

    val fields = toFieldList(input)

    val minX = 0 // fields.minBy { f -> f.point.x }!!.point.x
    val maxX = fields.maxBy { f -> f.point.x }!!.point.x + extraOffset
    val minY = 0 // fields.minBy { f -> f.point.y }!!.point.y
    val maxY = fields.maxBy { f -> f.point.y }!!.point.y + extraOffset

    val grid = Array(maxX - minX) { IntArray(maxY - minY) }


    for (x in 0 until grid.size) {
        for (y in 0 until grid[x].size) {
            val minimizerFunction: (Field) -> Int = { f -> abs(f.point.x - x - minX) + abs(f.point.y - y - minY) }
            val minField = fields.minBy(minimizerFunction)
            val reverseMinField = fields.reversed().minBy(minimizerFunction)

            if (minField!! == reverseMinField) {
                grid[x][y] = minField.id
            } else {
                grid[x][y] = -1
            }
        }
    }

    return grid
}

private fun toFieldList(input: List<String>): List<Field> {
    val fields = input.mapIndexed { index, s ->
        Field(
            index,
            Point(
                s.substringBefore(",").trim().toInt(),
                s.substringAfter(",").trim().toInt()
            )
        )
    }
    return fields
}

fun printField(field: Array<IntArray>) {
    for (x in 0 until field.size) {
        for (y in 0 until field[x].size) {
            val value = if (field[x][y] == -1) "." else field[x][y].toString()
            print("$value ")
        }
        print("\n")
    }
}

fun countMaxSize(field: Array<IntArray>): Int {

    val infiniteTop = field[0].toSet()
    val infiniteBottom = field[field.size - 1].toSet()
    val infiniteLeft = field.asList().map { ints -> ints[0] }.toSet()
    val infiniteRight = field.asList().map { ints -> ints[ints.size - 1] }.toSet()
    val allInfinites = HashSet<Int>(setOf(infiniteTop, infiniteBottom, infiniteLeft, infiniteRight).flatten())

    val flattened = field.asList().stream().map { ints -> ints.asList() }.asSequence().flatten()

    val flatGroup = flattened.groupBy { i -> i }
    val maxEntry = flatGroup.filterKeys { key -> !allInfinites.contains(key) }.maxBy { entry -> entry.value.size }
    return maxEntry!!.value.size
}

class Field(
    val id: Int,
    val point: Point
)