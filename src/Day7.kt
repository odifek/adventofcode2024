import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {

    val input = readFileInput("day7.txt")
    measureTimeMillis { partOneTotalCalibrationResult(input) }.also {
        println("Time taken: $it millis")
    }
}

private fun partOneTotalCalibrationResult(input: List<String>) {
    var total = 0L
    for (line in input) {
        val testResult = line.substring(startIndex = 0, endIndex = line.indexOf(":")).toLong()
        val operands = line.substring(line.indexOf(":") + 1)
            .trim()
            .split(" ")
            .map { it.toLong() }
        val numberOfOperatorsRequired = operands.size - 1
        val numberOfPossibleOperations = 2.0.pow(numberOfOperatorsRequired.toDouble()).toInt()


        for (operation in 0..numberOfPossibleOperations) {
            val operators = operation.toString(radix = 2)
                .padStart(numberOfOperatorsRequired, padChar = '0')
                .map { if (it == '0') Operator.Add else Operator.Multiply }
            val currentResult =
                operands.reduceIndexed { index, acc, next -> performOperation(operators[index - 1], acc, next) }
            if (currentResult == testResult) {
                total += currentResult

                val equation =
                    operands.mapIndexed { index, op -> if (index == 0) "$op" else "${operators[index - 1].ops}$op" }
                        .joinToString(separator = "")
                println("$testResult: $equation")
                break
            }
        }
    }
    println("Total calibration result: $total")

}

private fun performOperation(operator: Operator, op1: Long, op2: Long): Long = when (operator) {
    Operator.Add -> op1 + op2
    Operator.Multiply -> op1 * op2
}

private enum class Operator(val ops: Char) {
    Add('+'), Multiply('*')
}
