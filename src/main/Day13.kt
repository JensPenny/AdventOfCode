package main

import main.Direction.*
import main.kotlinECS.Component
import main.kotlinECS.Entity
import main.kotlinECS.EntitySystem
import util.readAsResource
import java.util.*
import java.util.EnumSet.*

fun main(args: Array<String>) {
    val lines = readAsResource("day13test")
    val entities = parseEntities(lines)
    entities.forEach { e ->
        println(e.tag)
        e.components.forEach { c -> println(c) }
    }

    val systems = arrayListOf<EntitySystem>()
}

fun parseEntities(lines: List<String>): Collection<Entity> {
    val entities: MutableCollection<Entity> = arrayListOf()
    for (y in 0 until lines.size) {
        for (x in 0 until lines[y].length) {
            val currentChar = lines[y][x]
            val position = Position(x, y)
            when (currentChar) {
                '/' -> {
                    //Check neighbours to find the directions
                    if (x == 0 ||
                        (x + 1 != lines[y].length
                                && lines[y][x + 1] == '-')
                    ) {
                        entities.add(Entity("rail", position, Track(of(EAST, SOUTH))))
                    } else {
                        entities.add(Entity("rail", position, Track(of(NORTH, WEST))))
                    }
                }
                '\\' -> {
                    //Check neighbours to find the directions
                    if (x == 0
                        || (x + 1 != lines[y].length
                                && lines[y][x + 1] == '-')
                    ) {
                        entities.add(Entity("rail", position, Track(of(NORTH, EAST))))
                    } else {
                        entities.add(Entity("rail", position, Track(of(WEST, SOUTH))))
                    }
                }
                '|' -> entities.add(Entity("rail", position, Track(of(NORTH, SOUTH))))
                '-' -> entities.add(Entity("rail", position, Track(of(WEST, SOUTH))))
                '+' -> {
                    entities.add(Entity("rail", position, Track(of(NORTH, SOUTH))))
                    entities.add(Entity("rail", position, Track(of(EAST, WEST))))
                }
                '>' -> {
                    entities.add(Entity("rail", position, Track(of(EAST, WEST))))
                    entities.add(Entity("cart", Cart(EAST, position)))
                }
                '<' -> {
                    entities.add(Entity("rail", position, Track(of(EAST, WEST))))
                    entities.add(Entity("cart", Cart(WEST, position)))
                }
                '^' -> {
                    entities.add(Entity("rail", position, Track(of(NORTH, SOUTH))))
                    entities.add(Entity("cart", Cart(NORTH, position)))
                }
                'v' -> {
                    entities.add(Entity("rail", position, Track(of(NORTH, SOUTH))))
                    entities.add(Entity("cart", Cart(SOUTH, position)))
                }
                else -> {
                    null
                }
            }
        }
    }

    return entities
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}

//Components used in this assignment
data class Position(val x: Int, val y: Int) : Component

data class Track(
    val directions: EnumSet<Direction>
) : Component {

    fun getNextDirection(direction: Direction) {
        directions.filter { d -> d == direction }.first()
    }
}

data class Cart(
    var direction: Direction, //Current direction: can change by rails and stuff
    var position: Position   //Current position, will change
) : Component {

    fun updateDirection(direction: Direction) {
        this.direction = direction
    }

    //Update the position, not clearly ECS-y, but I feel like its clear and could be moved to a systems class either way
    fun updatePosition() {
        position = when (direction) {
            NORTH -> Position(position.x, position.y - 1)
            EAST -> Position(position.x + 1, position.y)
            SOUTH -> Position(position.x, position.y + 1)
            WEST -> Position(position.x - 1, position.y)
        }
    }
}

//System that progresses the state of the actual railsystem
class ProgressSystem : EntitySystem {
    override fun applySystem(entities: Collection<Entity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

//Backup system: Check that there are no derailed carts on the system
class DerailmentSystem : EntitySystem {
    override fun applySystem(entities: Collection<Entity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

//System that checks the rail network for cart crashes
class CrashWarningSystem : EntitySystem {
    override fun applySystem(entities: Collection<Entity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}