package main

import main.Direction.*
import main.kotlinECS.Component
import main.kotlinECS.Entity
import main.kotlinECS.EntitySystem
import main.kotlinECS.components.Position
import util.readAsResource
import java.awt.Point
import java.util.*
import java.util.EnumSet.*

fun main(args: Array<String>) {
    val lines = readAsResource("day13")
    val entities = parseEntities(lines)

    val railNet = RailNetwork(entities)
    val progressSystem = ProgressSystem(railNet)

    (1..100000).forEach { gen ->
        println("Starting gen $gen")
        progressSystem.applySystem(entities)
    }
}

fun parseEntities(lines: List<String>): MutableCollection<Entity> {
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
                                && (lines[y][x + 1] == '-'
                                || lines[y][x + 1] == '>'
                                || lines[y][x + 1] == '+'
                                || lines[y][x + 1] == '<')
                                )
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
                                && (lines[y][x + 1] == '-'
                                || lines[y][x + 1] == '>'
                                || lines[y][x + 1] == '+'
                                || lines[y][x + 1] == '<')
                                )
                    ) {
                        entities.add(Entity("rail", position, Track(of(NORTH, EAST))))
                    } else {
                        entities.add(Entity("rail", position, Track(of(WEST, SOUTH))))
                    }
                }
                '|' -> entities.add(Entity("rail", position, Track(of(NORTH, SOUTH))))
                '-' -> entities.add(Entity("rail", position, Track(of(EAST, WEST))))
                '+' -> {
                    entities.add(Entity("crossing", position, Track(of(NORTH, EAST, SOUTH, WEST))))
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
    NORTH, EAST, SOUTH, WEST;

    companion object {
        fun turn(
            current: Direction,
            left: Boolean
        ): Direction {
            val ordinalOffset = if (left) -1 else 1
            return Direction.values()[(4 + current.ordinal + ordinalOffset) % 4]
        }
    }

}

data class Track(
    val directions: EnumSet<Direction>
) : Component {
    fun next(current: Direction): Direction {
        val comingFrom = Direction.values()[(4 + current.ordinal + 2) % 4]
        return directions.minus(comingFrom).first()
    }
}

data class Cart(
    var direction: Direction, //Current direction: can change by rails and stuff
    var position: Position   //Current position, will change
) : Component {
    var crossings = 0

    /**
     * Change direction after crossing a track
     */
    fun updateDirection(currentTrack: Track) {
        if (currentTrack.directions.size == Direction.values().size) {
            crossings++
            val crossingDirection = crossings % 3
            if (crossingDirection == 1) {
                this.direction = Direction.turn(this.direction, true)
            } else if (crossingDirection == 0) {
                this.direction = Direction.turn(this.direction, false)
            }
        } else {
            this.direction = currentTrack.next(this.direction)
        }
    }

    /**
     * Update the position of the cart
     */
    fun updatePosition() {
        position = when (direction) {
            NORTH -> Position(position.x, position.y - 1)
            EAST -> Position(position.x + 1, position.y)
            SOUTH -> Position(position.x, position.y + 1)
            WEST -> Position(position.x - 1, position.y)
        }
    }
}

class RailNetwork(entities: Collection<Entity>) {
    val railNet: Map<Point, Track>

    init {
        val entitiesWithTrack = entities.filter { e -> e.components.any { c -> c is Track } }
        val railNet: MutableMap<Point, Track> = hashMapOf()

        entitiesWithTrack.forEach { e ->
            val position = e.components.first { c -> c is Position }
            val point = (position as Position).asPoint()
            val track = e.components.first { c -> c is Track }
            railNet[point] = track as Track
        }

        this.railNet = railNet
    }
}

//System that progresses the state of the actual railsystem
//Requires the initial rail system to construct a positionmap
class ProgressSystem(
    val railNetwork: RailNetwork
) : EntitySystem {
    override fun applySystem(entities: MutableCollection<Entity>) {
        val carts = entities
            .filter { e -> e.components.any { c -> c is Cart } }
            .map { e -> e.components.first { c -> c is Cart } }
            .map { c -> c as Cart }
            .sortedWith(compareBy({it.position.y}, {it.position.x}))

        if (carts.size == 1){
            println("last cart location ${carts.first().position}")
            System.exit(0)
        }

        val foundCollision: Position? = null
        for (cart in carts) {
            cart.updatePosition()
            val network = railNetwork.railNet[cart.position.asPoint()]
            if (network == null){
                println("Error")
            }
            cart.updateDirection(network!!)

            //Check for new collisions
            val candidates = carts.minus(cart)
            val collision = candidates.map { cl ->
                cl.position
            }.find { pos -> pos.asPoint() == cart.position.asPoint() }
            if (collision != null){
                println("Collision: $collision")
                val crashed = carts.filter { c -> c.position == collision }
                val filtered = entities.filter { e -> e.components.intersect(crashed).isNotEmpty() }
                entities.removeAll(filtered)
            }
            println("cart ${cart.position} ${cart.direction}")
        }

        println("Collision: ${foundCollision ?: "none"}")
    }
}