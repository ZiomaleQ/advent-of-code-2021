fun main() {
    fun part1(input: List<String>): Int {
        var count = 0
        var lastNum = input[0].toInt()

        for (line in input) {
            val currNum = line.toInt()
            lastNum = currNum.also { if (lastNum < currNum) count++ }
        }

        return count

    }

    fun part2(input: List<String>): Int {
        var count = 0
        var end = false

        fun getTriple(offset: Int): Triple<Int, Int, Int> = if (input.size - offset >= 3) {
            Triple(input[offset].toInt(), input[offset + 1].toInt(), input[offset + 2].toInt())
        } else Triple(0, 0, 0).also { end = true }

        var offset = 0
        var triplet = getTriple(offset)

        while (!end) {
            val currTriplet = getTriple(++offset)
            triplet = currTriplet.also { if (triplet.sum() < currTriplet.sum()) count++ }
        }

        return count
    }

    val baseInput = readInput("Day01")

    println("Answer for first part: ${part1(baseInput)}")
    println("Answer for second part: ${part2(baseInput)}")
}

fun Triple<Int, Int, Int>.sum(): Int = this.first + this.second + this.third