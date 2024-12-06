class PageRules(input: List<String>) {
    val rulePairs = mutableSetOf<Pair<Int, Int>>()

    // a map of page -> (pages that must come after it)
    val after = mutableMapOf<Int, MutableSet<Int>>()

    // a map of page -> (pages that must come before it)
    val before = mutableMapOf<Int, MutableSet<Int>>()

    // a list of pages that must be updated
    val updates = mutableListOf<List<Int>>()

    init {
        for (line in input) {
            if (line.contains("|")) {
                val (a, b) = line.split("|").map { it.trim().toInt() }
                rulePairs.add(a to b)

                // populate after
                if (!after.contains(a)) after[a] = mutableSetOf(b)
                else after[a]!!.add(b)

                // populate before
                if (!before.contains(b)) before[b] = mutableSetOf(a)
                else before[b]!!.add(a)
            }

            else if (line.contains(",")) {
                updates.add(line.split(",").map { it.trim().toInt() })
            }
        }
    }

    fun List<Int>.followsRules(): Boolean {
//        println("Testing $this")

        // for each index, check to make sure none that are before it should be after
        // and none that are after it should be before
        indices.forEach { index ->
//            println("  Testing if index $index (${this[index]}) follows the rules:")

            val validBefore = before[this[index]] ?: mutableSetOf()
            val validAfter = after[this[index]] ?: mutableSetOf()

//            println("    pages that must come before ${this[index]}: $validBefore")
//            println("    pages that must come after ${this[index]}: $validAfter")

            val pagesBefore = subList(0, index)
            val pagesAfter = subList(index + 1, size)

//            println("    pagesBefore($pagesBefore) contains pages that must come after ($validAfter)? ${pagesBefore.intersect(validAfter)}")
//            println("    pagesAfter($pagesAfter) contains pages that must come before ($validBefore)? ${pagesAfter.intersect(validBefore)}")

            if (pagesBefore.intersect(validAfter).isNotEmpty()) return false
            if (pagesAfter.intersect(validBefore).isNotEmpty()) return false

//            println("  index $index (${this[index]}) follows the rules!")
        }
        return true
    }

    fun updateFollowsRules(index: Int): Boolean = updates[index].followsRules()

    fun getSortedUpdate(index: Int): List<Int> {
        val comparator: Comparator<Int> = Comparator { a, b ->
            if (rulePairs.contains(a to b)) -1
            else if (rulePairs.contains(b to a)) 1
            else 0
        }
        return updates[index].sortedWith(comparator)
    }
}

fun main() {

    fun <T> List<T>.middle() = get(size / 2)

    fun part1(input: List<String>): Int {
        val pr = PageRules(input)
        return pr.updates.indices
            .filter { pr.updateFollowsRules(it) }
            .sumOf { pr.updates[it].middle() }
    }

    fun part2(input: List<String>): Int {
        val pr = PageRules(input)
        return pr.updates.indices
            .filter { !pr.updateFollowsRules(it)}
            .map { pr.getSortedUpdate(it) }
            .sumOf { it.middle() }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
    """.trimIndent()

//    check(part1(sample.split("\n")) == 143)
    check(part2(sample.split("\n")) == 123)

    // Read the input from the `src/Day02.txt` file.
    val inputFilename = "Day05"
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    part1(input).println()

    print("part 2: ")
    part2(input).println()
}
