private const val GUARD = '^'
private const val OBSTRUCTION = '#'
fun main() {

    val input = readFileInput("day6.txt")
    partOne(input)
}

private fun partOne(input: List<String>) {

    val positionsVisited = mutableSetOf<Coord>()
    var currentPos = Position(Coord(x = -1, y = -1), direction = Direction.Up)
    for ((index, line) in input.withIndex()) {
        val guardPosition = line.indexOf(GUARD)
        if (guardPosition != -1) {
            currentPos = currentPos.copy(coord = Coord(x = guardPosition, y = index))
            break
        }
    }

    val inputMatrix = InputMatrix(input)
    positionsVisited.add(currentPos.coord)
    while (true) {
        val futurePosition = currentPos.move()
        if (isInsideArea(futurePosition.coord, inputMatrix)) {
            currentPos = if (hasAnyObstruction(futurePosition.coord, inputMatrix)) {
                currentPos.turnRight()
            } else {
                futurePosition.also { positionsVisited.add(it.coord)}
            }
        } else {
            break
        }
    }

    println(positionsVisited.size)
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
