import java.io.File

fun readFileInput(inputFile: String) = File("src/input/$inputFile").readLines()
