package main

import main.onetoten.findMinimal
import main.onetoten.reduceInput
import kotlin.test.assertEquals

fun main(args: Array<String>){
    Day5Test().test()
}

class Day5Test{
    fun test(){
        val testString = "dabAcCaCBAcCcaDA"
        val reduced = reduceInput(testString)

        assertEquals("dabCBAcaDA", reduced)

        val minimal = findMinimal(testString)
        assertEquals("daDA", minimal)
    }
}
