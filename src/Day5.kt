fun main() {
    val input = readFileInput("day5.txt")

    val pageOrderingRules = input.subList(0, toIndex = input.indexOfFirst { it.isBlank() })
        .map { it.split("|").let { (first, second) -> first.toLong() to second.toLong() } }

    val pageUpdates = input.subList(
        fromIndex = input.indexOfFirst { it.isBlank() } + 1,
        toIndex = input.size,
    ).map { it.split(",").map(String::toLong) }

    val validLines = mutableListOf<List<Long>>()
    for (line in pageUpdates) {
        var isUpdateValid = true
        line.forEachIndexed { index, page ->
            val ruleForPage = getRulesForPage(page, pageOrderingRules)
            val before = line.subList(0, index)
            val after = if (index != line.lastIndex) line.subList(index + 1, line.size) else emptyList()

            isUpdateValid =  ruleForPage.mustBefore.intersect(after.toSet()).isNotEmpty()
                    || ruleForPage.mustAfter.intersect(before.toSet()).isNotEmpty()
            if (!isUpdateValid) {
                return@forEachIndexed
            }
        }
        if (isUpdateValid) {
            validLines.add(line)
        }
    }
    val sumOfMiddlePages = validLines.sumOf { line ->
        line[line.size / 2]
    }

    println(pageOrderingRules)
    println(pageUpdates)
    println(validLines)
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
