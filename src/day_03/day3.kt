package day_03

import readFile

fun main() {
    val input = readFile("day_3/input.txt")
    val joltArr = parseInput(input)
    println(partTwo(joltArr))
}

fun partOne(input: Array<Array<Int>>): Int {
    var sum = 0
    input.forEach { bank ->
        val largest = getLargestTwo(bank)
        sum += (largest.first * 10 + largest.second)
        println("Largest : ${largest} Sum: ${sum}")
    }
    return sum
}

fun partTwo(input: Array<Array<Int>>): Long {
    var sum: Long = 0
    input.forEach { bank ->
        val largest6 = getLargestSixWithSpacing(bank)
        var str = ""
        largest6.forEach {
            str += it.first.toString() + it.second.toString()
        }
        println("Got: ${str}")
        sum += str.toLong()
    }
    return sum
}

fun parseInput(input: String): Array<Array<Int>>{
    val lines = input.split("\n")
    val resArr = lines.map { line ->
        line.map { joltage ->
            joltage.toString().toInt()
        }.toTypedArray()
    }.toTypedArray()
    return resArr
}

fun getLargestTwo(input: Array<Int>): Pair<Int,Int>{
    var l = Pair(input[0],0)
    var r = Int.MIN_VALUE

    for (i in 1 until input.size-1) if (input[i] > l.first)  l = Pair(input[i], i)
    for(i in l.second+1 until input.size) if (input[i] > r) r = input[i]

    return Pair(l.first,r)
}

fun getLargestTwoIndexed(input: Array<Int>): Pair<Pair<Int,Int>,Int>{
    var l = Pair(input[0],0)
    var r = Int.MIN_VALUE
    println("----------")
    input.forEach {
        print("${it} ")
    }

    for (i in 1 until input.size-1) if (input[i] > l.first)  l = Pair(input[i], i)
    for(i in l.second+1 until input.size) if (input[i] > r) {
        r = input[i]
        l = Pair(l.first,i)
    }

    println("Returning ${Pair(Pair(l.first,r),l.second)}")
    return Pair(Pair(l.first,r),l.second)
}

fun getLargestSixWithSpacing(input: Array<Int>): Array<Pair<Int,Int>>{
    var resArr = Array(6) {Pair(Pair(0,-1),0)}
    for (i in 0..5){
        var current: Pair<Pair<Int,Int>,Int>
        val firstSearchableIndex = if (i == 0 ) 0 else resArr[i-1].second
        val lastSearchableIndex = if (i == 5) input.size - 1 else input.size - (12 - i * 2) + 1
        val res = getLargestTwoIndexed(input.copyOfRange(firstSearchableIndex,lastSearchableIndex+1))
        current = Pair(res.first,firstSearchableIndex+res.second+1)
        println("Assigned current as : ${current}")
        resArr[i] = current
    }
    return resArr.map { it.first }.toTypedArray()
}