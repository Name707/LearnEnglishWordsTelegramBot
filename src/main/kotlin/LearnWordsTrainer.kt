import java.io.File

data class Statistics(
    val learnedWords: Int,
    val allWords: Int, val percentageOfLearnedWords: Int,
)

data class Question(
    val variants: List<Word>,
    val correctAnswer: Word,
)

class LearnWordsTrainer {
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
        val unlearnedWordsList = dictionary.filter { it.correctAnswersCount < 3 }
        if (unlearnedWordsList.isEmpty()) return null
        val randomFourUnlearnedWords = unlearnedWordsList.shuffled().take(4)
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
        val dictionary: MutableList<Word> = mutableListOf()
        val wordsFile: File = File("words.txt")
        wordsFile.createNewFile()
        val wordsToLinesList: List<String> = wordsFile.readLines()
        for (line in wordsToLinesList) {
            val lineOfWordsList = line.split("|")
            dictionary.add(Word(lineOfWordsList[0], lineOfWordsList[1], lineOfWordsList[2].toInt()))
        }
        return dictionary
    }

    private fun saveDictionary(words: List<Word>) {
        val wordsFile: File = File("words.txt")
        wordsFile.writeText("")
        for (word in words) wordsFile.appendText(("${word.original}|${word.translate}|${word.correctAnswersCount}\n"))
    }
}