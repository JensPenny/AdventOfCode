package main.kotlinECS

interface EntitySystem {
    fun applySystem(entities: Collection<Entity>)
}