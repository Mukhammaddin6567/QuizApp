package uz.gita.quizappMBF.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import timber.log.Timber
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.Entity
import uz.gita.quizappMBF.databinding.DialogAboutBinding
import uz.gita.quizappMBF.databinding.FragmentMenuBinding
import uz.gita.quizappMBF.extensions.getCurrentDate
import uz.gita.quizappMBF.extensions.onClick
import uz.gita.quizappMBF.extensions.snackMessage

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private var onBackPressedTime: Long = 0
    private val database by lazy { AppDatabase.getDatabase().databaseDao() }
    private var currentCoins: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        onBackPressed()
        currentCoins = database.getTotalSum()
        val currentDate = getCurrentDate()
        if (!database.isCurrentDayAvailable(currentDate)) database.addNewData(
            Entity(
                id = 0,
                date = currentDate,
                0,
                dailySpinCount = 5
            )
        )
        Timber.d("database.getTotalSum(): ${database.getTotalSum()}")

        Timber.d(
            "database.isCurrentDayAvailable: ${database.isCurrentDayAvailable(getCurrentDate())}"
        )

        Timber.d("database.getAllDataBySortingDate(): ${database.getAllDataBySortingDate()}")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnQuizzes.onClick {
                findNavController().navigate(R.id.action_menuFragment_to_categoriesFragment)
            }
            btnSpinner.onClick {
                findNavController().navigate(R.id.action_menuFragment_to_spinnerFragment)
            }
            btnStatistics.onClick {
                findNavController().navigate(R.id.action_menuFragment_to_statisticsFragment)
            }
            btnSettings.onClick {
                findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
            }
            btnAbout.onClick {
                showDialog()
            }
            btnQuit.onClick {
                activity?.finish()
            }
            textCurrentCoins.text =
                resources.getString(R.string.text_current_coins, currentCoins.toString())
        }
    }

    private fun showDialog() {
        val currentBinding = DialogAboutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setView(currentBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        currentBinding.buttonClose.onClick {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
                        activity?.finish()
                        return
                    } else snackMessage(resources.getString(R.string.snack_exit_text))
                    onBackPressedTime = System.currentTimeMillis()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}