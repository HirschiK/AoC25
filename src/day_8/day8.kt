package day_8

import readFile
import kotlin.math.pow

fun main() {
    val input = readFile("/day_8/input.txt")
    val points = parseInput(input)
    val res = connectAndClusterAll(points)
    partTwo(res.second)
    println()
}

class Point(val x: Int, val y: Int, val z: Int ){
    var connections: MutableSet<Point> = mutableSetOf<Point>()
}

fun MutableList<Pair<Double,Pair<Point,Point>>>.insertSorted(item: Pair<Double,Pair<Point,Point>>) {
    val idx = this.binarySearch(item, compareBy { it.first })
    val insertAt = if (idx >= 0) idx else -(idx + 1)
    this.add(insertAt, item)
}

fun partOne(clusters: Array<Array<Point>>){
    val sorted = clusters.sortedByDescending { it.size  }
    println( (sorted[0].size * sorted[1].size * sorted[2].size))
}

fun partTwo(lastConnection: Pair<Point,Point>){
    println(lastConnection.first.x * lastConnection.second.x)
}

fun parseInput(input: String): Array<Point>{
    val lines = input.lines()
    val pointArr = mutableListOf<Point>()
    lines.forEach { line ->
        val cords = line.split(",").map { cord -> cord.toInt() }
        pointArr += Point(cords[0],cords[1],cords[2])
    }
    return pointArr.toTypedArray()
}

fun connectAndCluster(points: Array<Point>, amount: Int): Array<Array<Point>>{
    val closestEachPoint: MutableList<Pair<Double,Pair<Point,Point>>> = mutableListOf()
    val clusters: MutableList<MutableSet<Point>> = mutableListOf()
    points.forEach { point ->
        closestEachPoint.add(findClosest(point, points))
        clusters.add(mutableSetOf(point))
    }

    var uniqueConnections = 0
    while(uniqueConnections < amount ) {
        closestEachPoint.sortBy{ it.first }
        val shortest =  closestEachPoint.removeAt(0)
        closestEachPoint.removeAt(0)

        val pointA = shortest.second.first
        val pointB = shortest.second.second

        pointA.connections.add(pointB)
        pointB.connections.add(pointA)

        closestEachPoint.add(findClosest(pointA,points))
        closestEachPoint.add(findClosest(pointB,points))

        val clusterA = clusters.find { pointA in it }
        val clusterB = clusters.find { pointB in it }

        if(clusterA != null && clusterB != null && clusterA != clusterB){
            clusterA.addAll(clusterB)
            clusters.remove(clusterB)
        }
        uniqueConnections++
    }

    return clusters.map { it.toTypedArray() }.toTypedArray()
}

fun connectAndClusterAll(points: Array<Point>): Pair<Array<Array<Point>>,Pair<Point,Point>>{
    val closestEachPoint: MutableList<Pair<Double,Pair<Point,Point>>> = mutableListOf()
    val clusters: MutableList<MutableSet<Point>> = mutableListOf()
    points.forEach { point ->
        closestEachPoint.add(findClosest(point, points))
        clusters.add(mutableSetOf(point))
    }
    closestEachPoint.sortBy{ it.first }

    var lastConnection: Pair<Point,Point> = Pair(clusters.first().first(),clusters.first().first())
    while(clusters.size > 1 ) {
        val shortest =  closestEachPoint.removeAt(0)
        closestEachPoint.removeAt(0)

        val pointA = shortest.second.first
        val pointB = shortest.second.second

        pointA.connections.add(pointB)
        pointB.connections.add(pointA)

        closestEachPoint.insertSorted(findClosest(pointA,points))
        closestEachPoint.insertSorted(findClosest(pointB,points))

        val clusterA = clusters.find { pointA in it }
        val clusterB = clusters.find { pointB in it }

        if(clusterA != null && clusterB != null && clusterA != clusterB){
            clusterA.addAll(clusterB)
            clusters.remove(clusterB)
            lastConnection = Pair(pointA,pointB)
        }
    }
    return Pair(clusters.map { it.toTypedArray() }.toTypedArray(),lastConnection)
}

fun findClosest(point: Point, points: Array<Point>):  Pair<Double, Pair<Point,Point>>{
    var closest: Pair<Double, Point> = Pair(Double.MAX_VALUE, point)
    points.forEach {
        if (it != point && it !in point.connections) {
            val distanceSquared = (it.x - point.x).toDouble().pow(2) + (it.y - point.y).toDouble()
                    .pow(2) + (it.z - point.z).toDouble().pow(2)

            (it.x - point.x).toDouble().pow(2)
            if (distanceSquared < closest.first) {
                closest = Pair(distanceSquared, it)
            }
        }
    }
    return Pair(closest.first,Pair(point,closest.second))
}