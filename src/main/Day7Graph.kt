package main

import util.readAsResource
import java.lang.StringBuilder
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val lines = readAsResource("day7")
    val nodes = createNodes(lines)
    val part1 = solvePart1(nodes)
    val time = System.currentTimeMillis() - start
    println("Part 1 $part1 in $time ms")

    val secondTiming = System.currentTimeMillis()
    val part2 = solvePart2(nodes, 10000, 5)
    val part2time = System.currentTimeMillis() - secondTiming
    println("Part 2 $part2 in $part2time ms")
}

fun createNodes(lines: List<String>): Collection<Node> {

    val createdNodes: MutableMap<String, Node> = hashMapOf()
    val nodeInfo = lines.map { l ->
        val parts = Pattern.compile("\\s[A-Z]\\s").toRegex().findAll(l).map { r -> r.value }.toList()
        val preReq = parts[0].trim()
        val child = parts[1].trim()
        Pair(preReq, child)
    }

    for (info in nodeInfo) {
        val preReqNode = createdNodes.getOrPut(info.first) { Node(nodeName = info.first) }
        val childNode = createdNodes.getOrPut(info.second) { Node(info.second) }
        preReqNode.children.add(childNode)
        childNode.preReqs.add(preReqNode)
    }

    return createdNodes.values
}

fun solvePart1(nodes: Collection<Node>): String {
    val nodesToSolve: MutableSet<Node> = HashSet(nodes)
    val solvedNodes: MutableSet<Node> = HashSet()

    var solutionString = ""
    while (nodes.size != solutionString.length) {
        val noPrereqNodes = nodesToSolve
            .filter { n ->
                n.preReqs.filter { pre -> !solvedNodes.contains(pre) }.isEmpty()
            }
            .sortedBy { n -> n.nodeName }
            .first()
        nodesToSolve.remove(noPrereqNodes)
        solvedNodes.add(noPrereqNodes)
        solutionString += noPrereqNodes.nodeName

    }

    return solutionString
}

fun solvePart2(nodes: Collection<Node>,
               secondsToTest: Int,
               amountWorkers : Int): Int {
    val workers = IntRange(1, amountWorkers).map { i ->
        Worker(i.toString())
    }

    val nodesToSolve: MutableSet<Node> = HashSet(nodes)
    val solvedNodes: MutableSet<Node> = HashSet()
    val nodesInProgress: MutableSet<Node> = HashSet()
    var solutionString = ""

    println(" SEC\tW1\tW2\tW3\tW4\tW5")
    for (second in 0..secondsToTest) {
        if (nodes.size != solutionString.length) {
            val noPrereqNodes = nodesToSolve
                .filter { n ->
                    n.preReqs.none { pre -> !solvedNodes.contains(pre) }
                }
                .filter { n -> !nodesInProgress.contains(n) }
                .sortedBy { n -> n.nodeName }

            //Attempt to add the nodes to a worker
            noPrereqNodes.forEach { node ->
                val worker = workers.firstOrNull { w -> w.isAvailable() }
                if (worker != null) {
                    worker.startWork(node)
                    nodesInProgress.add(node)
                }
            }

            val linePrinter = StringBuilder("$second\t\t")
            //Advance all the workers
            workers.forEach {
                if (it.currentNode != null) {
                    val node = it.currentNode
                    val isAvailable = it.reduceWork()
                    if (isAvailable){
                        nodesToSolve.remove(node)
                        solvedNodes.add(node!!)
                        solutionString += node.nodeName
                    }
                    linePrinter.append("${node!!.nodeName} ${it.workLeft}").append("\t")
                } else {
                    linePrinter.append(".\t")
                }
            }
            println(linePrinter)

            if (solutionString.length == nodes.size){
                return second + 1 //offset for 0 based second stuff
            }
        }
    }

    return 0
}

class Node(
    val nodeName: String,
    val preReqs: MutableSet<Node> = hashSetOf(),
    val children: MutableSet<Node> = hashSetOf()
)

class Worker(
    val name: String,
    var currentNode: Node? = null,
    var workLeft: Int = 0
) {

    fun startWork(node: Node) {
        currentNode = node
        workLeft = node.nodeName.first().toInt() - 4
    }

    fun reduceWork(): Boolean {
        workLeft--
        val isAvailable = isAvailable()
        if (isAvailable) {
            currentNode = null
        }
        return isAvailable
    }

    fun isAvailable(): Boolean {
        return workLeft == 0
    }
}