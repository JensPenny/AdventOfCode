package main.kotlinECS

interface EntitySystem {
    fun applySystem(entities: MutableCollection<Entity>)
}