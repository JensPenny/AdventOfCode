package main.onetoten

import util.readAsResource
import java.util.stream.Collectors

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val lines = readAsResource("day8")
    val treeInput = lines.first().split(" ")
    val root = createTree(treeInput.map { s -> s.toInt() })
    val sum = calculateMetadataSum(root.first)
    val stop = System.currentTimeMillis()

    println("Part 1: sum is $sum - found in ${stop - start} ms")
    val value = calculateValue(root.first)
    println("Part 2: value is $value")
}

fun createTree(input: List<Int>): Pair<TreeNode, List<Int>> {
    val header = input.subList(0, 2)
    val amountChildren = header[0]
    val amountMetadata = header[1]

    val node = TreeNode()
    var restInput = input.drop(2)
    for (child in 1..amountChildren) {
        val result = createTree(restInput)
        restInput = result.second
        node.children.add(result.first)
    }

    for (metadata in 1..amountMetadata) {
        node.metaData.add(restInput[metadata - 1])
    }

    return Pair(node, restInput.drop(amountMetadata))
}

fun calculateMetadataSum(node: TreeNode): Int {
    val total = node.metaData.sum()
    val childSum = node.children.stream().map { n -> calculateMetadataSum(n) }.collect(Collectors.toList())

    return total + childSum.sum()
}

fun calculateValue(node: TreeNode) : Int {
    return node.value()
}

class TreeNode {
    val children: MutableList<TreeNode> = arrayListOf()
    val metaData: MutableList<Int> = arrayListOf()

    fun value(): Int {
        return if (children.isEmpty()) {
            metaData.sum()
        } else {
            metaData
                .map { v ->
                    if (children.size < v) {
                        0
                    } else {
                        children[v - 1].value()
                    }
                }.sum()
        }
    }
}