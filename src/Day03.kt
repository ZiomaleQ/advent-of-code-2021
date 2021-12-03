fun main() {

    fun part1(input: List<String>): Int {
        val numOfOnes = IntArray(input[0].length)

        for (line in input) {
            line.toArr().forEachIndexed { i, elt ->
                if (elt) numOfOnes[i]++
            }
        }

        val binaryNum = numOfOnes.map { numOfOne ->
            val numOfZero = input.size - numOfOne
            if (numOfZero < numOfOne) 1 else 0
        }

        val gamma = binaryNum.joinToString("").toInt(2)

        val epsilon = binaryNum.map { 1 - it }.joinToString("").toInt(2)

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxygen = input.map { it.toArr() }.getOxygenRatio(0).joinToString("") { if (it) "1" else "0" }.toInt(2)
        val carbon = input.map { it.toArr() }.getCarbonRatio(0).joinToString("") { if (it) "1" else "0" }.toInt(2)
        return carbon * oxygen
    }

    val input = readInput("Day03")
    println("Output for part1 ${part1(input)}")
    println("Output for part2 ${part2(input)}")
}

fun String.toArr(): BooleanArray = BooleanArray(this.length) { this[it] == '1' }
fun List<BooleanArray>.getOxygenRatio(index: Int): BooleanArray {
    if (this.size == 1) return this[0]
    val ones = this.map { it[index] }.filter { it }.size
    val zeros = this.size - ones
    return if (ones == zeros || ones > zeros) {
        this.filter { !it[index] }.getOxygenRatio(index + 1)
    } else {
        this.filter { it[index] }.getOxygenRatio(index + 1)
    }
}

fun List<BooleanArray>.getCarbonRatio(index: Int): BooleanArray {
    if (this.size == 1) return this[0]
    val ones = this.map { it[index] }.filter { it }.size
    val zeros = this.size - ones
    return if (ones < zeros) {
        this.filter { !it[index] }.getCarbonRatio(index + 1)
    } else {
        this.filter { it[index] }.getCarbonRatio(index + 1)
    }
}