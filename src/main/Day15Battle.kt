package main

import main.kotlinECS.Component
import main.kotlinECS.Entity
import main.kotlinECS.components.Position

val testInput =
            "#######   \n" +
            "#.G...#   \n" +
            "#...EG#   \n" +
            "#.#.#G#   \n" +
            "#..G#E#   \n" +
            "#.....#   \n" +
            "#######"

fun main(args: Array<String>) {
    val entities = readInput(testInput.split('\n'))
    println("${entities.size} entities created")
}

fun readInput(lines: List<String>) : Collection<Entity> {
    val entities = mutableListOf<Entity>()
    for (y in 0 until lines.size) {
        for (x in 0 until lines[y].length) {
            val pos = Position(x, y)
            val char = lines[y][x]

            when(char){
                '#' -> entities.add(Entity("wall", Wall(pos)))
                'G' -> entities.add(Entity("goblin", Creature(200, pos, 3), Goblin()))
                'E' -> entities.add(Entity("elf", Creature(200, pos, 3), Elf()))
            }
        }
    }
    return entities
}

data class Wall(val position: Position) : Component
data class Creature(
    var hp: Int,
    var position: Position,
    val power: Int
) : Component {

    fun attack(enemy: Creature) {
        if (!isDead()) {                 //Dead creatures don't kill
            enemy.hp -= this.power
        }
    }

    fun isDead(): Boolean {
        return hp <= 0
    }
}

class Elf : Component
class Goblin : Component