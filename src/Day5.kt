fun main() {
    val input = readFileInput("day5.txt")

    part1(input)
}

private fun part1(input: List<String>) {
    val pageOrderingRules = input.subList(0, toIndex = input.indexOfFirst { it.isBlank() })
        .map { it.split("|").let { (first, second) -> first.toLong() to second.toLong() } }

    val pageUpdates = input.subList(
        fromIndex = input.indexOfFirst { it.isBlank() } + 1,
        toIndex = input.size,
    ).map { it.split(",").map(String::toLong) }

    val validLines = mutableListOf<List<Long>>()
    for (line in pageUpdates) {
        var isUpdateValid = true
        for ((index, page) in line.withIndex()) {
            val ruleForPage = getRulesForPage(page, pageOrderingRules)
            val before = if (index > 0) line.subList(0, index) else emptyList()
            val after = if (index != line.lastIndex) line.subList(index + 1, line.size) else emptyList()
            isUpdateValid = ruleForPage.mustBefore.intersect(after.toSet()).isEmpty()
                    && ruleForPage.mustAfter.intersect(before.toSet()).isEmpty()
            if (!isUpdateValid) {
                break
            }
        }
        if (isUpdateValid) {
            validLines.add(line)
        }
    }
    val sumOfMiddlePages = validLines.sumOf { line ->
        line[line.size / 2]
    }

    println(sumOfMiddlePages)
}

private fun getRulesForPage(page: Long, allRules: List<Pair<Long, Long>>): RuleForPage {
    return RuleForPage(
        page = page,
        mustBefore = allRules.filter { it.second == page }.map { it.first },
        mustAfter = allRules.filter { it.first == page }.map { it.second },
    )
}

data class RuleForPage(
    val page: Long,
    val mustBefore: List<Long>,
    val mustAfter: List<Long>,
)
