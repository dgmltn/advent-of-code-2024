
data class Cell(val x: Int, val y: Int) {
    operator fun plus(other: Cell) = Cell(x + other.x, y + other.y)
    fun move(direction: Direction) = when(direction) {
        Direction.N -> Cell(x, y - 1)
        Direction.S -> Cell(x, y + 1)
        Direction.W -> Cell(x - 1, y)
        Direction.E -> Cell(x + 1, y)
    }
}

class Map(input: List<String>) {
    val data = input.toMutableList()

    val width = data[0].length
    val height = data.size

    val size = width * height

    fun get(cell: Cell) = get(cell.x, cell.y)

    fun get(x: Int, y: Int): Char {
        return data[y][x]
    }

    fun isObstructed(cell: Cell) = isObstructed(cell.x, cell.y)

    fun isObstructed(x: Int, y: Int) = get(x, y) == '#'

    fun find(vararg chars: Char): Cell? {
        for (y in data.indices) {
            for (x in 0 until data[y].length) {
                if (chars.contains(data[y][x])) {
                    return Cell(x, y)
                }
            }
        }
        return null
    }

    fun visit(cell: Cell) {
        visit(cell.x, cell.y)
    }

    fun visit(x: Int, y: Int) {
        set(x, y, 'X')
    }

    fun obstruct(cell: Cell) {
        obstruct(cell.x, cell.y)
    }

    fun obstruct(x: Int, y: Int) {
        set(x, y, '#')
    }

    fun count(c: Char): Int = data.sumOf { it.count { it == c } }

    fun set(cell: Cell, c: Char) {
        set(cell.x, cell.y, c)
    }

    fun set(x: Int, y: Int, c: Char) {
        val row = data[y].toCharArray()
        row[x % width] = c
        data[y] = row.joinToString("")
    }

    fun contains(cell: Cell) = contains(cell.x, cell.y)

    fun contains(x: Int, y: Int) = y in data.indices && x in data[y].indices

    fun print() {
        data.forEach { println(it) }
    }
}

enum class Direction() {
    N, S, W, E;
    fun next(): Direction = when(this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }
    companion object {
        fun from(c: Char): Direction = when(c) {
            '^' -> N
            'v' -> S
            'V' -> S
            '<' -> W
            '>' -> E
            else -> error("Invalid direction: $c")
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val map = Map(input)
        var cell = map.find('^', 'v', 'V', '<', '>') ?: error("No starting location found")
        var direction = Direction.from(map.get(cell))

        // Travel around the map
        while (map.contains(cell)) {
//            println("visited Cell $cell, moving $direction")
            map.visit(cell)
//            map.print()
//            println()
            val next = cell.move(direction)
            if (!map.contains(next)) break
            if (map.isObstructed(next)) direction = direction.next()
            else cell = cell.move(direction)
        }

        // Look for all the visited cells
        return map.count('X')
    }

    fun part2(input: List<String>): Int {
        val map = Map(input)
        val startingCell = map.find('^', 'v', 'V', '<', '>') ?: error("No starting location found")
        val startingDirection = Direction.from(map.get(startingCell))
        var cell = startingCell
        var direction = startingDirection

        // naieve solution, just visit all cells
        // better solution: use the trail made in part 1
        val possibleObstructions = mutableSetOf<Cell>()
        val slimeTrail = mutableListOf<Pair<Cell, Direction>>()
        while (map.contains(cell)) {
            map.visit(cell)
            slimeTrail += cell to direction
            val next = cell.move(direction)
            if (!map.contains(next)) break
            possibleObstructions += next
            if (map.isObstructed(next)) direction = direction.next()
            else cell = cell.move(direction)
        }



        // Re-do the trail with the possible obstructions
        return possibleObstructions.count { obstruction ->
            val possibleMap = Map(input).apply { obstruct(obstruction) }
            val possibleSlimeTrail = mutableListOf<Pair<Cell, Direction>>()
            cell = startingCell
            direction = startingDirection
//            println("Possible obstruction at $obstruction")
//            possibleMap.print()
//            println("->")

            while (possibleMap.contains(cell)) {
                if (possibleSlimeTrail.contains(cell to direction)) return@count true
                possibleSlimeTrail.add(cell to direction)
                possibleMap.visit(cell)
                val next = cell.move(direction)
                if (!possibleMap.contains(next)) break
                if (possibleMap.isObstructed(next)) direction = direction.next()
                else cell = cell.move(direction)
            }
//            possibleMap.print()
//            println()
            false
        }
    }

    // Test if implementation meets criteria from the description, like:
    val sample = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
    """.trimIndent()

    check(part1(sample.split("\n")) == 41)
    check(part2(sample.split("\n")) == 6)

    // Read the input from the `src/Day02.txt` file.
    val inputFilename = "Day06"
    val input = readInput(inputFilename)
    println(inputFilename)

    print("part 1: ")
    part1(input).println()

    print("part 2: ")
    part2(input).println()
}
