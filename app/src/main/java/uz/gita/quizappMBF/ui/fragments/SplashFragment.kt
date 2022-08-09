package uz.gita.quizappMBF.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import timber.log.Timber
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.databinding.FragmentSplashBinding


class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val handler by lazy { Handler(Looper.myLooper()!!) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView()")
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        Timber.d("onResume()")
        super.onResume()
        handler.postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_menuFragment)
        }, 3000L)
    }

    override fun onPause() {
        Timber.d("onPause()")
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
        _binding = null
    }
}