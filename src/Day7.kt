import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {

    val input = readFileInput("day7.txt")
    measureTimeMillis { totalCalibrationResult(input, supportedOperators = setOf(Operator.Add, Operator.Multiply)) }.also {
        println("Part1 - Time taken: $it millis")
    }
    measureTimeMillis { totalCalibrationResult(input, supportedOperators = setOf(Operator.Add, Operator.Multiply, Operator.Concat)) }.also {
        println("Part2 - Time taken: $it millis")
    }
}

private fun totalCalibrationResult(input: List<String>, supportedOperators: Set<Operator>) {
    var total = 0L
    for (line in input) {
        val testResult = line.substring(startIndex = 0, endIndex = line.indexOf(":")).toLong()
        val operands = line.substring(line.indexOf(":") + 1)
            .trim()
            .split(" ")
            .map { it.toLong() }
        val numberOfOperatorsRequired = operands.size - 1
        val numberOfPossibleOperations = supportedOperators.size.toDouble()
            .pow(numberOfOperatorsRequired.toDouble()).toLong()


        for (operation in 0..numberOfPossibleOperations) {
            val operators = operation.toString(radix = supportedOperators.size)
                .padStart(numberOfOperatorsRequired, padChar = '0')
                .map(Operator.Companion::fromCode)
            val currentResult =
                operands.reduceIndexed { index, acc, next -> performOperation(operators[index - 1], acc, next) }
            if (currentResult == testResult) {
                total += currentResult

                // val equation =
                //     operands.mapIndexed { index, op -> if (index == 0) "$op" else "${operators[index - 1].ops}$op" }
                //         .joinToString(separator = "")
                // println("$testResult: $equation")
                break
            }
        }
    }
    println("Total calibration result: $total")

}

private fun performOperation(operator: Operator, op1: Long, op2: Long): Long = when (operator) {
    Operator.Add -> op1 + op2
    Operator.Multiply -> op1 * op2
    Operator.Concat -> "$op1$op2".toLong()
}

private enum class Operator(val ops: String, val code: Char) {
    Add(ops = "+", code = '0'),
    Multiply(ops = "*", code = '1'),
    Concat(ops = "||", code = '2');

    companion object {
        fun fromCode(code: Char) = Operator.entries.first { it.code == code }
    }
}
