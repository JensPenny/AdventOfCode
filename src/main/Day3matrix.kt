package main

import util.readAsResource
import java.awt.Dimension
import java.awt.Point
import kotlin.streams.asSequence

fun main(args: Array<String>) {
    firstPuzzle()
}

private fun firstPuzzle() {
    val lines = readAsResource("day3")
    val grid = Array(1000) { IntArray(1000) }

    val claims = lines.map { line ->
        val dimension = line.substringAfterLast(':').split('x')
        val point = line.substringAfter('@').substringBefore(':').split(',')
        val contentId = line.substringBefore('@')

        Claim(
            Point(point[0].trim().toInt(), point[1].trim().toInt()),
            Dimension(dimension[0].trim().toInt(), dimension[1].trim().toInt()),
            contentId
        )
    }
    println("${claims.size} claims found, registering")

    claims.forEach { c ->
        val start = c.point
        val stop = Point(start.x + c.size.width - 1, start.y + c.size.height - 1)

        for (row in start.x..stop.x) {
            for (col in start.y..stop.y) {
                grid[row][col]++
            }
        }
    }

    val multiclaim = grid
        .asList()
        .stream()
        .map {
            it.filter { cell -> cell > 1 }.size
        }
        .asSequence().sum()

    println("$multiclaim cells have been overwritten more then once")

    claims.forEach { c ->
        val start = c.point
        val stop = Point(start.x + c.size.width - 1, start.y + c.size.height - 1)

        var allOnes = true
        for (row in start.x..stop.x) {
            for (col in start.y..stop.y) {
                allOnes = allOnes && grid[row][col] == 1
            }
        }

        if (allOnes)
            println("single claim found: ${c.contentId}")
    }
}

private class Claim(
    val point: Point,
    val size: Dimension,
    val contentId: String
) 