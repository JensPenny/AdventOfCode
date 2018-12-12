package main.onetoten

import util.readAsResource

fun main(args: Array<String>) {
    val lines = readAsResource("day5")
    val reduced = reduceInput(lines.first())
    val start = System.currentTimeMillis()
    println(reduced.length)
    println("Solution found in " + (System.currentTimeMillis() - start) + " ms")

    val start2 = System.currentTimeMillis()
    val secondPuzzle = findMinimal(lines.first())
    println(secondPuzzle.length)
    println("Solution found in " + (System.currentTimeMillis() - start2) + " ms")
}

fun findMinimal(input: String): String {

    val turboRemover = "abcdefghijklmnopqrstuvwxyz"
    var smallestResult = input

    turboRemover.forEach { toRemove ->
        val toReduce = input.filter { c -> c.toLowerCase() != toRemove }

        val reduce = reduceInput(toReduce)
        if (reduce.length < smallestResult.length){
                smallestResult = reduce
        }
    }

    return smallestResult
}

fun reduceInput(input: String): String {

    var deleteNext = false
    var hasFiltered = true
    var filtered = input

    while (hasFiltered) {
        val inputSize = filtered.length
        filtered = filtered.filterIndexed { index, c ->

            val hasNext = filtered.length > index + 1

            if (deleteNext) {
                deleteNext = false
                false
            } else if (hasNext
                && isOtherCase(c, filtered[index + 1])
            ) {
                deleteNext = true
                false
            } else {
                true
            }
        }

        hasFiltered = inputSize != filtered.length
    }

    return filtered
}

fun isOtherCase(left: Char, right: Char): Boolean {
    return (left.isLowerCase() && right.isUpperCase() && left.toUpperCase() == right)
            || (left.isUpperCase() && right.isLowerCase() && left.toLowerCase() == right)
}