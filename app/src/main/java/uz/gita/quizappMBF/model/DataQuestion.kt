package uz.gita.quizappMBF.model

data class DataQuestion(
    val id: Int,
    val question: String,
    val correctAnswer: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String
)
