fun main() {
    val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()
    val mulResult: (MatchResult) -> Int = { it.groupValues[1].toInt() * it.groupValues[2].toInt() }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            mulRegex.findAll(line).map(mulResult).sum()
        }
    }

    val doRegex = """do\(\)""".toRegex()
    val dontRegex = """don't\(\)""".toRegex()
    val combinedRegex = """$mulRegex|$doRegex|$dontRegex""".toRegex()

    fun part2(input: List<String>): Int {
        var isEnabled = true
        return input.sumOf { line ->
            combinedRegex
                .findAll(line)
                .map { match ->
                    when {
                        // if "do", isEnabled = true
                        doRegex.matches(match.value) -> {
                            isEnabled = true
                            0
                        }
                        // if "don't", isEnabled = false
                        dontRegex.matches(match.value) -> {
                            isEnabled = false
                            0
                        }
                        // if "mul", total += first * second
                        mulRegex.matches(match.value) && isEnabled -> {
                            mulResult(match)
                        }
                        else -> 0
                    }
                }
                .sum()
        }
    }

    // Test if implementation meets criteria from the description, like:
    check(part1(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")) == 161)

    // Test if implementation meets criteria from the description, like:
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    // Or read a large test input from the `src/Day01_test.txt` file:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
