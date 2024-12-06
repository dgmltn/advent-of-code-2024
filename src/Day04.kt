
data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)
}

class WS(private val input: List<String>) {
    // Return null if out of bounds
    private fun Pos.char(): Char? = if (y in input.indices && x in input[y].indices) input[y][x] else null

    // Find position of all occurrences of "X"
    fun findAll(letter: Char = 'X'): List<Pos> {
        val result = mutableListOf<Pos>()
        for (y in input.indices) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == letter) {
                    result.add(Pos(x, y))
                }
            }
        }
        return result
    }

    val UL = Pos(-1, -1)
    val UR = Pos(1, -1)
    val DL = Pos(-1, 1)
    val DR = Pos(1, 1)

    val directions = listOf(
        Pos(1, 0),
        Pos(1, 1),
        Pos(0, 1),
        Pos(-1, 1),
        Pos(-1, 0),
        Pos(-1, -1),
        Pos(0, -1),
        Pos(1, -1)
    )

    fun getWordAt(pos: Pos, direction: Pos, len: Int): String {
        val result = StringBuilder()
        var currentPos = pos
        while (result.length != len) {
            val c = currentPos.char() ?: break
            result.append(c)
            currentPos += direction
        }
        return result.toString()
    }

    fun countWordsAt(pos: Pos, word: String): Int =
        directions.count {
            getWordAt(pos, it, word.length) == word
        }

    fun isXwordAt(pos: Pos, word: String): Boolean {
        // Type 1
        if (
            getWordAt(pos + UL, DR, word.length) == word
            && getWordAt(pos + UR, DL, word.length) == word
        ) return true

        // Type 2
        if (
            getWordAt(pos + UR, DL, word.length) == word
            && getWordAt(pos + DR, UL, word.length) == word
        ) return true

        // Type 3
        if (
            getWordAt(pos + DR, UL, word.length) == word
            && getWordAt(pos + DL, UR, word.length) == word
        ) return true

        // Type 4
        if (
            getWordAt(pos + DL, UR, word.length) == word
            && getWordAt(pos + UL, DR, word.length) == word
        ) return true

        return false
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val ws = WS(input)
        val exxes = ws.findAll('X')
        return exxes.sumOf { ws.countWordsAt(it, "XMAS") }
    }

    fun part2(input: List<String>): Int {
        val ws = WS(input)
        val ayes = ws.findAll('A')
        return ayes.count { ws.isXwordAt(it, "MAS") }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
    """.trimIndent()
    check(part1(sample.split("\n")) == 18)
    check(part2(sample.split("\n")) == 9)

    // Read the input from the `src/Day02.txt` file.
    val inputFilename = "Day04"
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    part1(input).println()

    print("part 2: ")
    part2(input).println()
}
