package uz.gita.quizappMBF.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import timber.log.Timber
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.MySharedPreferences
import uz.gita.quizappMBF.databinding.FragmentWinnerBinding
import uz.gita.quizappMBF.extensions.onClick
import java.util.concurrent.TimeUnit

class WinnerFragment : Fragment(R.layout.fragment_winner) {


    private var _binding: FragmentWinnerBinding? = null
    private val binding get() = _binding!!
    private val args: WinnerFragmentArgs by navArgs()
    private var music: MediaPlayer? = null
    private var isSoundEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWinnerBinding.inflate(inflater, container, false)
        Timber.d(java.sql.Date(System.currentTimeMillis()).toString())
        createSound()
        onBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            if (args.coins > 0) {
                iconWinner.setImageResource(R.drawable.ic_winner)
                title.text = resources.getString(R.string.text_congrats)
                when (args.coins) {
                    1 -> textEarnedCoins.text =
                        resources.getString(
                            R.string.text_earned_coins_single,
                            args.coins.toString()
                        )
                    else -> textEarnedCoins.text =
                        resources.getString(R.string.text_earned_coins, args.coins.toString())
                }
                if (isSoundEnabled) music?.start()
                makeAnimation()
            } else {
                iconWinner.setImageResource(R.drawable.ic_you_can_do_it)
                title.text = resources.getString(R.string.text_do_not_worry)
                textEarnedCoins.text =
                    resources.getString(R.string.text_try_one_more_time)
            }

            btnHome.onClick {
                findNavController().navigate(R.id.action_winnerFragment2_to_menuFragment)
            }
            btnRestart.onClick {
                findNavController().navigate(R.id.action_winnerFragment2_to_categoriesFragment)
            }
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
            music = MediaPlayer.create(requireContext(), R.raw.winner)
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_winnerFragment2_to_menuFragment)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        music?.start()
    }

    override fun onPause() {
        super.onPause()
        music?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        music?.stop()
        music = null
        _binding = null
    }
}