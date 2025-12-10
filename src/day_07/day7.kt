package day_07

import readFile

fun main() {
    val input = readFile("day_7/input.txt")
    partTwo((parseInput(input)))
}

fun parseInput(input: String): Pair<Int,Array<Array<Boolean>>>{
    val lines = input.lines()
    val width = lines.first().length
    val start = lines.first().indexOf("S")
    val arr = Array (lines.size) { idx ->
        Array<Boolean>(width) { jdx ->
            lines[idx][jdx] == '^'
        }
    }
    return Pair(start,arr)
}

fun partOne(input: Pair<Int,Array<Array<Boolean>>>){
    var beamLocations: MutableList<Int> = mutableListOf(input.first)

    var splits = 0
    input.second.forEach { line ->
        val splitBeamLocations = beamLocations.toMutableSet()
        beamLocations.forEach() {beam ->
            if(line[beam]) {
                splits++
                splitBeamLocations.remove(beam)
                splitBeamLocations += beam + 1
                splitBeamLocations += beam - 1
            }
        }
        beamLocations = splitBeamLocations.toMutableList()
    }
    println(splits)
}

fun partTwo(input: Pair<Int,Array<Array<Boolean>>>){
    val beamMap: MutableMap<Int, Long> = mutableMapOf(input.first to 1)
    input.second.forEach { line ->
        beamMap.toList().forEach { beam ->
            if (line[beam.first]) {
                beamMap.remove(beam.first)
                beamMap[beam.first - 1] = (beamMap[beam.first - 1] ?: 0L) + beam.second
                beamMap[beam.first + 1] = (beamMap[beam.first + 1] ?: 0L) + beam.second
            }
        }

    }
    println(beamMap.values.sum())
}


