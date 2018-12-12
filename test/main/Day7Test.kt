package main

import main.onetoten.createNodes
import main.onetoten.solvePart1
import main.onetoten.solvePart2
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = "Step C must be finished before step A can begin.\n" +
            "Step C must be finished before step F can begin.\n" +
            "Step A must be finished before step B can begin.\n" +
            "Step A must be finished before step D can begin.\n" +
            "Step B must be finished before step E can begin.\n" +
            "Step D must be finished before step E can begin.\n" +
            "Step F must be finished before step E can begin."
    val lines = input.split("\n")
    //testPart1(lines)
    testPart2(lines)
}

private fun testPart1(lines: List<String>) {
    val nodes = createNodes(lines)
    val part1 = solvePart1(nodes)

    assertEquals("CABDFE", part1)
}

private fun testPart2(lines: List<String>) {
    val nodes = createNodes(lines)
    val part2 = solvePart2(nodes, 50, 2)

    assertEquals(15, part2)
}