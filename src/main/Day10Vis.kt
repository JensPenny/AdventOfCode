package main

import util.readAsResource
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.util.stream.Collectors

fun main(args: Array<String>) {
    //val lines = testInput.split("\n")
    val lines = readAsResource("day10")
    val data = createDataPoints(lines)

    for (sec in 0..200000) {
        advanceDataPoints(data)
        printDataPoints(data, sec)
    }
}

fun createDataPoints(lines: List<String>): Collection<DataPoint> {
    return lines.map { s ->
        val doubleString = s.split("=")
        val positionStr = doubleString[1].substringAfter("<").substringBefore(">").split(",")
        val position = Point(positionStr[0].trim().toInt(), positionStr[1].trim().toInt())

        val vectorStr = doubleString[2].substringAfter("<").substringBefore(">").split(",")
        val vector = Point(vectorStr[0].trim().toInt(), vectorStr[1].trim().toInt())

        DataPoint(position, vector)
    }
}

fun advanceDataPoints(points: Collection<DataPoint>) {
    points.forEach { p -> p.translate() }
}

fun printDataPoints(points: Collection<DataPoint>, currentSec: Int) {
    val minX = points.map { p -> p.currentPosition.x }.min()!!
    val minY = points.map { p -> p.currentPosition.y }.min()!!
    val maxX = points.map { p -> p.currentPosition.x }.max()!!
    val maxY = points.map { p -> p.currentPosition.y }.max()!!
    val currentPoints = points.map { p -> p.currentPosition }.stream().collect(Collectors.toSet())

    val offsetX = 0 - minX + 2
    val offsetY = 0 - minY + 2

    val imgWidth = minX.until(maxX).toList().size + 4
    val imgHeight = minY.until(maxY).toList().size + 4

    if (imgWidth > 1000 && imgHeight > 1000){
        println("Could not print second $currentSec. Image too large: w$imgWidth - h$imgHeight")
        return
    }

    try {
        val img = BufferedImage(
            imgWidth,
            imgHeight,
            BufferedImage.TYPE_INT_RGB
        )
        val g = img.graphics

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val p = Point(x, y)
                if (currentPoints.contains(p)) {
                    g.drawRect(x + offsetX, y + offsetY, 1, 1)
                }
            }
        }
        val file = File("/home/jens/Workspace/Kotlin/AdventOfCode/images/$currentSec.jpg")
        val written = javax.imageio.ImageIO.write(img, "png", file)
        println("Written: $written")
    } catch (e: NegativeArraySizeException) {
        println("Could not print second $currentSec. Image too large: w$imgWidth - h$imgHeight")
    }
}

class DataPoint(
    startPosition: Point,
    val vector: Point
) {
    var currentPosition = startPosition

    fun translate(): Point {
        currentPosition = Point(currentPosition.x + vector.x, currentPosition.y + vector.y)
        return currentPosition
    }
}

val testInput = "position=< 9,  1> velocity=< 0,  2>\n" +
        "position=< 7,  0> velocity=<-1,  0>\n" +
        "position=< 3, -2> velocity=<-1,  1>\n" +
        "position=< 6, 10> velocity=<-2, -1>\n" +
        "position=< 2, -4> velocity=< 2,  2>\n" +
        "position=<-6, 10> velocity=< 2, -2>\n" +
        "position=< 1,  8> velocity=< 1, -1>\n" +
        "position=< 1,  7> velocity=< 1,  0>\n" +
        "position=<-3, 11> velocity=< 1, -2>\n" +
        "position=< 7,  6> velocity=<-1, -1>\n" +
        "position=<-2,  3> velocity=< 1,  0>\n" +
        "position=<-4,  3> velocity=< 2,  0>\n" +
        "position=<10, -3> velocity=<-1,  1>\n" +
        "position=< 5, 11> velocity=< 1, -2>\n" +
        "position=< 4,  7> velocity=< 0, -1>\n" +
        "position=< 8, -2> velocity=< 0,  1>\n" +
        "position=<15,  0> velocity=<-2,  0>\n" +
        "position=< 1,  6> velocity=< 1,  0>\n" +
        "position=< 8,  9> velocity=< 0, -1>\n" +
        "position=< 3,  3> velocity=<-1,  1>\n" +
        "position=< 0,  5> velocity=< 0, -1>\n" +
        "position=<-2,  2> velocity=< 2,  0>\n" +
        "position=< 5, -2> velocity=< 1,  2>\n" +
        "position=< 1,  4> velocity=< 2,  1>\n" +
        "position=<-2,  7> velocity=< 2, -2>\n" +
        "position=< 3,  6> velocity=<-1, -1>\n" +
        "position=< 5,  0> velocity=< 1,  0>\n" +
        "position=<-6,  0> velocity=< 2,  0>\n" +
        "position=< 5,  9> velocity=< 1, -2>\n" +
        "position=<14,  7> velocity=<-2,  0>\n" +
        "position=<-3,  6> velocity=< 2, -1>"