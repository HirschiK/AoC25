package day_10

import readFile

class Machine(val indicators: Array<Boolean>,val instructions: Array<Array<Int>>,val joltageReq: Array<Int>){
}

fun main() {
    val input = readFile("/day_10/input.txt")
    val machines = parseInput(input)
    enableMachines(machines)
    print("")
}

fun parseInput(input: String): Array<Machine>{
    val lines = input.lines()
    val machines: MutableList<Machine> = mutableListOf()
    lines.forEach { line ->
        val indicatorString = line.substring(1 until line.indexOf(']'))
        val indicators = indicatorString.map { char ->
            char == '#'
        }.toTypedArray()
        val instructionString = line.substring(line.indexOf(']')+1 until line.indexOf('{'))
        val instructions = instructionString.trim().split(" ").map { instruction ->
            val buttons = instruction.replace("(","").replace(")","").split(",").map { it.toInt() }
            buttons.toTypedArray()
        }.toTypedArray()

        val joltageReqString = line.substring(line.indexOf("{")+1 until line.indexOf("}"))
        val joltageReq = joltageReqString.split(",").map { req -> req.toInt() }.toTypedArray()

        machines.add(Machine(indicators,instructions,joltageReq))
    }
    return machines.toTypedArray()
}

fun enableMachines(machines: Array<Machine>){
    var presses = 0
    var joltagePresses = 0
    machines.forEach { machine ->
        val res = turnOn(machine.indicators,Array<Boolean>(machine.indicators.size){false},machine.instructions)
        presses += res.second
        val joltageRes = matchJoltage(machine.joltageReq, Array<Int>(machine.indicators.size){0},machine.instructions)
        joltagePresses += joltageRes
    }
    println(presses)
    print(joltagePresses)
}

fun turnOn (goal: Array<Boolean>, current: Array<Boolean>, instructions: Array<Array<Int>>): Pair<Array<Array<Int>>,Int> {
    var currentStates: MutableList<Pair<Array<Boolean>, Array<Array<Int>>>> = mutableListOf()
    currentStates.add(Pair(current,instructions))

    var newStates : MutableList<Pair<Array<Boolean>, Array<Array<Int>>>> = mutableListOf()

    for (i in 0 until instructions.size){
        val snap = currentStates.toList()
        snap.forEach { (state,remainingInstructions)->
            currentStates.remove(Pair(state,remainingInstructions))
            if(remainingInstructions.isEmpty()) println("Could not find solution")

            remainingInstructions.forEach { instruction ->
                val afterInstruction = state.mapIndexed { idx, light ->
                    if(instruction.contains(idx)) !light
                    else light
                }.toTypedArray()
                val remainingInstructions = instructions.toMutableList()
                remainingInstructions.remove(instruction)

                if(afterInstruction.contentEquals(goal)){
                    return Pair(remainingInstructions.toTypedArray(),i+1)
                }
                else {
                    newStates.add(Pair(afterInstruction,remainingInstructions.toTypedArray()))
                }
            }
        }
        currentStates = newStates
        newStates = mutableListOf()
    }

    throw Error("Not found")
}

fun matchJoltage (goal: Array<Int>, current: Array<Int>, instructions: Array<Array<Int>>): Int {
    var currentStates: MutableSet<Array<Int>> = mutableSetOf()
    currentStates.add(current)

    var newStates : MutableSet<Array<Int>> = mutableSetOf()
    var steps = 1
    while(true){
        val snap = currentStates.toList()
        println(steps)
        snap.forEach { state->
            currentStates.remove(state)

            instructions.forEach { instruction ->
                val afterInstruction = state.mapIndexed { idx, joltage ->
                    if(instruction.contains(idx)) joltage + 1
                    else joltage
                }.toTypedArray()

                val valid = afterInstruction.withIndex().none { (idx, joltage) ->
                    goal[idx] < joltage
                }
                if(valid) {
                    if (afterInstruction.contentEquals(goal)) {
                        return steps
                    }
                    else {
                        newStates.add(afterInstruction)
                    }
                }
            }
        }
        currentStates = newStates
        newStates = mutableSetOf()
        steps += 1
    }

}