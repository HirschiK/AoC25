import kotlin.math.pow

// Alternate way using DisjointUnionSet

class Point(val x: Int, val y: Int, val z: Int ){}

class DisjointUnionSet(val points: Array<Point>){
    private val rank = IntArray(points.size){1}
    private val sizes = IntArray(points.size){1}
    private val parent = IntArray(points.size){it}
    private val pointToIndex: Map<Point,Int> = points.withIndex().associate { it.value to it.index }

    private fun findIndex(i: Int): Int {
        if(parent[i] != i) parent[i] = findIndex(parent[i])
        return parent[i]
    }

    fun find(point: Point): Point{
        val idx = pointToIndex[point] ?: throw IllegalArgumentException("Unknown point")
        val rootIdx = findIndex(idx)
        return points[rootIdx]
    }

    fun getSize(point: Point): Int{
        val idx = pointToIndex[point] ?: throw IllegalArgumentException("Unknown point")
        return sizes[findIndex(idx)]
    }

    fun union(a: Point, b: Point) {
        val aIdx = pointToIndex[a] ?: throw IllegalArgumentException("Unknown point")
        val bIdx = pointToIndex[b] ?: throw IllegalArgumentException("Unknown point")
        val aRoot = findIndex(aIdx)
        val bRoot = findIndex(bIdx)
        if (aRoot == bRoot) return
        if (rank[aRoot] < rank[bRoot]) {
            parent[aRoot] = bRoot
            sizes[bRoot] += sizes[aRoot]
        }
        else if (rank[bRoot] < rank[aRoot]) {
            parent[bRoot] = aRoot
            sizes[aRoot] += sizes[bRoot]
        }
        else {
            parent[bRoot] = aRoot
            rank[aRoot]++
            sizes[aRoot] += sizes[bRoot]
        }
    }

    fun getRoots(): List<Point>{
        return parent.toSet().map {
            points[it]
        }
    }
}

fun MutableList<Pair<Double,Set<Point>>>.insertSorted(item: Pair<Double,Set<Point>>) {
    val idx = this.binarySearch(item, compareBy { it.first })
    val insertAt = if (idx >= 0) idx else -(idx + 1)
    this.add(insertAt, item)
}

fun main() {
    val input = readFile("/day_8/input.txt")
    val points = parseInput(input)
    val dUS = DisjointUnionSet(points)
    val res = connectAndCluster(dUS)
    partTwo(res.second)
}

fun parseInput(input: String): Array<Point>{
    val lines = input.lines()
    val pointArr = mutableListOf<Point>()
    lines.forEach { line ->
        val cords = line.split(",").map { cord -> cord.toInt() }
        pointArr += Point(cords[0], cords[1], cords[2])
    }
    return pointArr.toTypedArray()
}

fun partTwo(lastConnected: Pair<Point,Point>){
    println( lastConnected.first.x * lastConnected.second.x )
}

fun findClosest(point: Point, dUS: DisjointUnionSet):  Pair<Double, Set<Point>>{
    var closest: Pair<Double, Point> = Pair(Double.MAX_VALUE, point)
    val connectionSet: MutableSet<Point> = mutableSetOf()
    connectionSet.add(point)

    val pointRoot = dUS.find(point)
    dUS.points.forEach {
        if (pointRoot != dUS.find(it)) {
            val distanceSquared = (it.x - point.x).toDouble().pow(2) + (it.y - point.y).toDouble()
                .pow(2) + (it.z - point.z).toDouble().pow(2)

            (it.x - point.x).toDouble().pow(2)
            if (distanceSquared < closest.first) {
                closest = Pair(distanceSquared, it)
            }
        }
    }
    connectionSet.add(closest.second)

    return Pair(closest.first,connectionSet.toSet())
}

fun connectAndCluster(dUS: DisjointUnionSet): Pair<DisjointUnionSet,Pair<Point,Point>>{
    val closestEachPoint: MutableList<Pair<Double,Set<Point>>> = mutableListOf()
    dUS.points.forEach { point ->
        if (closestEachPoint.none { it.second.contains(point) }) {
            closestEachPoint.add(findClosest(point, dUS))
        }
    }
    var lastConnected = Pair(dUS.points[0],dUS.points[0])
    closestEachPoint.sortBy{ it.first }
    while(dUS.getRoots().size > 1) {
        val shortest =  closestEachPoint.removeAt(0)

        val (pointA, pointB) = shortest.second.toList()

        if(dUS.find(pointA) != dUS.find(pointB)){
            dUS.union(pointA,pointB)
            lastConnected = Pair(pointA, pointB)
        }

        val newClosestA = findClosest(pointA,dUS)
        if(!closestEachPoint.contains(newClosestA)) closestEachPoint.insertSorted(newClosestA)
        val newClosestB = findClosest(pointB,dUS)
        if(!closestEachPoint.contains(newClosestB)) closestEachPoint.insertSorted(newClosestB)
    }

    return Pair(dUS, lastConnected)
}

