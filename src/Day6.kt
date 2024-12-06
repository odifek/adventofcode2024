import kotlin.system.measureTimeMillis

private const val GUARD = '^'
private const val OBSTRUCTION = '#'
fun main() {

    val input = readFileInput("day6.txt")
    val pathToExit = partOne(input)
    measureTimeMillis { partTwo(pathToExit, input) }.let { println("Part 2 time: $it millis") }

}

private fun partTwo(exitPath: List<Position>, input: List<String>) {
    val inputMatrix = InputMatrix(input)
    val uniquePossibleCoords = mutableSetOf<Coord>()

    loop1@ for (position in exitPath.subList(1, exitPath.size)) {
        var currentPos = exitPath.first()
        if (position.coord == exitPath.first().coord) {
            // Skip original position
            continue@loop1
        }
        val modifiedMatrix = addObstructionAtCoordinates(position.coord, inputMatrix)
        val currentLoop = mutableMapOf<Position, Unit>()
        while (true) {
            val futurePosition = currentPos.move()
            if (isInsideArea(futurePosition.coord, modifiedMatrix)) {
                currentPos = if (hasAnyObstruction(futurePosition.coord, modifiedMatrix)) {
                    currentPos.turnRight()
                } else {
                    futurePosition
                }
                if (currentLoop.contains(currentPos)) {
                    // We've gone this way before. Definitely in an infinite loop
                    uniquePossibleCoords.add(position.coord)
                    break
                }
                currentLoop[currentPos] = Unit
            } else {
                break
            }
        }
    }
    println("Possible Infinite Loops")
    println(uniquePossibleCoords.size)
}

private fun partOne(input: List<String>): List<Position> {

    // Accumulate the entire positions and direction to exit the matri
    val pathToExit = mutableListOf<Position>()
    val locationsVisited = mutableSetOf<Coord>()
    var currentPos = Position(Coord(x = -1, y = -1), direction = Direction.Up)
    for ((index, line) in input.withIndex()) {
        val guardPosition = line.indexOf(GUARD)
        if (guardPosition != -1) {
            currentPos = currentPos.copy(coord = Coord(x = guardPosition, y = index))
            break
        }
    }

    val inputMatrix = InputMatrix(input)
    locationsVisited.add(currentPos.coord)
    pathToExit.add(currentPos)
    while (true) {
        val futurePosition = currentPos.move()
        if (isInsideArea(futurePosition.coord, inputMatrix)) {
            currentPos = if (hasAnyObstruction(futurePosition.coord, inputMatrix)) {
                currentPos.turnRight()
            } else {
                futurePosition.also {
                    locationsVisited.add(it.coord)
                }
            }
            pathToExit.add(currentPos)
        } else {
            break
        }
    }

    println(locationsVisited.size)
    println("Exit path length: ${pathToExit.size}")
    println()
    return pathToExit
}

private fun isInsideArea(position: Coord, matrix: InputMatrix): Boolean =
    position.x in 0..<matrix.columnCount && position.y in 0..<matrix.rowCount

private fun hasAnyObstruction(position: Coord, matrix: InputMatrix): Boolean {
    return matrix.rows[position.y][position.x] == OBSTRUCTION
}

private fun move(coord: Coord, direction: Direction): Coord {
    return when (direction) {
        Direction.Up -> coord.copy(y = coord.y - 1)
        Direction.Down -> coord.copy(y = coord.y + 1)
        Direction.Left -> coord.copy(x = coord.x - 1)
        Direction.Right -> coord.copy(x = coord.x + 1)
    }
}

private fun addObstructionAtCoordinates(coord: Coord, inputMatrix: InputMatrix): InputMatrix {
    val mutableLines = inputMatrix.rows.toMutableList()
    val lineToUpdate = inputMatrix.rows[coord.y]
    val lhs = lineToUpdate.substring(0, coord.x)
    val rhs = if (coord.x < lineToUpdate.lastIndex) {
        lineToUpdate.substring(coord.x + 1, lineToUpdate.length)
    } else {
        ""
    }
    val updatedLine = lhs + OBSTRUCTION + rhs
    mutableLines[inputMatrix.rows.indexOf(lineToUpdate)] = updatedLine
    return InputMatrix(mutableLines)
}

private enum class Direction {
    Up, Down, Left, Right
}

private data class Position(
    val coord: Coord,
    val direction: Direction,
) {
    fun move() = copy(coord = move(coord = this.coord, direction))

    fun turnRight() = copy(
        direction = when (direction) {
            Direction.Up -> Direction.Right
            Direction.Down -> Direction.Left
            Direction.Left -> Direction.Up
            Direction.Right -> Direction.Down
        }
    )
}

private data class Coord(val x: Int, val y: Int)

private data class InputMatrix(
    val rows: List<String>,
) {
    val rowCount = rows.size
    val columnCount = rows.first().length
}
