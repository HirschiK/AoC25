package day_6

import readFile

fun main() {
    val input = readFile("day_6/input.txt")
    partTwo(parseInputString(input))
}

fun parseInput(input: String): Pair<Array<Array<Int>>,Array<String>> {
    val lines = input.split("\n")
    val functionCount = lines.first().trim().replace(Regex("\\s+"),(" ")).split(" ").size
    val operators: Array<String> = Array<String>(functionCount) {" "}
    val numbers: Array<Array<Int>> = Array<Array<Int>>(functionCount){ Array<Int>(lines.size -1 ){0} }
    lines.forEachIndexed(){idx, line ->
        val lineInputs = line.trim().replace(Regex("\\s+"),(" ")).split(" ")
        lineInputs.forEachIndexed(){ jdx, inp ->
            if(inp != "+" && inp != "*" && inp != "" ){
                numbers[jdx][idx]= inp.toInt()
            }
            else {
                if (inp != "") operators[jdx] = inp
            }
        }
    }
    return Pair(numbers,operators)
}

fun parseInputString(input: String):Pair<Array<Array<String>>,Array<Char>> {
    val lines = input.split("\n")
    val opList: MutableList<Pair<Char, Int>> = mutableListOf()
    lines.last().forEachIndexed { index, c ->
        if (c == '+' || c == '*') opList += Pair(c,index)
    }
    val numberArr = Array<Array<String>>(opList.size){Array<String>(lines.size-1){"0"}}
    lines.forEachIndexed { idx, line ->
        if(!(idx >= lines.size-1)) {
            opList.forEachIndexed { jdx, operator ->
                if(jdx == opList.size - 1)  numberArr[jdx][idx] = line.substring(operator.second)
                else numberArr[jdx][idx] = line.substring(operator.second until opList[jdx + 1].second )
            }
        }
    }
    return (Pair(numberArr,opList.map { it.first }.toTypedArray()))
}

fun partOne(input: Pair<Array<Array<Int>>,Array<String>>){
    var sum: Long = 0
    input.first.forEachIndexed(){idx , numbers ->
        var intermediateSum: Long = if(input.second[idx] == "+") 0 else 1
        numbers.forEach { number ->
            if (input.second[idx] == "+") intermediateSum += number
            else intermediateSum *= number
        }
        sum += intermediateSum
    }
    println(sum)
}

fun partTwo(input: Pair<Array<Array<String>>,Array<Char>>){
    var sum: Long = 0
    input.first.forEachIndexed() { idx, numbers ->
        val numberLength = numbers.map { it.trim() }.maxByOrNull { it.length }!!.length
        val newNumbers: Array<Int> = Array<Int>(numberLength){0}

        for (i in 0 until numberLength ){
            var num = ""
            numbers.forEach { number ->
                if(number.length > i && number[i] != ' '){
                    num += number[i]
                }
            }
            newNumbers[i] = num.toInt()
        }

        var intermediateSum: Long = if(input.second[idx] == '+') 0 else 1
        newNumbers.forEach { number ->
            if (input.second[idx] == '+') intermediateSum += number
            else intermediateSum *= number
        }
        sum += intermediateSum
    }
    println(sum)
}
