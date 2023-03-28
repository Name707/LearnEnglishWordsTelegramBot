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
    val learnedWords = dictionary.filter { it.correctAnswersCount >= 3 }.size

    while (true) {
        println("Меню: 1 – Учить слова, 2 – Статистика, 0 – Выход")

        when (readln().toInt()) {
            1 -> do {
                val unlearnedWordsList = dictionary.filter { it.correctAnswersCount < 3 }
                val randomFourUnlearnedWords = unlearnedWordsList.shuffled().take(4)

                if (unlearnedWordsList.isEmpty()) {
                    println("Вы выучили все слова")
                    break
                } else {
                    println("Выберите правильный перевод слова \"${randomFourUnlearnedWords.random().original}\":\n" +
                            randomFourUnlearnedWords.map { it.translate }
                    )
                    val userAnswer = readln()
                }

            } while (unlearnedWordsList.isNotEmpty())

            2 -> println(
                "Список содержит $learnedWords выученных слов." +
                        "\nВыучено $learnedWords из ${dictionary.size} слов | " +
                        "${(((learnedWords).toDouble() / dictionary.size) * 100).roundToInt()}%"
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