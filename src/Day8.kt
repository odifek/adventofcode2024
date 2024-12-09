import util.*

fun main() {
    val input = readFileInput("sample8.txt")

    val antennas = input.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, c ->
            if (c.isLetter() || c.isDigit()) Antenna(type = c, point = Point(x, y)) else null
        }
    }.groupBy({ it.type }) { it.point }

    val boardSize = Size(input.size, input.first().length)

    partOneNumberOfUniqueAntinodes(antennas, boardSize)
    partTwoNumberOfUniqueAntinodes(antennas, boardSize)
}

private fun partOneNumberOfUniqueAntinodes(antennas: Map<Char, List<Point>>, boardSize: Size) {
    val totalAntinodes = antennas.flatMap { (_, points) ->
        findAntinodes(points, boardSize = boardSize, includeHarmonics = false)
    }.toSet()

    println(totalAntinodes.size)
}

private fun partTwoNumberOfUniqueAntinodes(antennas: Map<Char, List<Point>>, boardSize: Size) {
    val totalAntinodes = antennas.flatMap { (_, points) ->
        findAntinodes(points, boardSize = boardSize, includeHarmonics = true)
    }.toSet()

    println(totalAntinodes.size)
}

private fun findAntinodes(points: List<Point>, boardSize: Size, includeHarmonics: Boolean): List<Point> {
    if (points.size < 2) return emptyList()
    if (points.size == 2) {
        return findAllAntinodes(points, boardSize, includeHarmonics)
    }

    return buildList {
        for ((index, first) in points.withIndex()) {
            if (index == points.lastIndex) break

            for (second in points.slice((index + 1)..points.lastIndex)) {
                addAll(findAntinodes(listOf(first, second), boardSize, includeHarmonics))
            }
        }
    }
}

private fun findAllAntinodes(
    points: List<Point>,
    boardSize: Size,
    includeHarmonics: Boolean,
): List<Point> {
    val distance = points[0].distanceFrom(points[1])
    return buildList {
        if (includeHarmonics) {
            var harmonic = 1
            while (true) {
                val firstAntinode = points[0] + distance * harmonic
                if (!firstAntinode.isWithinBounds(boardSize)) {
                    break
                } else {
                    add(firstAntinode)
                }
                harmonic++
            }
            while (true) {
                val secondAntinode = points[0] - distance * (harmonic)
                if (!secondAntinode.isWithinBounds(boardSize)) {
                    break
                } else {
                    add(secondAntinode)
                }
                harmonic++
            }
        } else {
            val firstAntinode = points[0] + distance
            val secondAntinode = points[0] - distance * 2
            if (firstAntinode.isWithinBounds(boardSize)) {
                add(firstAntinode)
            }
            if (secondAntinode.isWithinBounds(boardSize)) {
                add(secondAntinode)
            }
        }
    }
}

private data class Antenna(
    val type: Char,
    val point: Point,
)
