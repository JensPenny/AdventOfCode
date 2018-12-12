package main

import main.onetoten.TreeNode
import main.onetoten.calculateMetadataSum
import main.onetoten.calculateValue
import main.onetoten.createTree
import kotlin.test.assertEquals

fun main(args: Array<String>) {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
    val treeInput = input.split(" ")
    val root = createTree(treeInput.map { s -> s.toInt() })

    firstTest(root)
    secondTest(root.first)
}

fun firstTest(root: Pair<TreeNode, List<Int>>) {

    val total = calculateMetadataSum(root.first)
    assertEquals(138, total)
    assert(root.second.isEmpty())
}

fun secondTest(root: TreeNode) {
    val value = calculateValue(root)
    assertEquals(66, value)
}