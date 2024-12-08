import java.math.BigInteger

class Operation(input: String) {
    val answer: BigInteger
    val values: List<BigInteger>

    init {
        val (answerStr, valuesStr) = input.split(":")
        answer = BigInteger(answerStr)
        values = valuesStr.trim().split(" ").map { BigInteger(it) }
    }

    fun hasSolutionPart1(): Boolean = testPart1(values)

    private fun testPart1(values: List<BigInteger>): Boolean {
        if (values.isEmpty()) return false
        if (values.size == 1) return values.first() == answer
        val a = values[0]
        val b = values[1]
        val rest = values.subList(2, values.size)
        val s1 = listOf(a + b) + rest
        val s2 = listOf(a * b) + rest
        return testPart1(s1) || testPart1(s2)
    }

    fun hasSolutionPart2(): Boolean = testPart2(values)

    private fun testPart2(values: List<BigInteger>): Boolean {
        if (values.isEmpty()) return false
        if (values.size == 1) return values.first() == answer
        val a = values[0]
        val b = values[1]
        val rest = values.subList(2, values.size)
        val s1 = listOf(a + b) + rest
        val s2 = listOf(a * b) + rest
        val s3 = listOf(BigInteger(a.toString() + b.toString())) + rest
        return testPart2(s1) || testPart2(s2) || testPart2(s3)
    }
}

fun main() {

    fun part1(input: List<String>): BigInteger {
        return input
            .map { Operation(it) }
            .map { if (it.hasSolutionPart1()) it.answer else BigInteger.ZERO }
            .reduce { a, b -> a + b }
    }

    fun part2(input: List<String>): BigInteger {
        return input
            .map { Operation(it) }
            .map { if (it.hasSolutionPart2()) it.answer else BigInteger.ZERO }
            .reduce { a, b -> a + b }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
    """.trimIndent()

    val inputFilename = "Day07"
    // Read the input from the `src/$inputFilename.txt` file.
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    check(part1(sample.split("\n")) == BigInteger("3749"))
    part1(input).println()

    print("part 2: ")
    check(part2(sample.split("\n")) == BigInteger("11387"))
    part2(input).println()
}
