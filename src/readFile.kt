import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths

fun main() {
    val input = readFile("day_1/test_input.txt")
    println(input)
}
fun readFile(pathFromSrc: String): String {
    val path = Paths.get("").toAbsolutePath().toString() + "/AoC_2025/src/" + pathFromSrc
    println(path)
    val bufferedReader: BufferedReader = File(path).bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.trim()
}