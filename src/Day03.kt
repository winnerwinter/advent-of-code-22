fun main() {

    fun part1(): Int {
        val testInput = readInput("Day03_test")
        val test_ans = computePrioritiesOfMisplacedItems(testInput.toRucksacks())
        check(test_ans == 157)

        val input = readInput("Day03")
        val ans = computePrioritiesOfMisplacedItems(input.toRucksacks())
        return ans
    }

    fun part2(): Int {
        val testInput = readInput("Day03_test")
        val test_ans = computePrioritiesOfTeamBadges(testInput.toRucksacks())
        check(test_ans == 70)

        val input = readInput("Day03")
        val ans = computePrioritiesOfTeamBadges(input.toRucksacks())
        return ans
    }

    println(part1())
    println(part2())
}


///// Part 1

data class Rucksack(val items: List<Char>) {
    constructor(items: String) : this(items.toList())
    init {
        check(items.isNotEmpty())
        check(items.toList().size % 2 == 0)
    }

    val numItems: Int = items.size
    val compartment1: List<Char> = items.subList(0, numItems / 2)
    val compartment2: List<Char> = items.subList(numItems / 2, numItems)
}

fun List<String>.toRucksacks() = map { Rucksack(it) }

fun Char.toPriority(): Int = if (isLowerCase()) { code - 96 } else { code - 38 }

fun Rucksack.findMisplacedItem(): Char = requireNotNull(compartment1.find { it in compartment2 })

fun computePriorities(extractItems: () -> List<Char>): Int =
    extractItems().sumOf { it.toPriority() }

fun computePrioritiesOfMisplacedItems(rucksacks: List<Rucksack>): Int =
    computePriorities { rucksacks.map { it.findMisplacedItem() } }


///// Part 2

fun computePrioritiesOfTeamBadges(rucksacks: List<Rucksack>): Int {
    val teams = rucksacks.chunked(3).map { it.sortedBy { it.items.size } }
    val badges = teams.map { (a, b, c) ->
        computePriorities {
            listOfNotNull(a.items.find { it in b.items && it in c.items })
        }
    }
    return badges.sum()
}
