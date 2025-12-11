package day_11

import readFile

class Node {
    var previous: MutableList<String> = mutableListOf()
    var next: MutableList<String> = mutableListOf()
}

fun main() {
    val input = readFile("day_11/input.txt")
    val parsed = parseInput(input)
    val p = countPathsWithRequiredNodes(parsed,"out","svr",listOf("dac","fft"))
    println(p)
}

fun parseInput(input: String):Map<String, Node>{
    val lines = input.lines()

    val map: MutableMap<String, Node> = mutableMapOf()

    lines.forEach {
        val split = it.split(":")

        val currentNode = split.first().trim()

        val node = map.getOrPut(currentNode) { Node() }

        val connectedTo = split.last().trim().split(Regex("\\s+"))
        connectedTo.forEach { connection ->
            val connectionNode = map.getOrPut(connection) { Node() }
            connectionNode.previous.add(currentNode)
        }
        node.next.addAll(connectedTo)
        map[currentNode] = node
    }

    return map
}

fun countAllPaths(input: Map<String, Node>, destinationNodeName: String, startNodeName: String): Long {
    val visitedNodes = mutableSetOf<String>()
    val nodeValidPaths = mutableMapOf<String, Long>()

    fun dfs(currentNodeName: String): Long {
        if (currentNodeName == startNodeName) return 1L
        nodeValidPaths[currentNodeName]?.let { return it }

        val currentNode = input[currentNodeName] ?: return 0L
        var count = 0L

        for (prevNodeName in currentNode.previous) {
            if (prevNodeName !in visitedNodes) {
                visitedNodes.add(prevNodeName)
                count += dfs(prevNodeName)
                visitedNodes.remove(prevNodeName)
            }
        }

        if (currentNode.previous.none { it in visitedNodes }) {
            nodeValidPaths[currentNodeName] = count
        }
        return count
    }

    visitedNodes.add(destinationNodeName)
    return dfs(destinationNodeName)
}

fun countPathsWithRequiredNodes(input: Map<String, Node>, destinationNodeName: String, startNodeName: String,requiredNodes: List<String> ): Long {
    val visitedNodes = mutableSetOf<String>()
    val nodeValidPaths = mutableMapOf<Pair<String,Set<String>>, Long>()

    fun dfs(currentNodeName: String): Long {
        if (currentNodeName == startNodeName) {
                return if(visitedNodes.containsAll(requiredNodes)) 1L else 0L
        }

        val visitedRequiredNodes = visitedNodes.filter { it in requiredNodes  }.toSet()
        nodeValidPaths[ currentNodeName to visitedRequiredNodes ]?.let{return it }


        val currentNode = input[currentNodeName] ?: return 0L
        var count = 0L

        for (prevNodeName in currentNode.previous) {
            if (prevNodeName !in visitedNodes) {
                visitedNodes.add(prevNodeName)
                count += dfs(prevNodeName)
                visitedNodes.remove(prevNodeName)
            }
        }

        if (currentNode.previous.none { it in visitedNodes } ) {
            nodeValidPaths[ currentNodeName to visitedRequiredNodes ] = count
        }
        return count
    }

    visitedNodes.add(destinationNodeName)
    return dfs(destinationNodeName)
}