fun main() {
    fun part1(input: List<String>): Long {
        return FishTank(input[0].split(',').map { it.toInt() }).simulateTill(80)
    }

    fun part2(input: List<String>): Long {
        return FishTank(input[0].split(',').map { it.toInt() }).simulateTill(256)
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

data class FishTank(val startingPoint: List<Int>) {
    private val tanks = startingPoint.groupBy { it }.entries.map { FishContainer(it.value.size.toLong(), it.key) }.toMutableList()

    fun simulateTill(day: Int): Long {
        var index = 0

        while (index < day) {
            var newFishes = 0L
            for (tank in tanks) {
                tank.timer--
                if (tank.timer == -1) {
                    tank.timer = 6
                    newFishes += tank.count
                }
            }
            tanks.add(FishContainer(newFishes, 8))
            index++
        }

        return tanks.sumOf { it.count }
    }
}

data class FishContainer(var count: Long, var timer: Int)