import kotlin.math.absoluteValue

fun main() {
    fun List<Int>.isIncreasing(): Boolean = (0 until size - 1).all { this[it] < this[it + 1] }
    fun List<Int>.isDecreasing(): Boolean = (0 until size - 1).all { this[it] > this[it + 1] }
    fun List<Int>.gapsAreBetween(min: Int = 1, max: Int = 3) = (0 until size - 1).all { (this[it + 1] - this[it]).absoluteValue in min..max }
    fun List<Int>.isSafe(): Boolean = (isIncreasing() || isDecreasing()) && gapsAreBetween()

    fun part1(input: List<String>): Int {
        return input.map {
            it
                .split("""\s+""".toRegex())
                .map(String::toInt)
                .isSafe()
        }.count { it }
    }

    fun List<Int>.removeAt(index: Int) = subList(0, index) + subList(index + 1, size)
    fun List<Int>.isSafeWithDampener(): Boolean = isSafe() || indices.any { removeAt(it).isSafe() }

    fun part2(input: List<String>): Int {
        return input.map {
            it
                .split("""\s+""".toRegex())
                .map(String::toInt)
                .isSafeWithDampener()
        }.count { it }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
    """.trimIndent()
    check(part1(sample.split("\n")) == 2)
//    check(part2(sample.split("\n")) == 31)

    // Read the input from the `src/Day02.txt` file.
    val inputFilename = "Day02"
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    part1(input).println()

    print("part 2: ")
    part2(input).println()
}
