package uz.gita.quizappMBF.ui.viewmodels

import androidx.lifecycle.LiveData

interface QuizViewModel {
    //    val dataLiveData: LiveData<List<DataQuestion>>
    val quizCategoryLiveData: LiveData<String>
    val quizQuestionsCountLiveData: LiveData<String>
    val questionLiveData: LiveData<String>
    val optionsLiveData: LiveData<ArrayList<String>>
    val questionOrderAndCountLiveData: LiveData<String>
    val correctAnswersCountLiveData: LiveData<String>
    val timerLiveData: LiveData<String>
    val messageLiveData: LiveData<String>
    val closeActionLiveData: LiveData<Unit>
    val onTimeOver: LiveData<Boolean>

    fun onTimeOver(): Boolean
    fun onAnswerSelect(answer: String): Boolean
    fun onNext(): Boolean
    fun onQuizEndReturnAnswersCount(): Int
//    fun onRestart()
}