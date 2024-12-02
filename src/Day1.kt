import java.io.File
import kotlin.math.abs

// aoc-2024-in-kotlin
// https://adventofcode.com/2024/day/1
fun main() {

    // Part 1 - total distance

    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    readFileInput("day1.txt")
        .forEach { line ->
            left.add(line.takeWhile { it.isDigit() }.toInt())
            right.add(line.takeLastWhile { it.isDigit() }.toInt())
        }
    val leftSorted = left.sorted()
    val rightSorted = right.sorted()

    var totalDistance = 0

    leftSorted.forEachIndexed { index, value ->
        totalDistance += abs(value - rightSorted[index])
    }

    print("Total Distance: ")
    print(totalDistance)
    println()

    // Part Two - similarity score

    var similarityScore = 0
    leftSorted.forEach {  value ->
        similarityScore += value * rightSorted.count { it == value }
    }
    print("Similarity Score: ")
    print(similarityScore)
    println()


}

