import kotlin.system.measureTimeMillis

fun main() {

    val initialStones = readFileInput("day11.txt")
        .first().split(" ")
        .map { it.toLong() }

    measureTimeMillis {
        println(partOne(initialStones, blinkTimes = 25))
    } .also { println(it) }
}

private fun partOne(stones: List<Long>, blinkTimes: Int): Int {
    val mutableLine = stones.toMutableList()
    repeat(blinkTimes) {
        var currentIndex = 0
        while (true) {
            if (currentIndex > mutableLine.lastIndex) break
            val currentStone = mutableLine[currentIndex]
            when {
                currentStone == 0L -> mutableLine.set(currentIndex, 1L).also { currentIndex++ }
                (currentStone.toString().length % 2 == 0) -> {
                    val stringStone = currentStone.toString()
                    val firstHalf = stringStone.substring(0..<stringStone.length / 2).toLong()
                    val secondHalf = stringStone.substring(stringStone.length / 2).toLong()
                    mutableLine[currentIndex] = firstHalf
                    mutableLine.add(++currentIndex, secondHalf)
                    currentIndex++
                }

                else -> {
                    mutableLine[currentIndex] = currentStone * 2024L
                    currentIndex++
                }
            }
        }
    }
    return mutableLine.size
}

