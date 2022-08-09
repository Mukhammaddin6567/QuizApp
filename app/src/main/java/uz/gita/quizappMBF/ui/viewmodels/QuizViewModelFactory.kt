package uz.gita.quizappMBF.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.gita.quizappMBF.ui.fragments.quiz.QuizViewModelImpl

class QuizViewModelFactory(
    private val application: Application,
    private val id: Int,
    private val category: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModelImpl::class.java)) {
            return QuizViewModelImpl(application, id, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}