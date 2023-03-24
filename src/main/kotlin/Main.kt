import java.io.File

fun main() {

    val wordsFile: File = File("words.txt")
    wordsFile.createNewFile()

    val dictionary: MutableList<Word> = mutableListOf()
    val wordsToLinesList: List<String> = wordsFile.readLines()

    for (line in wordsToLinesList) {
        val lineOfWordsList = line.split("|")
        dictionary.add(Word(lineOfWordsList[0], lineOfWordsList[1], lineOfWordsList[2].toInt()))

    }

    println("Количество правильных ответов: ${dictionary.sumOf { it.correctAnswersCount }}")
    println(dictionary)

}

class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int = 0,
) {
    override fun toString(): String {
        return "Слово: $original, перевод: $translate, правильных ответов: $correctAnswersCount"
    }
}