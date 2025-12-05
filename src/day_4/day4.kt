package day_4

import readFile

fun main() {
    val input = readFile("day_4/input.txt")
    println(partTwo(parseInput(input)))
}

fun parseInput(input: String): Array<Array<Boolean>>{
    val lines = input.split("\n")
    val arr = Array(lines.size) { Array(lines[0].length) {false} }
    lines.forEachIndexed() { idx, line ->
        line.forEachIndexed() { jdx, char ->
            arr[idx][jdx] = char == '@'
        }
    }
    return arr
}

fun partOne(input: Array<Array<Boolean>>): Int{
    var totalCount = 0
    val arr = input
    arr.forEachIndexed() {idx, line ->
        line.forEachIndexed() {jdx, pos ->
            if(pos) {
                val adjOccupied = compareAdjacent(input, Pair(idx,jdx), {it})
                if (adjOccupied < 4) {
                    totalCount++
                    println("Found free at ${idx} ${jdx} with adjOcp ${adjOccupied}")

                }
            }
        }
    }
    return totalCount
}

fun partTwo(input: Array<Array<Boolean>>): Int{
    var totalCount = 0
    val arr = input
    var removed = true
    while (removed){
        removed = false
        arr.forEachIndexed() {idx, line ->
            line.forEachIndexed() {jdx, pos ->
                if(pos) {
                    //val adjOccupied = checkAdjacent(input, Pair(idx, jdx))
                    val adjOccupied = compareAdjacent(input, Pair(idx,jdx), {it})
                    if (adjOccupied < 4) {
                        totalCount++
                        println("Found free at ${idx} ${jdx} with adjFree ${adjOccupied}")
                        arr[idx][jdx] = false
                        removed = true
                    }
                }
            }
        }
    }
    return totalCount
}

fun checkAdjacent(arr: Array<Array<Boolean>>, pos: Pair<Int,Int>): Int{
    if(pos.first >= arr.size || pos.second >= arr[pos.first].size) throw IndexOutOfBoundsException()
    var adjOccupied = 0

    if (pos.first != 0){
        if(pos.second != 0){
            if(arr[pos.first-1][pos.second-1]) adjOccupied++
        }
        if(arr[pos.first-1][pos.second]) adjOccupied++
        if(pos.second < arr[pos.first].size - 1){
            if(arr[pos.first-1][pos.second+1]) adjOccupied++
        }
    }

    if(pos.second != 0){
        if(arr[pos.first][pos.second-1]) adjOccupied++
    }
    if(pos.second < arr[pos.first].size - 1){
        if(arr[pos.first][pos.second+1]) adjOccupied++
    }

    if(pos.first < arr.size -1){
        if(pos.second != 0){
            if(arr[pos.first+1][pos.second-1]) adjOccupied++
        }
        if(arr[pos.first+1][pos.second]) adjOccupied++
        if(pos.second < arr[pos.first].size - 1){
            if(arr[pos.first+1][pos.second+1]) adjOccupied++
        }
    }

    return adjOccupied
}

// Extra Function to check Adjacent without checking for IOoB
fun <ArrElemType>compareAdjacent(arr: Array<Array<ArrElemType>>, pos: Pair<Int,Int>, compareFunction: (a: ArrElemType)-> Boolean): Int{
    if(pos.first >= arr.size || pos.second >= arr[pos.first].size) throw IndexOutOfBoundsException()
    var cmpSuc = 0

    for (i in -1 until 2){
        for (j in -1 until 2){
            if(i == 0 && j == 0)continue
            try {
                if(compareFunction(arr[pos.first+i][pos.second+j])) cmpSuc++
            }
            catch (e: Exception){
            }
        }
    }
    return cmpSuc
}