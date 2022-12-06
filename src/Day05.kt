fun main() {

    fun parseInstruction(input: String): Instruction {
        val instructionParts = input.split(" ")
        val move = instructionParts[1].toInt()
        val from = instructionParts[3].toInt()
        val to = instructionParts[5].toInt()

        return Triple(move, from, to)
    }

    fun parseInput(input: List<String>): Pair<State, List<Instruction>> {
        val instructionSep = input.indexOf("")
        val diagram = input.subList(0, instructionSep - 1)
        val stackLabels = input.subList(instructionSep - 1, instructionSep)
        val instructionStrings = input.subList(instructionSep + 1, input.size)

        val numStacks = stackLabels.single().last().toString().toInt()

        val stackMap = (1 .. numStacks).associateWith { i ->
            val diagramI = ((i - 1) * 4) + 1
            diagram.mapNotNull { it.getOrNull(diagramI)?.takeIf { it != ' ' } }.reversed()
        }

        val instructions = instructionStrings.map { parseInstruction(it) }

        return stackMap to instructions
    }

    fun State.moveNBox(from: Int, to: Int, n: Int): State {
        val crateIds = requireNotNull(this[from]?.takeLast(n))
        return this.mapValues { (stackId, stack) ->
            when (stackId) {
                from -> stack.dropLast(n)
                to -> listOf(*stack.toTypedArray(), *crateIds.toTypedArray())
                else -> stack
            }
        }
    }

    fun State.applyInstruction(instruction: Instruction, crateMover: CrateMover): State {
        val (move, from, to) = instruction
        return when (crateMover) {
            CrateMover.v9000 -> (0 until move).fold(this) { acc, _ -> acc.moveNBox(from, to, 1) }
            CrateMover.v9001 -> this.moveNBox(from, to, move)
        }
    }

    fun State.compute(instructions: List<Instruction>, crateMover: CrateMover): String =
        instructions.fold(this) { acc, instruction -> acc.applyInstruction(instruction, crateMover) }.let {
            it.values.map { it.last() }.joinToString("")
        }

    fun part1(): String {
        val testInput = readInput("Day05_test")
        val test_ans = parseInput(testInput).let { (state, instructions) -> state.compute(instructions, CrateMover.v9000) }
        check(test_ans == "CMZ")

        val input = readInput("Day05")
        val ans = parseInput(input).let { (state, instructions) -> state.compute(instructions, CrateMover.v9000) }
        return ans
    }

    fun part2(): String {
        val testInput = readInput("Day05_test")
        val test_ans = parseInput(testInput).let { (state, instructions) -> state.compute(instructions, CrateMover.v9001) }
        check(test_ans == "MCD")

        val input = readInput("Day05")
        val ans = parseInput(input).let { (state, instructions) -> state.compute(instructions, CrateMover.v9001) }
        return ans
    }

    println(part1())
    println(part2())
}

typealias State = Map<Int, List<Char>>
typealias Instruction = Triple<Int, Int, Int>

enum class CrateMover {
    v9000,
    v9001
}
