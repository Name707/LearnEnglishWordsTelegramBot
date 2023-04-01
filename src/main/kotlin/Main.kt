import kotlin.math.roundToInt

data class Word(
    val original: String,
    val translate: String,
    var correctAnswersCount: Int = 0,
)

fun Question.asConsoleString(): String {
    val variants = this.variants
        .mapIndexed { index, word -> "${index + 1} - ${word.translate}" }
        .joinToString("\n")
    return this.correctAnswer.original + "\n" + variants + "\n 0 выйти в меню"
}

fun main() {
    val trainer = LearnWordsTrainer()
    while (true) {
        println("Меню: 1 – Учить слова, 2 – Статистика, 0 – Выход")
        when (readln().toInt()) {
            1 -> do {
                val question = trainer.getNextQuestion()
                if (question == null) {
                    println("Вы выучили все слова")
                    break
                } else {
                    println(question.asConsoleString())
                    val userAnswer = readln().toIntOrNull()
                    if (userAnswer == 0) break
                    if (trainer.checkUserAnswer(userAnswer?.minus(1))) {
                        println("Правильно!\n")
                    } else println("Ответ неверный.\n")
                }
            } while (question?.variants?.isNotEmpty() == true)

            2 -> {
                val statistics = trainer.getStatistics()
                println(
                    "Список содержит ${statistics.learnedWords} выученных слов." + "\nВыучено $statistics.learnedWords из ${statistics.allWords} слов | " +
                            "${(((statistics.learnedWords).toDouble() / statistics.percentageOfLearnedWords).roundToInt())}%"
                )
            }

            0 -> break
            else -> println("Такой команды нет, выберите нужную команду.")
        }
    }
}