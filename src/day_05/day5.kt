package day_05

import readFile
import java.math.BigInteger

class BigIntegerRange(val start: BigInteger, val endInclusive: BigInteger) {
    operator fun contains(value: BigInteger): Boolean {
        return value >= start && value <= endInclusive
    }

    operator fun iterator(): Iterator<BigInteger> {
        return object : Iterator<BigInteger> {
            private var current = start

            override fun hasNext(): Boolean {
                return current <= endInclusive
            }

            override fun next(): BigInteger {
                if (!hasNext()) throw NoSuchElementException()
                return current.also { current += BigInteger.ONE }
            }
        }
    }
}


fun main() {
    val input = readFile("day_5/input.txt")
    partTwo(parseInput(input).first)
}

fun parseInput(input: String): Pair<Array<BigIntegerRange>,Array<BigInteger>>{
    val split = input.split("\n\n")
    val rangesString = split.first().split("\n")
    val ingredientsString = split[1].split("\n")

    val ranges = rangesString.map { range ->
        val rangeValues = range.split("-")
        BigIntegerRange(rangeValues[0].toBigInteger(), rangeValues[1].toBigInteger())
    }.toTypedArray()

    val ingredients = ingredientsString.map { ingridient ->
        ingridient.toBigInteger()
    }.toTypedArray()

    return Pair(ranges,ingredients)
}

fun partOne(input: Pair<Array<BigIntegerRange>,Array<BigInteger>>){
    var goodIngredients = 0
    input.second.forEach { ingredient ->
        for(i in input.first){
            if (ingredient in i) {
                goodIngredients++
                break
            }
        }
    }
    println(goodIngredients)
}

fun partTwo(input: Array<BigIntegerRange>){
    var goodIngredients = BigInteger.ZERO
    val mergedRanges = mutableListOf<BigIntegerRange>()

    input.sortedBy { it.start }.forEach { range ->
        if (mergedRanges.isNotEmpty() && mergedRanges.last().endInclusive >= range.start) {
            val lastRange = mergedRanges.removeAt(mergedRanges.size - 1)
            mergedRanges.add(BigIntegerRange(lastRange.start, maxOf(lastRange.endInclusive, range.endInclusive)))
        } else {
            mergedRanges.add(range)
        }
    }

    mergedRanges.forEach { range ->
        goodIngredients += range.endInclusive - range.start + BigInteger.ONE
    }
    println(goodIngredients)
}