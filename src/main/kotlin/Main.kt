import java.io.File

fun main() {

    val words: File = File("words.txt")
    words.createNewFile()

    val wordsToList = words.readLines()
    for (wordsPair in wordsToList) println(wordsPair)

}