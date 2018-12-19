package main.kotlinECS.components

import main.kotlinECS.Component
import java.awt.Point

//Components used in this assignment
data class Position(val x: Int, val y: Int) : Component {
    fun asPoint(): Point {
        return Point(x, y)
    }
}