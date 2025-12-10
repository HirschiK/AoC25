package day_02

import readFile


fun main() {
    val input = readFile("day_2/input.txt")
    println(calcSum(getRepeatingNumbers(getRanges(input))))
}

fun getRanges (input: String):Array<Pair<String,String>>{
    val ranges = input.split(',')
    var resArr: Array<Pair<String,String>> = arrayOf()
    ranges.forEach {
        val pair = it.split('-')
        resArr += Pair(pair[0].trim(),pair[1].trim())
    }
    return resArr
}

fun getTwiceRepeatingNumbers(input: Array<Pair<String,String>>): Array<String>{
    var resArr : Array<String> = arrayOf()
    input.forEach { range ->
        if(range.first.length % 2 != 0 && range.second.length %2 != 0 && range.second.length - range.first.length < 2) return@forEach
        if(range.second.length - range.first.length >= 2) throw NotImplementedError()

        val intRange = range.first.toLong()..range.second.toLong()
        for (number in intRange) {
            val stringNumber = number.toString()
            if(stringNumber.length % 2 != 0) continue
            if(stringNumber.substring(0, (stringNumber.length/2)) ==  stringNumber.substring((stringNumber.length/2))) resArr += stringNumber
        }
    }
    return resArr
}

fun getRepeatingNumbers(input: Array<Pair<String,String>>): Array<String>{
    var resArr : Array<String> = arrayOf()
    input.forEach { range ->
        val intRange = range.first.toLong()..range.second.toLong()
        for (number in intRange) {
            println("processing: ${number}")
            val stringNumber = number.toString()
            for (charAmount in 1..stringNumber.length/2) {
                val compareString = stringNumber.substring(0,charAmount)
                val toCompare = stringNumber.chunked(charAmount)
                val isValid = toCompare.all { it == compareString }
                if(isValid){
                    println("isValid: ${ stringNumber }")
                    resArr += stringNumber
                    break
                }
            }
      }
    }
    return resArr
}

fun calcSum(input: Array<String>): Long {
    var sum: Long = 0
    input.forEach {
        sum += it.toLong()
    }
    return sum
}
