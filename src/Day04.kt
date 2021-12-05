fun main() {
    fun part1(input: List<String>): Int {
        return Bingo.parse(input).run()
    }

    fun part2(input: List<String>): Int {
        return Bingo.parse(input).runReversed()
    }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class Bingo(val list: List<Int>, val boards: List<BingoBoard>) {

    fun run(): Int {

        for (num in list) {
            for (board in boards) {
                if (board.setCheckWin(num)) return board.calculateScore(num)
            }
        }

        return 0
    }

    fun runReversed(): Int {

        val results = mutableListOf<Pair<Int, Int>>()

        for ((i, num) in list.withIndex()) {
            for (board in boards.filter { !it.wonGame }) {
                if (board.setCheckWin(num)) results.add(Pair(i, board.calculateScore(num)))
            }
        }

        return results.maxByOrNull { it.first }!!.second
    }

    companion object {
        fun parse(input: List<String>): Bingo {
            val numberList = input[0].split(",").map { it.toInt() }
            val boards = mutableListOf<BingoBoard>()

            var data = mutableListOf<Int>()

            input.forEachIndexed { i, line ->
                if (i == 0) return@forEachIndexed

                if (line.isEmpty() && data.isNotEmpty()) {
                    boards.add(BingoBoard(data.toIntArray()))
                    data = mutableListOf()
                }

                data.addAll(line.split(" ").mapNotNull { if (it.isBlank()) null else it.trim().toInt() })
            }

            if (data.isNotEmpty()) boards.add(BingoBoard(data.toIntArray()))

            return Bingo(numberList, boards)
        }
    }
}

data class BingoBoard(private val data: IntArray) {
    val marked = mutableListOf<Int>()
    private var alreadyWon = false
    var index = 0

    val wonGame: Boolean
        get() = alreadyWon || checkWin()

    operator fun get(x: Int, y: Int) = data[x * 5 + y]

    fun setCheckWin(num: Int): Boolean {
        if (!wonGame) index++
        if (!data.contains(num)) return false

        marked.add(num)

        return checkWin()
    }

    fun checkWin(): Boolean {

        if (alreadyWon) return true

        val markedData = data.map { MarkedData(it, it in marked) }

        val rows = List(5) {
            val index = it * 5
            markedData.subList(index, index + 5)
        }

        rows.forEach { list ->
            if (list.all { it.marked }) return true.also { alreadyWon = true }
        }

        val columns = List(5) {
            val list = mutableListOf<MarkedData>()
            for (index in 0..4) {
                list.add(markedData[it + index * 5])
            }

            list
        }

        columns.forEach { list ->
            if (list.all { it.marked }) return true.also { alreadyWon = true }
        }

        return false
    }

    fun calculateScore(num: Int): Int {
        return num * data.map { MarkedData(it, it in marked) }.filter { !it.marked }.sumOf { it.data }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BingoBoard

        if (!data.contentEquals(other.data)) return false
        if (marked != other.marked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + marked.hashCode()
        return result
    }

    data class MarkedData(val data: Int, val marked: Boolean)
}