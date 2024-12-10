fun main() {
    val input = readFileInput("day9.txt").first()

    val fileBlocks = getFileBlocksFromInput(input)

    val defragedFileBlocks = defragmentFileBlocksPart1(fileBlocks)
    println(checksum(defragedFileBlocks))
}

private fun checksum(fileBlocks: List<FileBlock>): Long {
    var sum = 0L
    for ((index, file) in fileBlocks.withIndex()) {
        when (file) {
            is FileBlock.Free -> break
            is FileBlock.File -> sum += index * file.id
        }
    }
    return sum
}

private fun getFileBlocksFromInput(input: String) = input.flatMapIndexed { index, c ->
    if (index % 2 == 0) {
        (0..<c.digitToInt()).map { FileBlock.File((index / 2)) }
    } else {
        (0..<c.digitToInt()).map { FileBlock.Free }
    }
}

private fun defragmentFileBlocksPart1(fileBlocks: List<FileBlock>): List<FileBlock> {
    val mutableFileBlocks = fileBlocks.toMutableList()
    var leftPointer = 0
    var rightPointer = fileBlocks.lastIndex

    while (true) {
        if (leftPointer >= rightPointer) break

        if (mutableFileBlocks[leftPointer] !is FileBlock.Free) {
            leftPointer += 1
        }
        if (mutableFileBlocks[rightPointer] is FileBlock.Free) {
            rightPointer -= 1
        }
        if (mutableFileBlocks[leftPointer] is FileBlock.Free && mutableFileBlocks[rightPointer] is FileBlock.File) {
            swap(leftPointer, rightPointer, mutableFileBlocks)
        }
    }
    return mutableFileBlocks.toList()
}

private fun defragmentFileBlocksPart2(fileBlocks: List<FileBlock>): List<FileBlock> {
    val mb = fileBlocks.toMutableList()
    var leftPointer = 0
    var leftIndexEnd = 0

    var rightPointer = fileBlocks.lastIndex
    var rightStartIndex = fileBlocks.lastIndex

    while (true) {
        if (leftPointer >= rightPointer) break


        while (mb[rightPointer] is FileBlock.Free && rightPointer > leftPointer) {
            --rightPointer
        }

        while (mb[rightStartIndex] == mb[rightPointer] && rightStartIndex > leftPointer) {
            --rightStartIndex
        }

        while (mb[leftPointer] !is FileBlock.Free && leftPointer < rightStartIndex) {
            ++leftPointer
            leftIndexEnd = leftPointer
        }
        // Move to end of free space
        while (mb[leftIndexEnd+1] is FileBlock.Free && leftIndexEnd < rightStartIndex) {
            ++leftIndexEnd
        }

    }
    return mb.toList()
}

private fun <T> swap(aIndex: Int, bIndex: Int, mutableList: MutableList<T>) {
    val aTemp = mutableList[aIndex]
    mutableList[aIndex] = mutableList[bIndex]
    mutableList[bIndex] = aTemp
}

private fun <T> swap(aRange: IntRange, bRange: IntRange, mutableList: MutableList<T>) {
    require(aRange.count() == bRange.count()) { "aRange count should equal bRange count" }
    val bIterator = bRange.iterator()
    aRange.forEach { aIndex ->
        swap(aIndex, bIterator.next(), mutableList)
    }
}

private sealed interface FileBlock {
    data object Free : FileBlock

    @JvmInline
    value class File(val id: Int) : FileBlock
}
