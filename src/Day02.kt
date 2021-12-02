fun main() {
    fun part1(input: List<String>) = traversePath(input.map { Path(it) }).let { it.first * it.second }

    fun part2(input: List<String>) = traversePath(input.map { Path(it) }, true).let { it.first * it.second }

    val input = readInput("Day02")
    println("Solution for first part: ${part1(input)}")
    println("Solution for second part: ${part2(input)}")
}

sealed interface Path {
    data class Forward(val value: Int) : Path
    data class Up(val value: Int) : Path
    data class Down(val value: Int) : Path
    data class None(val value: Int) : Path
}

fun Path(input: String): Path {
    val parsed = input.split(" ").let { Pair(it[0], it[1].toInt()) }
    return when (parsed.first) {
        "forward" -> Path.Forward(parsed.second)
        "up" -> Path.Up(parsed.second)
        "down" -> Path.Down(parsed.second)
        else -> Path.None(parsed.second)
    }
}

fun traversePath(input: List<Path>, withAim: Boolean = false): Pair<Int, Int> {
    var x = 0
    var y = 0
    var aim = 0

    for (instruction in input) {
        when (instruction) {
            is Path.Up -> {
                y -= instruction.value * (if (withAim) 0 else 1)
                aim -= instruction.value
            }
            is Path.Down -> {
                y += instruction.value * (if (withAim) 0 else 1)
                aim += instruction.value
            }
            is Path.Forward -> {
                x += instruction.value
                y += (aim * instruction.value) * (if (withAim) 1 else 0)
            }
            else -> Unit
        }
    }

    return Pair(x, y)
}