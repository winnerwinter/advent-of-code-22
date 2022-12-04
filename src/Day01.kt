fun main() {

    fun separateElves(input: List<String>): List<List<Int>> {
        val elfSeparators = input.mapIndexedNotNull { i, str -> i.takeIf { str.isEmpty() } }
        val elfSeparatorsWithEnds = listOf(0, *elfSeparators.toTypedArray(), input.size)
        val elfIndices = elfSeparatorsWithEnds.windowed(2)

        return elfIndices.map { (low, high) ->
            input.subList(low, high).filter { it.isNotEmpty() }.map { it.toInt() }
        }
    }

    fun returnMaxCalories(input: List<String>, topN: Int): Int {
        val caloriesPerItem = separateElves(input)
        val calorieTotals = caloriesPerItem.map { it.sum() }

        return calorieTotals.sortedDescending().take(topN).sum()
    }

    fun part1(): Int {
        val testInput = readInput("Day01_test")
        val test_ans = returnMaxCalories(testInput, 1)
        check(test_ans == 24000)

        val input = readInput("Day01")
        val ans = returnMaxCalories(input, 1)
        return ans
    }

    fun part2(): Int {
        val testInput = readInput("Day01_test")
        val test_ans = returnMaxCalories(testInput, 3)
        check(test_ans == 45000)

        val input = readInput("Day01")
        val ans = returnMaxCalories(input, 3)
        return ans
    }

    check(part1() == 66186)
    check(part2() == 196804)
}
