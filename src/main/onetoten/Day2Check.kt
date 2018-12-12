package main.onetoten

import util.readAsResource
import java.util.stream.Collectors

fun main(args: Array<String>) {
    firstPuzzle()
    secondPuzzle()
}

private fun firstPuzzle() {
    val readlines = readAsResource("day2")

    val charAmount = readlines
        .stream()
        .map { str ->
            str.groupingBy { it }
                .eachCount()
                .filterValues { amount -> amount == 2 || amount == 3 }
        }
        .collect(Collectors.toList())

    val amount2 = charAmount.count { kv -> kv.containsValue(2) }
    val amount3 = charAmount.count { kv -> kv.containsValue(3) }

    println("checksum " + amount2 * amount3)
}

private fun secondPuzzle() {
    val readLines = readAsResource("day2")

    val start = System.currentTimeMillis()
    val linesWithScores = readLines.stream()
        .map { line ->
            val toCheck = readLines.minus(line)
            val groupBy = toCheck.groupBy({ it }, { it.filterIndexed { index, c -> line[index] != c }.length })
            val asPairs = groupBy.map { e -> Pair(e.key, e.value.min()) }
            asPairs
                .stream()
                .min(Comparator.comparingInt { s -> s.second!! })
        }
        .collect(Collectors.toList())

    val minimumPair = linesWithScores.minBy { s -> s.get().second!! }
    val minimumPairIndex = linesWithScores.indexOf(minimumPair)
    val originalLine = readLines.get(minimumPairIndex)
    val sameChars = originalLine.filterIndexed { index, c -> minimumPair!!.get().first[index] == c }
    val stop = System.currentTimeMillis() - start
    println("Solution found in $stop ms")

    println("originalLine $originalLine")
    println("secondary    ${minimumPair!!.get().first}")
    println("samechars    $sameChars")
}