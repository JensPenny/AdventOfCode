package main

fun main(args: Array<String>) {

    val recipe = mutableListOf(3,7)
    //First puzzle
    val result = firstPuzzle(recipe, 30121)
    println(result)

    val size = secondPuzzle(recipe, "030121")
    println("2nd puzzle $size")
}

fun firstPuzzle(input: List<Int>, generations: Int): String {
    val recipe = input.toMutableList()
    val elves = intArrayOf(0, 1)
    var currentResult = GenerationResult(recipe, elves)
    println("Gen 0: ${currentResult.recipeResult}")
    for (gen in 1..generations + 10) {
        currentResult = makeRecipes(currentResult.recipeResult, currentResult.elvesResult)
    }

    return currentResult.recipeResult.drop(generations).take(10).joinToString (separator = "") { i -> i.toString()}
}

fun secondPuzzle(input: MutableList<Int>, toFind: String): Int {
    val elves = intArrayOf(0, 1)
    val recipe = input.toMutableList()
    var currentResult = GenerationResult(recipe, elves)
    println("Gen 0: ${currentResult.recipeResult}")

    while (true) {
        currentResult = makeRecipes(currentResult.recipeResult, currentResult.elvesResult)
        val foundIndex = currentResult.recipeResult
            .takeLast(toFind.length + 1)
            .joinToString (separator = ""){ c -> c.toString() }
            .indexOf(toFind)
        if (foundIndex > -1) {
            return foundIndex + currentResult.recipeResult.size - toFind.length - 1
        }
    }
}

fun makeRecipes(input: MutableList<Int>, elves: IntArray): GenerationResult {
    val recipes = elves.map { e -> input[e] }
        .map { s -> s.toString().toInt() }

    val newRecipe = recipes.sum()
    if (newRecipe >= 10) {
        val toAdd = (newRecipe / 10) % 10
        input.add(toAdd)
    }
    val toAdd = newRecipe % 10
    input.add(toAdd)

    //Give the elves new indices for their new recipes
    val updatedElves = elves
        .mapIndexed { index, e ->
            var newIndex = e + recipes[index] + 1
            while (newIndex >= input.size){
                newIndex -= input.size
            }
            newIndex //Used to be a modulo, but this is faster for bigger strings
        }
        .toIntArray()
    return GenerationResult(input, updatedElves)
}

class GenerationResult(
    val recipeResult: MutableList<Int>,
    val elvesResult: IntArray
)