package uz.gita.quizappMBF.ui.fragments.quiz

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import timber.log.Timber
import uz.gita.quizappMBF.model.DataQuestion
import uz.gita.quizappMBF.repository.Repository
import uz.gita.quizappMBF.ui.viewmodels.QuizViewModel

class QuizViewModelImpl(application: Application, id: Int, category: String) :
    AndroidViewModel(application),
    QuizViewModel {

    private val repository: Repository = Repository.getRepository()

    //    override val dataLiveData: MutableLiveData<List<DataQuestion>> by lazy { MutableLiveData() }
    override val quizCategoryLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val quizQuestionsCountLiveData: MutableLiveData<String> by lazy { MutableLiveData() }

    override val questionLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val optionsLiveData: MutableLiveData<ArrayList<String>> by lazy { MutableLiveData() }
    override val questionOrderAndCountLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val correctAnswersCountLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val timerLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val messageLiveData: MutableLiveData<String> by lazy { MutableLiveData() }
    override val closeActionLiveData: MutableLiveData<Unit> by lazy { MutableLiveData() }
    override val onTimeOver: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    private var questions: List<DataQuestion> = ArrayList()
    private var options: ArrayList<String> = ArrayList()
    private var currentIndex: Int = 0
    private var correctAnswersCount: Int = 0
    private var correctAnswer: String = ""

    // for timer
    private var countDownTimer: CountDownTimer
    private val timerMillisInFuture = 31000L
    private val timerCountDownInterval = 1000L
    private val timerDone = 0L
    private var currentTimer = 0L

    companion object {
        private const val questionsCount: Int = 10
    }

    init {
//        Timber.d(repository.getScienceQuestionsList(application.resources).toString())
        Timber.d("init")

        countDownTimer = object : CountDownTimer(timerMillisInFuture, timerCountDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timerLiveData.value = (millisUntilFinished / timerCountDownInterval).toString()
//                Timber.d((millisUntilFinished / timerCountDownInterval).toString())
            }

            override fun onFinish() {
                onTimeOver.value = true
            }
        }
        countDownTimer.start()

        quizCategoryLiveData.value = category
        quizQuestionsCountLiveData.value = "$questionsCount"

        when (id) {
            0 -> questions = repository.getScienceQuestionsList(application.resources)
            1 -> questions = repository.getGeographyQuestionsList(application.resources)
            2 -> questions = repository.getSportQuestionsList(application.resources)
            3 -> questions = repository.getMovieQuestionsList(application.resources)
        }
        initFirstQuestions()
    }

    private fun initFirstQuestions() {
        questionLiveData.value = questions[currentIndex].question
        correctAnswer = questions[currentIndex].correctAnswer

        options.clear()
        options.add(questions[currentIndex].option1)
        options.add(questions[currentIndex].option2)
        options.add(questions[currentIndex].option3)
        options.add(questions[currentIndex].option4)
        options.shuffle()
        optionsLiveData.value = options

        questionOrderAndCountLiveData.value = "${++currentIndex} / $questionsCount"
    }

    override fun onTimeOver(): Boolean {
        if (currentIndex == questionsCount) {
            Timber.d("onTimeOver is false")
            return false
        }
        countDownTimer.cancel()
        countDownTimer.start()

        questionLiveData.value = questions[currentIndex].question
        correctAnswer = questions[currentIndex].correctAnswer
        options.clear()
        options.add(questions[currentIndex].option1)
        options.add(questions[currentIndex].option2)
        options.add(questions[currentIndex].option3)
        options.add(questions[currentIndex].option4)
        options.shuffle()
        optionsLiveData.value = options

        questionOrderAndCountLiveData.value = "${++currentIndex} / $questionsCount"
        onTimeOver.value = false
        return true
    }

    override fun onAnswerSelect(answer: String): Boolean {
        options.clear()
        countDownTimer.cancel()
        return if (answer == correctAnswer) {
            correctAnswersCount++
            correctAnswersCountLiveData.value = correctAnswersCount.toString()
            true
        } else false
    }

    override fun onNext(): Boolean {
        if (questionsCount != currentIndex) {
            options.clear()

            questionLiveData.value = questions[currentIndex].question
            correctAnswer = questions[currentIndex].correctAnswer

            options.add(questions[currentIndex].option1)
            options.add(questions[currentIndex].option2)
            options.add(questions[currentIndex].option3)
            options.add(questions[currentIndex].option4)
            options.shuffle()
            optionsLiveData.value = options

            questionOrderAndCountLiveData.value = "${++currentIndex} / $questionsCount"

            countDownTimer.start()
            return true
        }
        Timber.d("onNext is false")
        return false
    }

    override fun onQuizEndReturnAnswersCount(): Int {
        return correctAnswersCount
    }

    /*override fun onRestart() {
        currentIndex = 0
        correctAnswersCount = 0
        correctAnswer = ""
        countDownTimer.cancel()
        countDownTimer.start()
        initFirstQuestions()
    }*/
}