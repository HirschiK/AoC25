package day_12

import readFile

fun main() {
    val input = readFile("day_12/input.txt")
    val parsed = parseInput(input)
    val a = partOne(parsed.first,parsed.second)
}

fun parseInput(input: String): Pair<Array<Array<Array<Boolean>>>, Array<Pair<Pair<Int, Int>, Array<Int>>>> {
    val presentsSplit = input.substring(0..input.lastIndexOf("\n\n")).trim()
    val regionsSplit = input.substring(input.lastIndexOf("\n\n")).trim()

    val presents: MutableList<Array<Array<Boolean>>> = mutableListOf()
    val regions: MutableList<Pair<Pair<Int,Int>, Array<Int>>> = mutableListOf()

    val presentString =   presentsSplit.split("\n\n")
    presentString.forEach { present ->
        val presentLines = present.lines().toMutableList()
        presentLines.removeFirst()
        presentLines.removeIf { it.trim().isEmpty() }
        var presentArr: Array<Array<Boolean>> = Array<Array<Boolean>>(presentLines.size){ Array<Boolean>(presentString.first().lines().last().length){false} }
        presentLines.forEachIndexed { idx, presentLine ->
                presentArr[idx] = Array<Boolean>(presentLine.length) { false }
                presentLine.forEachIndexed { jdx, char ->
                    presentArr[idx][jdx] = char == '#'
            }
        }
        presents.add(presentArr)
    }
    regionsSplit.trim().lines().forEach { region ->
        val (size, instructionsString) = region.split(":")
        val instructions  = instructionsString.trim().split(" ").map {
            if(!it.isEmpty()) it.trim().toInt()
            else 0
        }
        regions.add(Pair(Pair(size.split("x").first().toInt(),size.split("x").last().toInt()),instructions.toTypedArray()))
    }
    return Pair(presents.toTypedArray(),regions.toTypedArray())
}

fun partOne(presents: Array<Array<Array<Boolean>>>,regions: Array<Pair<Pair<Int,Int>, Array<Int>>>): Int{
    val sizes = presents.map{present -> present.size * present.first().size}
    val validRegions = regions.fold(0){ acc , region ->
        val regionSize = region.first.first * region.first.second
        val presentMaxSize = region.second.foldIndexed(0) { presentIndex, acc , count  ->
            acc + sizes[presentIndex] * count
        }
        if(regionSize >= presentMaxSize) acc +1
        else acc
    }
    return validRegions
}