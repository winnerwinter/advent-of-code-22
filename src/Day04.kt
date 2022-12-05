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

    fun containsOverlap(elfA: IntRange, elfB: IntRange): Boolean =
        (elfA intersect elfB).isNotEmpty()

    fun countFreeloadingPartners(
        elves: List<Pair<IntRange, IntRange>>,
        determineFreeloading: (IntRange, IntRange) -> Boolean
    ): Int =
        elves.sumOf { (elfA, elfB) -> if (determineFreeloading(elfA, elfB)) 1 else 0 as Int }

    fun part1(): Int {
        val testInput = readInput("Day04_test")
        val test_ans = countFreeloadingPartners(testInput.map { it.toElfPair() }, ::isOverlap)
        check(test_ans == 2)

        val input = readInput("Day04")
        val ans = countFreeloadingPartners(input.map { it.toElfPair() }, ::isOverlap)
        return ans
    }


    fun part2(): Int {
        val testInput = readInput("Day04_test")
        val test_ans = countFreeloadingPartners(testInput.map { it.toElfPair() }, ::containsOverlap)
        check(test_ans == 4)

        val input = readInput("Day04")
        val ans = countFreeloadingPartners(input.map { it.toElfPair() }, ::containsOverlap)
        return ans
    }

    println(part1())
    println(part2())
}
