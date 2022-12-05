fun main() {

    fun String.toElfPair(): Pair<IntRange, IntRange> {
        val (elfA, elfB) = this.split(",").map { sections ->
            val (low, high) = sections.split("-")
            low.toInt()..high.toInt()
        }
        return elfA to elfB
    }

    fun isOverlap(elfA: IntRange, elfB: IntRange): Boolean =
        (elfA intersect elfB).let { it == elfA.toSet() || it == elfB.toSet() }

    fun countFreeloadingPartners(elves: List<Pair<IntRange, IntRange>>): Int =
        elves.sumOf { (elfA, elfB) -> if (isOverlap(elfA, elfB)) 1 else 0 as Int }

    fun part1(): Int {
        val testInput = readInput("Day04_test")
        val test_ans = countFreeloadingPartners(testInput.map { it.toElfPair() })
        check(test_ans == 2)

        val input = readInput("Day04")
        val ans = countFreeloadingPartners(input.map { it.toElfPair() })
        return ans
    }

    fun part2(): Int {
        val testInput = readInput("Day04_test")
        val test_ans = 0
        check(test_ans == 0)

        val input = readInput("Day04")
        val ans = 0
        return ans
    }

    println(part1())
    println(part2())
}
