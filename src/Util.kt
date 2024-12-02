import java.io.File

fun readFileInput(inputFile: String) = File("src/input/$inputFile").readLines()

fun <T> List<T>.removeElementAt(index: Int): List<T> {
    if (index < 0 || index >= this.size) {
        throw IndexOutOfBoundsException("Index $index out of bounds for list of size ${this.size}")
    }
    return this.subList(0, index) + this.subList(index + 1, this.size)
}
