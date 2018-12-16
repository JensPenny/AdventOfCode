package main

import util.readAsResource
import kotlin.math.min

fun main(args: Array<String>) {
    val lines = readAsResource("day12")
    val plantInput = splitInput(lines)
    doGenerations(plantInput, 1000)
    secondPuzzle()
}

fun secondPuzzle(){
    //Dont even try to copy this, because this wizardry wont work for others
    //gen 98 sum is the first value
    val gen98sum = 6193
    val genstogo = 50000000000 - 98
    val addEachGen = 51  //Each following gen stabilizes
    val result = gen98sum + genstogo * addEachGen
    println("second puzzle: $result")
}

fun doGenerations(plantInput: PlantInput, gens: Long) {
    var generationString1 = plantInput.inputString
    var currentOffset = 0
    (1..gens).forEach { gen ->
        val result = doGeneration(generationString1, plantInput.matchers)
        generationString1 = result.first
        currentOffset -= result.second

        val total = generationString1.mapIndexed { index, c ->
            if (c == '#') currentOffset + index else 0
        }.sum()

        println("$gen - sum:$total- $generationString1 ")
    }
    println("Final offset $currentOffset")
}

fun splitInput(lines: List<String>): PlantInput {
    val input = lines.first().substringAfter(":").trim()

    val plantMatchers = lines
        .minus(lines.first())
        .filter { l -> !l.isBlank() }
        .map { l ->
            PlantMatcher(
                l.substringBefore("=>").trim(),
                l.substringAfter("=>").trim().first()
            )
        }.filter { m -> m.result != '.' }
    return PlantInput(input, plantMatchers)
}

/**
 * Returns the new generation string and the extra offset of this generation
 */
fun doGeneration(generationString: String, matchers: Collection<PlantMatcher>): Pair<String, Int> {

    //pad until first 5 chars are empty pods
    val maxPad = (0..5).firstOrNull {
        generationString[it] == '#'
    }?:0

    var oldString = generationString.padStart(5 - maxPad + generationString.length, '.')
    oldString = oldString.padEnd(oldString.length + 5, '.') //speedup loop for future improvements
    val newString = oldString.toCharArray()

    for (index in 2..oldString.length - 3) {
        val replace = matchers
            .find { ma -> oldString.substring(index - 2, index + 3) == ma.match }
            .let { ma -> ma?.result } ?: '.'
        newString[index] = replace
    }

    //remove ad max 5 empty pots from the end result
    val firstPlant = min(newString.slice(0..10).indexOfFirst { c -> c == '#' }, 10)
    val lastPlant = newString.indexOfLast { c: Char -> c == '#' }
    val slice = newString.slice(firstPlant..lastPlant).toCharArray()
    return Pair(String(slice), maxPad + 5 - firstPlant) //offset voor iteratie 15 nar 16 is nog shit
}

data class PlantInput(
    val inputString: String,
    val matchers: Collection<PlantMatcher>
)

data class PlantMatcher(
    val match: String,
    val result: Char
)