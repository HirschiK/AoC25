package day_9

import readFile
import kotlin.math.abs

fun main() {
    val input = readFile("day_9/input.txt")
    val parsed = parseInput(input)
    partTwo(parsed)
}

fun parseInput(input: String): Array<Pair<Int,Int>>{
    val lines = input.lines()
    val coords = mutableListOf<Pair<Int,Int>>()
    lines.forEach { line ->
        val (x,z) = line.split(",").map { it.trim().toInt() }
        coords.add(Pair(x,z))
    }
    coords.sortBy { it.first * it.first + it.second * it.second }
    return coords.toTypedArray()
}

fun partOne(coords: Array<Pair<Int, Int>>){
    println(getEachLargest(coords).sortedByDescending { it.first }.first())
}

fun partTwo(coords: Array<Pair<Int, Int>>){
    val bounds = getBounds(coords)
    println(getEachLargestInBounds(coords,bounds))
}

fun getEachLargest(coords: Array<Pair<Int,Int>>): Array<Pair<Long,Pair<Pair<Int,Int>,Pair<Int,Int>>>>{
    val largest = mutableSetOf<Pair<Long,Pair<Pair<Int,Int>,Pair<Int,Int>>>>()
    coords.forEach { coordinate ->
        var currentLargest: Pair<Long,Pair<Int,Int>> = Pair(Long.MIN_VALUE,Pair(coordinate.first,coordinate.second))
        coords.forEach { cmpCoord ->
            val area = (abs(coordinate.first - cmpCoord.first) + 1).toLong() * (abs(coordinate.second - cmpCoord.second) + 1).toLong()
            if(area > currentLargest.first) currentLargest = Pair(area,Pair(cmpCoord.first,cmpCoord.second))
        }
        val (a,b) = listOf<Pair<Int,Int>>(coordinate,currentLargest.second).sortedBy { it.first + it.second }
        largest.add(Pair(currentLargest.first,Pair(a,b)))
    }

    return largest.toTypedArray()
}

fun getEachLargestInBounds(coords: Array<Pair<Int,Int>>, bounds: Set<Pair<IntRange,IntRange>>): Pair<Long,Pair<Pair<Int,Int>,Pair<Int,Int>>>{
    var largestArea: Pair<Long,Pair<Pair<Int,Int>,Pair<Int,Int>>> = Pair(Long.MIN_VALUE, Pair(Pair(0,0),Pair(0,0)))

    val verticalBounds = bounds.filter { it.first.first == it.first.last }.toSet()
    val horizontalBounds = bounds.filter { it.second.first == it.second.first }.toSet()
    coords.forEach coordinateCheck@ {coordinate ->
        val areas : MutableSet<Pair<Long,Pair<Int,Int>>> = mutableSetOf()
        coords.forEach { cmpCoord ->
            val area = (abs(coordinate.first - cmpCoord.first) + 1).toLong() * (abs(coordinate.second - cmpCoord.second) + 1).toLong()
            if(area > largestArea.first) {
                areas.add(Pair(area, Pair(cmpCoord.first, cmpCoord.second)))
            }
        }
        val areasSorted = areas.sortedByDescending { it.first }
        areasSorted.forEach { area ->
            if(area.first == 33L){
                print("")
            }
            val areaCorners: Array<Pair<Int,Int>> = arrayOf(
                area.second, coordinate, Pair(area.second.first,coordinate.second),Pair(coordinate.first,area.second.second)
            )
            if(pointInBounds(areaCorners[2],bounds,verticalBounds) && pointInBounds(areaCorners[3],bounds,verticalBounds)){
                val areaBounds: Set<Pair<IntRange, IntRange>> = getBounds(areaCorners)
                var areaInBounds = true

                for (bound in areaBounds){
                    if(bound.second.first == bound.second.last){
                        val halfwayPoint = Pair(((bound.first.last-bound.first.first)/2+bound.first.first),bound.second.first)
                        if(rangeIsInterupted(bound,verticalBounds) || !pointInBounds(halfwayPoint,bounds,verticalBounds)){
                            areaInBounds = false
                            break
                        }
                    }
                    else {
                        val halfwayPoint = Pair(bound.first.first,((bound.second.last-bound.second.first)/2)+bound.second.first)
                        if(rangeIsInterupted(bound,horizontalBounds ) || !pointInBounds(halfwayPoint,bounds,verticalBounds)){
                            areaInBounds = false
                            break
                        }
                    }
                }
                if (areaInBounds) {
                    largestArea = Pair(area.first, Pair(coordinate, area.second))
                    return@coordinateCheck
                }
            }
        }
    }
    return largestArea
}

fun getBounds(coords: Array<Pair<Int,Int>>): Set<Pair<IntRange,IntRange>>{
    val bounds: MutableSet<Pair<IntRange,IntRange>> = mutableSetOf()

    val groupedX = coords.groupBy { it.first }
    for ((x,points) in groupedX){
        var copy = points
        while(copy.isNotEmpty()){
            val current = copy.take(2)
            copy = copy.drop(2)
            val yValues = current.map { it.second }
            val minY = yValues.minOrNull()!!
            val maxY = yValues.maxOrNull()!!
            bounds.add(Pair(x..x, minY..maxY))
        }
    }

    val groupedY = coords.groupBy { it.second }
    for ((y,points) in groupedY){
        var copy = points
        while(copy.isNotEmpty()){
            val current = copy.take(2)
            copy = copy.drop(2)
            val yValues = current.map { it.first }
            val minX = yValues.minOrNull()!!
            val maxX = yValues.maxOrNull()!!
            bounds.add(Pair(minX..maxX, y..y))
        }
    }

    return bounds
}

fun pointInBounds(point:Pair<Int,Int>, bounds: Set<Pair<IntRange, IntRange>>, verticalBounds: Set<Pair<IntRange, IntRange>>): Boolean{
    bounds.forEach { bound ->
        if (point.first in bound.first && point.second in bound.second) return true
    }

    var hitCount = 0
    for (bound in verticalBounds) {
        val x = bound.first.first
        val yRange = bound.second
        if (x > point.first && point.second in yRange) hitCount++
    }
    return (hitCount % 2) == 1
}

fun rangeIsInterupted(range:Pair<IntRange, IntRange>, perpedicularRanges: Set<Pair<IntRange, IntRange>>): Boolean{
    if(range.first.first != range.first.last){
        val y = range.second.first
        val filtered = perpedicularRanges.filter {
            it.second.contains(y)
        }.filter { it.second.first != y && it.second.last!= y }

        val checkRange = range.first.first+1..range.first.last-1
        filtered.forEach {
            if ( checkRange.contains(it.first.first) ) return true
        }
    }
    else{
        val x = range.first.first
        val filtered = perpedicularRanges.filter {
            it.first.contains(x)
        }.filter { it.first.first != x && it.first.last!= x }

        val checkRange = range.second.first+1..range.second.last-1
        filtered.forEach {
            if ( checkRange.contains(it.second.first) ) return true
        }
    }
    return false
}