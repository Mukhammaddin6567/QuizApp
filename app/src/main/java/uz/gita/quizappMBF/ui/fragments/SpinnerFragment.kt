package uz.gita.quizappMBF.ui.fragments

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.Entity
import uz.gita.quizappMBF.database.MySharedPreferences
import uz.gita.quizappMBF.databinding.FragmentSpinnerBinding
import uz.gita.quizappMBF.extensions.getCurrentDate
import uz.gita.quizappMBF.extensions.snackMessage
import uz.gita.quizappMBF.spinWheel.model.LuckyItem
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class SpinnerFragment : Fragment(R.layout.fragment_spinner) {


    private var _binding: FragmentSpinnerBinding? = null
    private val binding get() = _binding!!
    private val database by lazy { AppDatabase.getDatabase().databaseDao() }
    private var soundWin: MediaPlayer? = null
    private var soundSpin: MediaPlayer? = null

    private var isSoundEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpinnerBinding.inflate(inflater, container, false)
        createSound()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = ArrayList<LuckyItem>()

        val item1 = LuckyItem()
        item1.topText = "5"
        item1.secondaryText = resources.getString(R.string.text_coins)
        item1.color = Color.parseColor("#FFFFFF")
        item1.textColor = Color.parseColor("#000000")

        val item2 = LuckyItem()
        item2.topText = "10"
        item2.secondaryText = resources.getString(R.string.text_coins)
        item2.color = Color.parseColor("#00cf00")
        item2.textColor = Color.parseColor("#000000")

        val item3 = LuckyItem()
        item3.topText = "15"
        item3.secondaryText = resources.getString(R.string.text_coins)
        item3.color = Color.parseColor("#FFFFFF")
        item3.textColor = Color.parseColor("#000000")

        val item4 = LuckyItem()
        item4.topText = "20"
        item4.secondaryText = resources.getString(R.string.text_coins)
        item4.color = Color.parseColor("#304FFE")
        item4.textColor = Color.parseColor("#000000")

        val item5 = LuckyItem()
        item5.topText = "25"
        item5.secondaryText = resources.getString(R.string.text_coins)
        item5.color = Color.parseColor("#FFFFFF")
        item5.textColor = Color.parseColor("#000000")

        val item6 = LuckyItem()
        item6.topText = "30"
        item6.secondaryText = resources.getString(R.string.text_coins)
        item6.color = Color.parseColor("#dc0000")
        item6.textColor = Color.parseColor("#000000")

        val item7 = LuckyItem()
        item7.topText = "35"
        item7.secondaryText = resources.getString(R.string.text_coins)
        item7.color = Color.parseColor("#FFFFFF")
        item7.textColor = Color.parseColor("#000000")

        val item8 = LuckyItem()
        item8.topText = "0"
        item8.secondaryText = resources.getString(R.string.text_coins)
        item8.color = Color.parseColor("#000000")
        item8.textColor = Color.parseColor("#FFFFFF")

        data.add(item1)
        data.add(item2)
        data.add(item3)
        data.add(item4)
        data.add(item5)
        data.add(item6)
        data.add(item7)
        data.add(item8)
        binding.wheelView.setData(data)
        binding.wheelView.setRound(5)

        binding.spinButton.setOnClickListener {
            val currentDate = getCurrentDate()
            if (database.getCurrentSpinCount(currentDate) == 0) {
                snackMessage(getString(R.string.text_limit_is_over))
                return@setOnClickListener
            } else {
                database.updateSpinCountByDate(currentDate)
                snackMessage(
                    resources.getString(
                        R.string.text_daily_spin_counts,
                        database.getCurrentSpinCount(currentDate).toString()
                    )
                )
            }
            soundSpin?.start()
            YoYo.with(Techniques.BounceIn).duration(600).playOn(binding.imgArrow)
            YoYo.with(Techniques.BounceIn).duration(600).playOn(it)
            val randomNumber = Random.nextInt(data.size)
            binding.wheelView.startLuckyWheelWithTargetIndex(randomNumber)
            it.isEnabled = false
        }

        binding.wheelView.setLuckyRoundItemSelectedListener {
            soundSpin?.pause()
            soundSpin?.seekTo(0)
            binding.spinButton.isEnabled = true
            when (data[it].topText) {
                "0" -> snackMessage(resources.getString(R.string.text_try_one_more_time))
                else -> {
                    if (isSoundEnabled) soundWin?.start()
                    makeAnimation()
                    snackMessage(resources.getString(R.string.text_won_coins, data[it].topText))
                    val currentDate = getCurrentDate()
                    when {
                        database.isCurrentDayAvailable(currentDate) -> {
                            database.updateCoinsByDate(currentDate, data[it].topText.toInt())
                        }
                        else -> {
                            database.addNewData(
                                Entity(
                                    id = 0,
                                    date = currentDate,
                                    coins = data[it].topText.toInt(),
                                    dailySpinCount = 5
                                )
                            )
                        }
                    }
                }
            }
//            findNavController().popBackStack()
        }
    }

    private fun makeAnimation() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200),
            position = Position.Relative(0.5, 0.3)
        )
        binding.animation.start(party)
    }

    private fun createSound() {
        if (MySharedPreferences.getBoolean(resources.getString(R.string.shp_sound), true)) {
            isSoundEnabled = true
            soundWin = MediaPlayer.create(requireContext(), R.raw.winner)
            soundSpin = MediaPlayer.create(requireContext(), R.raw.spin_wheel)
        }
    }

    override fun onResume() {
        super.onResume()
        soundSpin?.setVolume(1f, 1f)
        soundWin?.setVolume(1f, 1f)
    }

    override fun onPause() {
        super.onPause()
        soundSpin?.setVolume(0f, 0f)
        soundWin?.setVolume(0f, 0f)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundSpin?.stop()
        soundWin?.stop()
        soundSpin = null
        soundWin = null
        _binding = null
    }
}