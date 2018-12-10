package main

import util.readAsResource
import java.math.BigInteger
import java.util.regex.Pattern
import java.util.stream.Collectors
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    val input = readAsResource("day9")
    val gameInput = readInput(input.first())
    firstPuzzle(gameInput)
    secondPuzzle(gameInput)
}

private fun firstPuzzle(gameInput: Pair<Int, Int>) {
    val players = calculateWithLinks(gameInput.first, gameInput.second)
    maxScore(players)
}

private fun secondPuzzle(gameInput: Pair<Int, Int>) {
    val players = calculateWithLinks(gameInput.first, gameInput.second * 100)
    maxScore(players)
}

fun calculateWithLinks(players: Int, marbles: Int): Collection<Player>{
    val start = System.currentTimeMillis()
    val players = (1..players).map { playerId -> Player("Player $playerId") }

    val zeroMarble = Link(0) //Add the first marble
    val oneMarble = Link(1)  //Add the second marble to get a decent list going where the size > 1
    zeroMarble.previous = oneMarble
    zeroMarble.next = oneMarble
    oneMarble.previous = zeroMarble
    oneMarble.next = zeroMarble

    var currentMarble = oneMarble
    var stepCounter = 2
    (2..marbles).forEach { marble ->
        if (stepCounter == 23) {
            var playerIndex = marble % players.size - 1
            if (playerIndex < 0) {
                playerIndex += players.size
            }
            stepCounter = 0
            val currentPlayer = players[playerIndex]
            currentPlayer.addScore(marble)
            for(jump in 0..6){
                currentMarble = currentMarble.previous!!
            }
            currentPlayer.addScore(currentMarble.value)
            currentMarble = currentMarble.remove()
        } else {
            currentMarble = currentMarble.next!!  //2 jumps through the chain
            val newMarble = Link(marble)
            currentMarble.add(newMarble)
            currentMarble = newMarble
        }
        stepCounter++

        /*var printMarble = zeroMarble
        for (step in 0..marble){
            print("${printMarble.value} ")
            printMarble = printMarble.next!!
        }
        println()*/

        //println("step $marble" + placedMarbles.toString())
    }

    println("Steps created in ${System.currentTimeMillis() - start} ms")
    return players
}

fun calculateGame(players: Int, marbles: Int): Collection<Player> {
    val start = System.currentTimeMillis()
    val players = (1..players).map { playerId -> Player("Player $playerId") }
    val placedMarbles = ArrayList<Int>()
    placedMarbles.add(0) //Add the first marble
    placedMarbles.add(1) //Add the second marble to get a decent list going where the size > 1
    var currentIndex = 1
    var stepCounter = 2
    (2..marbles).forEach { marble ->
        if (stepCounter == 23) {
            var playerIndex = marble % players.size - 1
            if (playerIndex < 0) {
                playerIndex += players.size
            }
            stepCounter = 0
            val currentPlayer = players[playerIndex]
            currentPlayer.addScore(marble)
            var toRemove = currentIndex - 7
            if (toRemove < 0) {
                toRemove += placedMarbles.size
            }
            currentPlayer.addScore(placedMarbles[toRemove])
            placedMarbles.removeAt(toRemove)
            currentIndex = toRemove
        } else {
            currentIndex += 2
            if (currentIndex > placedMarbles.size) {
                currentIndex -= (placedMarbles.size)
            }

            placedMarbles.add(currentIndex, marble)
        }
        stepCounter++

        //println("step $marble" + placedMarbles.toString())
    }

    println("Steps created in ${System.currentTimeMillis() - start} ms")
    return players
}

/**
 * read input and return a pair where:
 * - the first value is the amount of players
 * - the second value is the maximum marble
 */
fun readInput(input: String): Pair<Int, Int> {
    val numberPattern = Pattern.compile("-?\\d+")
    val matched = numberPattern.matcher(input).results().map { r -> r.group() }.collect(Collectors.toList())
    return Pair(matched[0].toInt(), matched[1].toInt())
}

fun maxScore(players: Collection<Player>): BigInteger {
    val maxPlayer = players.maxBy { p -> p.score }!!
    println("${maxPlayer.id} has score ${maxPlayer.score}")
    return maxPlayer.score
}

class Player(val id: String) {
    var score = BigInteger.valueOf(0)

    fun addScore(toAdd: Int) {
        score = score.add(toAdd.toBigInteger())
    }
}

class Link(val value: Int) {
    var previous: Link? = null
    var next: Link? = null

    fun remove() : Link{
        if (previous != null) {
            previous!!.next = next
        }
        if (next != null) {
            next!!.previous = previous
        }
        return next!!
    }

    fun add(newMarble: Link) {
        val tempNext = next
        next = newMarble
        newMarble.previous = this
        newMarble.next = tempNext
        tempNext!!.previous = newMarble
    }
}
