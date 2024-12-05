private val rulesMap = mutableMapOf<Long, RuleForPage>()
fun main() {
    val input = readFileInput("day5.txt")

    val invalidLines = part1FindValidLines(input)
    part2RearrangeInvalidLines(invalidLines)
}

private fun part2RearrangeInvalidLines(invalidLines: List<List<Long>>) {
    val correctedLines = invalidLines.map { line ->
        val sortedPages = mutableListOf(line.first())
        for ((index, currentPage) in line.withIndex()) {
            if (index == 0) continue

            var indexToInsert = 0
            for ((sortedIndex, sortedPage) in sortedPages.withIndex()) {
                val ruleForPage = rulesMap[currentPage]!!
                val ruleForSorted = rulesMap[sortedPage]!!
                if (ruleForPage.mustBefore.contains(sortedPage) || ruleForSorted.mustAfter.contains(currentPage)) {
                    indexToInsert++
                } else {
                    indexToInsert = sortedIndex
                    break
                }
            }
            sortedPages.add(indexToInsert, currentPage)
        }
        sortedPages.toList()
    }
    println(sumOfMiddlesPages(correctedLines))
}

private fun part1FindValidLines(input: List<String>): List<List<Long>> {
    val pageOrderingRules = input.subList(0, toIndex = input.indexOfFirst { it.isBlank() })
        .map { it.split("|").let { (first, second) -> first.toLong() to second.toLong() } }

    val pageUpdates = input.subList(
        fromIndex = input.indexOfFirst { it.isBlank() } + 1,
        toIndex = input.size,
    ).map { it.split(",").map(String::toLong) }

    val validLines = mutableListOf<List<Long>>()
    val invalidLines = mutableListOf<List<Long>>()
    for (line in pageUpdates) {
        var isUpdateValid = true
        for ((index, page) in line.withIndex()) {
            val ruleForPage = rulesMap.getOrPut(page) { getRulesForPage(page, pageOrderingRules) }
            val before = if (index > 0) line.subList(0, index) else emptyList()
            val after = if (index != line.lastIndex) line.subList(index + 1, line.size) else emptyList()
            isUpdateValid = ruleForPage.mustBefore.intersect(after.toSet()).isEmpty()
                    && ruleForPage.mustAfter.intersect(before.toSet()).isEmpty()
            if (!isUpdateValid) {
                invalidLines.add(line)
                break
            }
        }
        if (isUpdateValid) {
            validLines.add(line)
        }
    }
    val sumOfMiddlePages = sumOfMiddlesPages(validLines)

    println(sumOfMiddlePages)
    return invalidLines
}

private fun sumOfMiddlesPages(validLines: List<List<Long>>) =
    validLines.sumOf { line ->
        line[line.size / 2]
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
