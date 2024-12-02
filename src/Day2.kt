fun main() {

    val reports = readFileInput("day2.txt")
        .map { it.split("\\s".toRegex()).map { it.toInt() } }

    val countSafe = reports.count { report ->
        isSafeReport(report)
    }
    println("Safe")
    println(countSafe)

    val countSafeDampened = reports.count { isSafeWithDampner(it) }
    println("Safe dampened")
    println(countSafeDampened)
}

private fun isSafeWithDampner(report: List<Int>): Boolean {
    if (isSafeReport(report)) return true
    for (i in report.indices) {
        val subreport = report.removeElementAt(i)
        if (isSafeReport(subreport)) {
            return true
        }
    }
    return false
}

private fun isSafeReport(report: List<Int>): Boolean {
    var prev = report.first()
    var isSafe = true
    val isIncreasing = report[0] < report[1]
    for (i in 1..<report.size) {
        val curr = report[i]
        if (isIncreasing) {
            if (curr <= prev || curr - prev > 3) {
                isSafe = false
                break
            }
        } else {
            if (curr >= prev || prev - curr > 3) {
                isSafe = false
                break
            }
        }
        prev = curr
    }
    return isSafe
}

