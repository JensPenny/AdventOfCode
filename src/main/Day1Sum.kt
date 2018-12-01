package main

import util.readAsResource

fun main(args: Array<String>) {
    firstPuzzle()
    secondPuzzle()
}

private fun secondPuzzle() {
    val readLines = readAsResource("day1")
    val encountered = mutableSetOf(0)
    var found = false
    var lastResult = 0

    while (!found) {
        println("no result in " + encountered.size + " results, continuing")
        lastResult = readLines
            .map { s -> s.toInt() }
            .takeWhile { !found }                           //Somethings wrong in the neighbourhood
            .fold(lastResult, operation =  { acc, item ->
                found = encountered.contains(acc)
                if (found) {
                    println("result found: $acc")
                    //System.exit(0) //RIP
                }
                encountered.add(acc)
                acc + item
            })
    }
}

private fun firstPuzzle() {
    val readFileLines = readAsResource("day1")
    val total = readFileLines
        .map { s -> s.toInt() }
        .sum()
    println(total)
}