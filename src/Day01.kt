import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val list1 = mutableListOf<Int>()
        val list2 = mutableListOf<Int>()
        input.map {
            it.split("""\s+""".toRegex())
                .map(String::toInt)
                .let {
                    list1.add(it[0])
                    list2.add(it[1])
                }
        }
        list1.sort()
        list2.sort()
        return list1.zip(list2).map { (a, b) -> (b - a).absoluteValue.also { println("$b - $a = $it") } }.sum()
    }

    fun part2(input: List<String>): Int {
        val list1 = mutableListOf<Int>()
        val list2 = mutableMapOf<Int, Int>()
        input.map {
            it.split("""\s+""".toRegex())
                .map(String::toInt)
                .let {
                    list1.add(it[0])
                    if (list2.contains(it[1])) {
                        list2[it[1]] = list2[it[1]]!! + 1
                    } else {
                        list2[it[1]] = 1
                    }
                }
        }

        return list1.sumOf {
            if (list2.contains(it)) {
                it * list2[it]!!
            } else {
                0
            }
        }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()
    check(part1(sample.split("\n")) == 11)
    check(part2(sample.split("\n")) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()

    part2(input).println()
}
