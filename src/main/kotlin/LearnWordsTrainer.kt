import java.io.File
import java.lang.IllegalStateException

data class Statistics(
    val learnedWords: Int,
    val allWords: Int, val percentageOfLearnedWords: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer(
    private val learnedAnswerCount: Int = 3,
    private val countOfQuestionWords: Int = 4,
) {
    private var question: Question? = null
    private val dictionary = loadDictionary()

    fun getStatistics(): Statistics {
        val learnedWords = dictionary.filter { it.correctAnswersCount >= 3 }.size
        val allWords = dictionary.size
        val percentageOfLearnedWords = dictionary.size * 100
        return Statistics(
            learnedWords,
            allWords,
            percentageOfLearnedWords
        )
    }

    fun getNextQuestion(): Question? {
        val unlearnedWordsList = dictionary.filter { it.correctAnswersCount < learnedAnswerCount }
        if (unlearnedWordsList.isEmpty()) return null
        val randomFourUnlearnedWords = if (unlearnedWordsList.size < countOfQuestionWords) {
            val learnedList = dictionary.filter { it.correctAnswersCount >= learnedAnswerCount }.shuffled()
            unlearnedWordsList.shuffled()
                .take(countOfQuestionWords) + learnedList.take(countOfQuestionWords - unlearnedWordsList.size)
        } else {
            unlearnedWordsList.shuffled().take(countOfQuestionWords)
        }.shuffled()

        val randomWordForLearn = randomFourUnlearnedWords.random()
        question = Question(
            variants = randomFourUnlearnedWords, correctAnswer = randomWordForLearn,
        )
        return question
    }

    fun checkUserAnswer(userAnswerIndex: Int?): Boolean {
        return question?.let {
            val correctAnswerId =
                it.variants.indexOf(it.correctAnswer)
            if (correctAnswerId == userAnswerIndex) {
                it.correctAnswer.correctAnswersCount++
                saveDictionary(dictionary)
                true
            } else {
                false
            }
        } ?: false
    }

    private fun loadDictionary(): List<Word> {
        try {
            val dictionary: MutableList<Word> = mutableListOf()
            val wordsFile: File = File("words.txt")
            wordsFile.createNewFile()
            val wordsToLinesList: List<String> = wordsFile.readLines()
            for (line in wordsToLinesList) {
                val lineOfWordsList = line.split("|")
                dictionary.add(Word(lineOfWordsList[0], lineOfWordsList[1], lineOfWordsList[2].toInt()))
            }
            return dictionary
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalStateException("Некорректный файл")
        }
    }

    private fun saveDictionary(words: List<Word>) {
        val wordsFile: File = File("words.txt")
        wordsFile.writeText("")
        for (word in words) wordsFile.appendText(("${word.original}|${word.translate}|${word.correctAnswersCount}\n"))
    }
}