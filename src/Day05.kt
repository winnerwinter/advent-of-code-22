fun main() {

    fun parseInput(input: List<String>): Pair<State, List<Instruction>> {
        val instructionSep = input.indexOf("")
        val diagram = input.subList(0, instructionSep - 1)
        val stackLabels = input.subList(instructionSep - 1, instructionSep)
        val instructions = input.subList(instructionSep + 1, input.size)

        val numStacks = stackLabels.single().last().toString().toInt()

        val stackMap = (1 .. numStacks).associateWith { i ->
            val diagramI = ((i - 1) * 4) + 1
            diagram.mapNotNull { it.getOrNull(diagramI)?.takeIf { it != ' ' } }.reversed()
        }

        return stackMap to instructions
    }

    fun State.moveBox(from: Int, to: Int): State {
        val crateId = requireNotNull(this[from]?.last())
        return this.mapValues { (stackId, stack) ->
            when (stackId) {
                from -> stack.dropLast(1)
                to -> listOf(*stack.toTypedArray(), crateId)
                else -> stack
            }
        }
    }

    fun State.applyInstruction(instruction: Instruction): State {
        val instructionParts = instruction.split(" ")
        val move = instructionParts[1].toInt()
        val from = instructionParts[3].toInt()
        val to = instructionParts[5].toInt()

        return (0 until move).fold(this) { acc, _ ->
            acc.moveBox(from, to)
        }
    }

    fun State.compute(instructions: List<Instruction>): String =
        instructions.fold(this) { acc, instruction -> acc.applyInstruction(instruction) }.let {
            it.values.map { it.last() }.joinToString("")
        }


    fun part1(): String {
        val testInput = readInput("Day05_test")
        val test_ans = parseInput(testInput).let { (state, instructions) -> state.compute(instructions) }
        check(test_ans == "CMZ")

        val input = readInput("Day05")
        val ans = parseInput(input).let { (state, instructions) -> state.compute(instructions) }
        return ans
    }

    fun part2(): Int {
        val testInput = readInput("Day05_test")
        val test_ans = 0
        check(test_ans == 0)

        val input = readInput("Day05")
        val ans = 0
        return ans
    }

    println(part1())
    println(part2())
}

typealias State = Map<Int, List<Char>>
typealias Instruction = String
