import java.io.File
import kotlin.math.roundToInt

fun main() {

    val wordsFile: File = File("words.txt")
    wordsFile.createNewFile()

    val dictionary: MutableList<Word> = mutableListOf()
    val wordsToLinesList: List<String> = wordsFile.readLines()

    for (line in wordsToLinesList) {
        val lineOfWordsList = line.split("|")
        dictionary.add(Word(lineOfWordsList[0], lineOfWordsList[1], lineOfWordsList[2].toInt()))

    }
    while (true) {
        println("Меню: 1 – Учить слова, 2 – Статистика, 0 – Выход")

        when (readln().toInt()) {
            1 -> println("Выбрали команду 1")
            2 -> println(
                "Список содержит ${dictionary.filter { it.correctAnswersCount >= 3 }.size} выученных слов." +
                        "\nВыучено ${dictionary.filter { it.correctAnswersCount >= 3 }.size} из ${dictionary.size} слов | " +
                        "${(((dictionary.filter { it.correctAnswersCount >= 3 }.size).toDouble() / dictionary.size) * 100).roundToInt() }%"
            )

            0 -> break
            else -> println("Такой команды нет, выберите нужную команду.")
        }
    }
}

class Word(
    val original: String,
    val translate: String,
    val correctAnswersCount: Int = 0,
) {

}