fun main() {

    fun part1(): Int {
        val testInput = readInput("Day06_test")
        val test_ans = testInput.map { naive(it, 4) }
        check(test_ans == listOf(7,5,6,10,11))

        val input = readInput("Day06")
        val ans = naive(input.single(), 4)
        return ans
    }

    fun part2(): Int {
        val testInput = readInput("Day06_test")
        val test_ans = testInput.map { naive(it, 14) }
        check(test_ans ==  listOf(19,23,23,29,26))

        val input = readInput("Day06")
        val ans = naive(input.single(), 14)
        return ans
    }

    println(part1())
    println(part2())
}

fun naive(input: String, size: Int): Int =
    input.windowed(size = size, step = 1).indexOfFirst { it.toList().size == it.toSet().size } + size
