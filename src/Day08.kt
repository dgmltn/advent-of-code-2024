import kotlin.math.sqrt

data class Point(val x: Int, val y: Int) {
    fun distanceTo(p: Point): Double {
        return sqrt((p.x - x).toDouble() * (p.x - x) + (p.y - y) * (p.y - y))
    }

    override fun toString(): String {
        return "[$x, $y]"
    }
}

private fun Char.isAntenna() = this in 'A'..'Z' || this in 'a' .. 'z' || this in '0' .. '9'

private fun <T> MutableMap<Char, MutableList<T>>.addToList(k: Char, v: T) {
    if (!contains(k)) this[k] = mutableListOf(v)
    else this[k]!!.add(v)
}

class Antennas(input: List<String>) {
    private val bounds: Point = Point(x = input[0].length - 1, y = input.size - 1).also { println("bounds: $it") }
    private val antennas: MutableMap<Char, MutableList<Point>> = mutableMapOf()

    init {
        for (y in 0 .. bounds.y) {
            val chars = input[y].toCharArray()
            for (x in 0 .. bounds.x) {
                if (chars[x].isAntenna()) antennas.addToList(chars[x], Point(x, y))
            }
        }
    }

    // Given a list of any length, finds all combinations of pairs of that list
    private fun <T> List<T>.pairs(): List<Pair<T, T>> =
        (0 .. size - 2).flatMap { a ->
            (a+1..<size).map { b ->
                this[a] to this[b]
            }
        }

    // Takes a pair of Point and returns the location of the antinodes of that
    // pair. May be out of bounds.
    private fun Pair<Point, Point>.antinodes(): List<Point> {
        val (a, b) = this
        val dx = b.x - a.x
        val dy = b.y - a.y
        val p1 = Point(a.x - dx, a.y - dy)
        val p2 = Point(b.x + dx, b.y + dy)
        val p3 = Point(a.x + dx / 3, a.y + dy / 3).takeIf { dx % 3 == 0 && dy % 3 == 0 }
        val p4 = Point(b.x - dx / 3, b.y - dy / 3).takeIf { dx % 3 == 0 && dy % 3 == 0 }
        val result = listOfNotNull(p1, p2, p3, p4)
            .filter { it.inBounds() }

//        println("for points $a, $b: antinodes => ${result.joinToString(",")}")
        return result
    }

    private fun Pair<Point, Point>.antinodes2(): List<Point> {
        val (a, b) = this
        val dx = a.x - b.x
        val dy = a.y - b.y
        val pointSlope: (Float) -> Float = { x -> dy * (x - a.x) / dx + a.y }
        val result = (0..bounds.x)
            .mapNotNull { x ->
                val y = pointSlope(x.toFloat())
                if (y.toInt().toFloat() == y && y.toInt() in 0..bounds.y) Point(x, y.toInt())//.also { println("valid: [$x, $y]") }
                else null
            }

//        println("for points $a, $b: antinodes => ${result.joinToString(",")}")
        return result
    }

    fun antinodes(): Set<Point> =
        pairs()
            .flatMap { it.antinodes() }
            .filter { it.inBounds() }
            .toSet()

    fun antinodesPart2(): Set<Point> =
        pairs()
            .flatMap { it.antinodes2() }
            .filter { it.inBounds() }
            .toSet()

    private fun pairs(): List<Pair<Point, Point>> =
        antennas
            .entries
            .map {
//                println("${it.key} -> ${it.value}")
                it.value
            }
            .flatMap { it.pairs() }

    private fun Point.inBounds(): Boolean = x in 0..bounds.x && y in 0..bounds.y
}

fun main() {

    fun part1(input: List<String>): Int {
        return Antennas(input)
            .antinodes()
            .also { println(it.map {"[${it.x},${it.y}]"}.sorted().joinToString(","))}
            .count()
    }

    fun part2(input: List<String>): Int {
        return Antennas(input)
            .antinodesPart2()
            .also { println(it.map {"[${it.x},${it.y}]"}.sorted().joinToString(","))}
            .count()
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
    """.trimIndent()

    val inputFilename = "Day08"
    // Read the input from the `src/$inputFilename.txt` file.
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    val part1SampleSolution = part1(sample.split("\n"))
    println("sample: $part1SampleSolution")
    check(part1SampleSolution == 14)
    part1(input).println()

    print("part 2: ")
    check(part2(sample.split("\n")) == 34)
    part2(input).println()
}
