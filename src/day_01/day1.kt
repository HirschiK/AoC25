package day_01

import readFile
import kotlin.math.abs

const val LOWER_LIMIT = 0
const val UPPER_LIMIT = 99
const val START_NUMBER = 50


fun main() {
    val input = readFile("day_1/input.txt")
    val parsed = parseInput(input)
    println(timesNumberReachedTotal(START_NUMBER, parsed, 0))
}

fun timesNumberReachedExactly(start: Int, inputs: Array<Pair<Boolean,Int>>, number: Int): Int{
    var current = start
    var res = 0
    inputs.forEach { input ->
        current = turn(input.first,input.second, Pair(LOWER_LIMIT, UPPER_LIMIT), current ).first
        if (current == number) res++;
    }
    return res
}

fun timesNumberReachedTotal(start: Int, inputs: Array<Pair<Boolean,Int>>, number: Int): Int{
    var current = start
    var res = 0
    inputs.forEachIndexed() { idx, input ->
        val turnRes = turn(input.first,input.second, Pair(LOWER_LIMIT, UPPER_LIMIT), current )
        if((current == LOWER_LIMIT && input.first) ) res--
        current = turnRes.first
        res += turnRes.second
        if(turnRes.second == 0 && current == number) res++
        println("Step: ${idx} Input=$input, Current=$current, Res=$res, TurnRes${turnRes}")
    }
    return res
}

fun parseInput(input: String): Array<Pair<Boolean,Int>> {
    val lines = input.split("\n");
    var resArr: Array<Pair<Boolean,Int>> = arrayOf()
    lines.forEach { line ->
        var res: Pair<Boolean,Int> = Pair(false, 0)
        res = res.copy(first = line.lowercase().contains("l"))
        res = res.copy(second = line.filter { it.isDigit() }.toInt())
        resArr += res
    }
    return resArr
}

fun turn(left: Boolean, amount: Int, limits: Pair<Int,Int>, current: Int): Pair<Int,Int> {
    return if (left) {
        sub(current, amount, Pair(limits.first, limits.second))
    }
    else {
        add(current, amount, Pair(limits.first,limits.second))
    }

}

fun add (l: Int, r: Int, limits: Pair<Int, Int>): Pair<Int,Int>{
    val range = (limits.second - limits.first + 1)
    val addRes = (l+r) % range
    val overflow = (l+r) / range
    return Pair(addRes,overflow)
}

fun sub (l:Int, r: Int, limits: Pair<Int, Int>): Pair<Int,Int>{
    val range = (limits.second - limits.first + 1)
    val subRes = ((l - r) % range + range) % range
    val overflow = if (l - r < 0) abs((l - r) / range - 1) else 0
    return Pair(subRes,overflow)

}