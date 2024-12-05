private const val XMAS = "XMAS"
private const val MAS = "MAS"

fun main() {

    // Not working yet
    val inputLines = readFileInput("day4.txt")

    println(part1CountXmas(inputLines))
    println(part2CountXmas(inputLines))
}

private fun part1CountXmas(input: List<String>): Long {
    val rowLength = input.first().length
    val columnLength = input.size
    var count = 0L
    // Scan through the rows, columns and diagonals
    for (i in 0..rowLength - 4) {
        for (j in 0..columnLength - 4) {
            val row1 = input[i].substring(j, j + 4)
            val row2 = input[i + 1].substring(j, j + 4)
            val row3 = input[i + 2].substring(j, j + 4)
            val row4 = input[i + 3].substring(j, j + 4)
            val matrix4 = Matrix4(
                diagonals = listOf(
                    "${row1[0]}${row2[1]}${row3[2]}${row4[3]}",
                    "${row1[3]}${row2[2]}${row3[1]}${row4[0]}",
                ),
                rows = if (i == 0) listOf(row1, row2, row3, row4) else listOf(row4),
                columns = if (j == 0) (0..3).map { "${row1[it]}${row2[it]}${row3[it]}${row4[it]}" }
                else listOf("${row1[3]}${row2[3]}${row3[3]}${row4[3]}")
            )
            count += countXmasInMatrix(matrix4)
        }
    }
    return count
}

private fun countXmasInMatrix(matrix4: Matrix4): Long {
    var count = 0L
    for (row in matrix4.rows) {
        if (row == XMAS || row.reversed() == XMAS) {
            count += 1
        }
    }

    for (column in matrix4.columns) {
        if (column == XMAS || column.reversed() == XMAS) {
            count += 1
        }
    }

    for (diagonal in matrix4.diagonals) {
        if (diagonal == XMAS || diagonal.reversed() == XMAS) {
            count += 1
        }
    }
    return count
}

private fun part2CountXmas(input: List<String>): Long {
    val rowLength = input.first().length
    val columnLength = input.size
    var count = 0L
    // Scan through the rows, columns and diagonals
    for (i in 0..rowLength - 3) {
        for (j in 0..columnLength - 3) {
            val row1 = input[i].substring(j, j + 3)
            val row2 = input[i + 1].substring(j, j + 3)
            val row3 = input[i + 2].substring(j, j + 3)
            val diagonals =
                "${row1[0]}${row2[1]}${row3[2]}" to
                        "${row1[2]}${row2[1]}${row3[0]}"
            if (makesAnXmas(diagonals)) {
                count++
            }
        }
    }
    return count
}


private fun makesAnXmas(diagonals: Pair<String, String>): Boolean {
    return (diagonals.first == MAS || diagonals.first.reversed() == MAS)
            && (diagonals.second == MAS || diagonals.second.reversed() == MAS)
}

data class Matrix4(
    val rows: List<String>,
    val columns: List<String>,
    val diagonals: List<String>,
)
