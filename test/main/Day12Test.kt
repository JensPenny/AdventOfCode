package main

val testInput = "initial state: #..#.#..##......###...###\n" +
        "\n" +
        "...## => #\n" +
        "..#.. => #\n" +
        ".#... => #\n" +
        ".#.#. => #\n" +
        ".#.## => #\n" +
        ".##.. => #\n" +
        ".#### => #\n" +
        "#.#.# => #\n" +
        "#.### => #\n" +
        "##.#. => #\n" +
        "##.## => #\n" +
        "###.. => #\n" +
        "###.# => #\n" +
        "####. => #"

fun main(args: Array<String>) {
    val input = splitInput(testInput.split('\n'))
    println(input)
    doGenerations(input, 20)
}