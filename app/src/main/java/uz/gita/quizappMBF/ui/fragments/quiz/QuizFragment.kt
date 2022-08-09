package uz.gita.quizappMBF.ui.fragments.quiz

import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import timber.log.Timber
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.Entity
import uz.gita.quizappMBF.database.MySharedPreferences
import uz.gita.quizappMBF.databinding.FragmentQuizBinding
import uz.gita.quizappMBF.extensions.getCurrentDate
import uz.gita.quizappMBF.extensions.onClick
import uz.gita.quizappMBF.extensions.snackMessage
import uz.gita.quizappMBF.ui.viewmodels.QuizViewModel
import uz.gita.quizappMBF.ui.viewmodels.QuizViewModelFactory


class QuizFragment : Fragment(R.layout.fragment_quiz), View.OnClickListener {

    // binds view
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    // database
    private val database by lazy { AppDatabase.getDatabase().databaseDao() }

    // args
    private val args: QuizFragmentArgs by navArgs()

    // viewModel factory
    private lateinit var factory: QuizViewModelFactory

    // private val viewModel: QuizViewModel by viewModels { factory }
    private lateinit var viewModel: QuizViewModel

    private var onBackPressedTime: Long = 0

    private var music: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var soundTrueAnswer = 1
    private var soundFalseAnswer = 2
    private var isMusicEnabled = false
    private var isSoundEnabled = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        factory = QuizViewModelFactory(requireActivity().application, args.id, args.category)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d(args.category)
        Timber.d(args.id.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, factory).get(QuizViewModelImpl::class.java)
        onBackPressed()
        createMusic()
        createSound()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewBinding(binding)
        subscribeViewModel(viewModel)
    }

    private fun subscribeViewBinding(binding: FragmentQuizBinding) = with(binding) {
        btnContinue.onClick {
            if (viewModel.onNext()) {
                if (isMusicEnabled) {
                    music?.seekTo(0)
                    music?.start()
                }
                reset()
            } else {
                Timber.d("!viewModel.onNext()")
                showWinner()
            }

        }
        btnNext.isEnabled = false
        btnNext.onClick {
            if (viewModel.onNext()) {
                if (isMusicEnabled) {
                    music?.seekTo(0)
                    music?.start()
                }
                reset()
            } else {
                Timber.d("!viewModel.onNext()")
                showWinner()
            }
        }
        btnBack.onClick {
            findNavController().popBackStack()
        }
        option1.setOnClickListener(this@QuizFragment)
        option2.setOnClickListener(this@QuizFragment)
        option3.setOnClickListener(this@QuizFragment)
        option4.setOnClickListener(this@QuizFragment)

        /*btnRestart.onClick {
            viewModel.onRestart()
        }*/
    }

    private fun subscribeViewModel(viewModel: QuizViewModel) = with(viewModel) {
        quizCategoryLiveData.observe(viewLifecycleOwner) {
            binding.category.text = it
        }
        questionOrderAndCountLiveData.observe(viewLifecycleOwner) {
            Timber.d("questionOrderAndCountLiveData: $it")
            val animation =
                android.view.animation.AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.anim_slide_from_top_to_center
                )
            with(binding) {
                textCurrentQuestion.startAnimation(animation)
                textCurrentQuestion.text =
                    resources.getString(R.string.text_current_question_and_questions_count, it)
//                textCurrentQuestion.clearAnimation()
            }

        }
        timerLiveData.observe(viewLifecycleOwner) {
            binding.timer.text = it
//            Timber.d("Timer: $it")
            binding.progressCircular.progress = it.toInt()
        }
        onTimeOver.observe(viewLifecycleOwner) {
            onTimeOverObserver(it)
        }
        questionLiveData.observe(viewLifecycleOwner) {
            YoYo.with(Techniques.ZoomIn).duration(600).playOn(binding.textQuestion)
            binding.textQuestion.text = it
        }
        optionsLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                option1.text = it[0]
                YoYo.with(Techniques.ZoomIn).duration(600).playOn(option1)
                option2.text = it[1]
                YoYo.with(Techniques.ZoomIn).duration(600).playOn(option2)
                option3.text = it[2]
                YoYo.with(Techniques.ZoomIn).duration(600).playOn(option3)
                option4.text = it[3]
                YoYo.with(Techniques.ZoomIn).duration(600).playOn(option4)
            }
        }
    }

    private fun reset() {
        val defaultShape =
            AppCompatResources.getDrawable(requireContext(), R.drawable.shape_option_default)
        val defaultColor = Color.WHITE
        with(binding) {
            option1.background = defaultShape
            option2.background = defaultShape
            option3.background = defaultShape
            option4.background = defaultShape

            option1.isClickable = true
            option2.isClickable = true
            option3.isClickable = true
            option4.isClickable = true

            option1.setTextColor(defaultColor)
            option2.setTextColor(defaultColor)
            option3.setTextColor(defaultColor)
            option4.setTextColor(defaultColor)

            btnNext.isEnabled = false
        }
    }

    override fun onClick(view: View) {
        if (isMusicEnabled) music?.pause()
        when (view.id) {
            R.id.option1,
            R.id.option2,
            R.id.option3,
            R.id.option4 -> {
                with(binding) {
                    option1.isClickable = false
                    option2.isClickable = false
                    option3.isClickable = false
                    option4.isClickable = false
                    btnNext.isEnabled = true
                }
                val selected = view as RadioButton

                Timber.d("selected textview: " + { selected.text.toString() })

                selected.setTextColor(
                    AppCompatResources.getColorStateList(
                        requireContext(),
                        R.color.color_status_bar
                    )
                )
                if (viewModel.onAnswerSelect(selected.text.toString())) {
                    YoYo.with(Techniques.FadeIn).duration(600).playOn(selected)
                    if (isSoundEnabled) soundPool?.play(soundTrueAnswer, 1f, 1f, 1, 0, 1f)
                    selected.background = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.shape_option_correct
                    )
                } else {
                    YoYo.with(Techniques.Shake).duration(600).playOn(selected)
                    if (isSoundEnabled) soundPool?.play(soundFalseAnswer, 1f, 1f, 1, 0, 1f)
                    selected.background = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.shape_option_wrong
                    )
                }
            }
        }
    }

    private fun onTimeOverObserver(value: Boolean) {
        if (value) with(binding) {
            option1.isClickable = false
            option2.isClickable = false
            option3.isClickable = false
            option4.isClickable = false
            btnNext.isEnabled = true
        }
    }

    private fun showWinner() {
        music?.stop()
        if (viewModel.onQuizEndReturnAnswersCount() != 0) updateDatabase()
        findNavController().navigate(
            QuizFragmentDirections.actionQuizFragmentToWinnerFragment2(
                viewModel.onQuizEndReturnAnswersCount()
            )
        )
    }

    private fun updateDatabase() {
        Timber.d("current date: ${getCurrentDate()}")
        Timber.d("coins on win: ${viewModel.onQuizEndReturnAnswersCount()}")
        Timber.d("isCurrentDayAvailable: ${database.isCurrentDayAvailable(getCurrentDate())}")
        Timber.d("update data -> date: ${getCurrentDate()} and coins: ${viewModel.onQuizEndReturnAnswersCount()}")
        when {
            database.isCurrentDayAvailable(getCurrentDate()) -> {
                Timber.d("database.isCurrentDayAvailable: true")
                database.updateCoinsByDate(
                    getCurrentDate(),
                    viewModel.onQuizEndReturnAnswersCount()
                )
            }
            else -> {
                Timber.d("database.isCurrentDayAvailable: true")
                database.addNewData(
                    Entity(
                        id = 0,
                        date = getCurrentDate(),
                        coins = viewModel.onQuizEndReturnAnswersCount(),
                        dailySpinCount = 5
                    )
                )
            }
        }
    }

    private fun createMusic() {
        if (MySharedPreferences.getBoolean(
                resources.getString(R.string.shp_music),
                true
            )
        ) {
            isMusicEnabled = true
            music = MediaPlayer.create(requireContext(), R.raw.music_countdown)
            music?.start()
        }
    }

    private fun createSound() {
        if (MySharedPreferences.getBoolean(resources.getString(R.string.shp_sound), true)) {
            isSoundEnabled = true
            val attributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            soundPool = SoundPool.Builder().setMaxStreams(2).setAudioAttributes(attributes).build()
            soundTrueAnswer = soundPool!!.load(requireContext(), R.raw.true_answer, 1)
            soundFalseAnswer = soundPool!!.load(requireContext(), R.raw.wrong_answer, 1)
        }
    }

    /*private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_popup, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.menu_restart -> {

                }
                R.id.menu_sound -> {

                }
                R.id.menu_music -> {

                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }*/

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
                        findNavController().popBackStack()
                        return
                    } else snackMessage(resources.getString(R.string.snack_exit_text))
                    onBackPressedTime = System.currentTimeMillis()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        music?.setVolume(1f, 1f)
    }

    override fun onPause() {
        super.onPause()
        music?.setVolume(0f, 0f)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        music?.stop()
        music = null
        soundPool?.stop(0)
        soundPool?.stop(1)
        soundPool = null
    }


}