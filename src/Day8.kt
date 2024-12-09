import util.*

fun main() {
    val input = readFileInput("day8.txt")

    partOneNumberOfUniqueAntinodes(input)
}

private fun partOneNumberOfUniqueAntinodes(input: List<String>) {
    val antennas = input.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c.isLetter() || c.isDigit()) Antenna(type = c, point = Point(x, y)) else null
        }
    }.groupBy({ it.type }) { it.point }

    val boardSize = Size(input.size, input.first().length)
    val totalAntinodes = antennas.flatMap { (_, points) ->
        findAntinodes(points, boardSize = boardSize)
    }.toSet()

    println(totalAntinodes.size)
}

private fun findAntinodes(points: List<Point>, boardSize: Size): List<Point> {
    if (points.size < 2) return emptyList()
    if (points.size == 2) {
        val distance = points[0].distanceFrom(points[1])
        val firstAntinode = points[0] + distance
        val secondAntinode = points[0] - distance * 2
        return listOf(firstAntinode, secondAntinode).filter { it.isWithinBounds(boardSize) }
    }

    return buildList {
        for ((index, first) in points.withIndex()) {
            if (index == points.lastIndex) break

            for (second in points.slice((index + 1)..points.lastIndex)) {
                addAll(findAntinodes(listOf(first, second), boardSize))
            }
        }
    }
}

private data class Antenna(
    val type: Char,
    val point: Point,
)
