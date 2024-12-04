private const val doRegex = "(do\\(\\))"
private const val dontRegex = "(don't\\(\\))"
private const val mulRegex = "(mul\\(\\d{1,3},\\d{1,3}\\))"
private const val mulExtractValuesRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)"

fun main() {

    val input = readFileInput("day3.txt").joinToString()


    // Sum of all
    // part1
    sumUpInstructionsResult(input)
        .let(::println)

    println(part2(input))
}

private fun part2(input: String): Int {
    var sum = 0
    var enable = true

    "$doRegex|$dontRegex|$mulRegex".toRegex().findAll(input).forEach { result ->
        when(result.value) {
            "do()" -> enable = true
            "don't()" -> enable = false
            else -> if (enable) {
               sum += sumUpInstructionsResult(result.value)
            }
        }
    }
    return sum
}

private fun sumUpInstructionsResult(input: String) = mulExtractValuesRegex.toRegex().findAll(input)
    .map { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
    .sum()
