from z3 import *

class Machine:
    def __init__(self, instructions, joltageRequirements):
        self.instructions = instructions
        self.joltageRequirements = joltageRequirements


def readFile(filename):
    with open(filename, 'r') as file:
        data = file.read().strip()
    return data

def parseInput(data):
    lines = data.split('\n')
    machines = []
    for line in lines:
        _, *_instructions, _joltage = line.split(" ")
        _instructions = [list(map(int, x[1:-1].split(","))) for x in _instructions]
        _joltage = list(map(int, _joltage[1:-1].split(",")))
        machines.append(Machine(_instructions, _joltage))
    return machines

def main():
    fileInput = readFile("input.txt")
    machines = parseInput(fileInput)
    part2(machines)

def part2(machines):
    totalCount = 0
    for machine in machines:
        # create Z3 optimization problem
        opt = Optimize()
        # create integer variables for each instruction
        instructionUsedCount = [Int(f"inst_{i}") for i in range(len(machine.instructions))]

        # add non negativity constraints
        for count in instructionUsedCount:
            opt.add(count >= 0)

        # for all joltage requirements, check which instructions affect them
        for lightIndex, joltageReq in enumerate(machine.joltageRequirements):
            affectsJoltage = []
            for instructionIndex, instruction in enumerate(machine.instructions):
                if lightIndex in instruction:
                    affectsJoltage.append(instructionUsedCount[instructionIndex])
            # Constrain the sum of these instructions to equal the requirement
            opt.add(Sum(affectsJoltage) == joltageReq)

        opt.minimize(Sum(instructionUsedCount))

        if opt.check() == sat:
            model = opt.model()
            total_instructions = sum(model[instructionCountIndex].as_long() for instructionCountIndex in instructionUsedCount)
            totalCount += total_instructions
        else:
            print("No solution found")

    print(totalCount)

main()