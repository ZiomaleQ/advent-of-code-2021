fun main() {
    fun part1(input: List<String>): Int {
        val duplicates: MutableMap<Point, Int> = mutableMapOf()

        fun MutableMap<Point, Int>.add(point: Point) {
            val pointCount = this[point]
            this[point] = (pointCount ?: 0) + 1
        }

        input.forEach { inputLine ->
            val line = parseLine(inputLine)
            if (!line.isValid) return@forEach

            line.let { duplicates.add(it.end);it }.forEach {
                duplicates.add(it)
            }
        }

        return duplicates.entries.filter { it.value >= 2 }.size
    }

    fun part2(input: List<String>): Int {
        val duplicates: MutableMap<Point, Int> = mutableMapOf()

        fun MutableMap<Point, Int>.add(point: Point) {
            this[point] = (this[point] ?: 0) + 1
        }

        input.forEach { inputLine ->
            val line = parseLine(inputLine, cross = true)
            if (!line.isValid) return@forEach

            line.let { duplicates.add(it.end);it }.forEach {
                duplicates.add(it)
            }
        }

        return duplicates.entries.filter { it.value >= 2 }.size
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun parseLine(line: String, cross: Boolean = false): Line {
    val points = line.split("->").map { Point.fromString(it.trim()) }
    return Line(points[0], points[1], cross)
}

data class Point(val x: Int, val y: Int) {
    fun nextPoint(other: Point): Point {
        if (x != other.x && y != other.y) {
            val nextX = if (x < other.x) x + 1 else x - 1
            val nextY = if (y < other.y) y + 1 else y - 1
            return Point(nextX, nextY)
        }
        return if (x != other.x) {
            val nextX = if (x < other.x) x + 1 else x - 1
            Point(nextX, y)
        } else {
            val nextY = if (y < other.y) y + 1 else y - 1
            Point(x, nextY)
        }
    }

    companion object {
        fun fromString(input: String): Point {
            val parsedInput = input.split(',').map { it.trim().toInt() }
            return Point(parsedInput[0], parsedInput[1])
        }
    }
}

data class Line(val start: Point, val end: Point, val cross: Boolean) : Iterable<Point> {
    val isValid = (start.x == end.x || start.y == end.y) || cross

    override fun iterator(): Iterator<Point> {

        return object : Iterator<Point> {
            var current: Point = start

            override fun next(): Point {
                if (!hasNext()) throw NoSuchElementException()
                val result = current
                current = current.nextPoint(end)
                return result
            }

            override fun hasNext(): Boolean {
                return isValid && (current.x != end.x || current.y != end.y)
            }
        }
    }
}