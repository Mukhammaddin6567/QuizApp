package uz.gita.quizappMBF.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.MySharedPreferences
import uz.gita.quizappMBF.databinding.DialogClearRecordsBinding
import uz.gita.quizappMBF.databinding.FragmentSettingsBinding
import uz.gita.quizappMBF.extensions.onClick
import uz.gita.quizappMBF.extensions.snackMessage

class SettingsFragment : Fragment(R.layout.fragment_settings) {


    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val database by lazy { AppDatabase.getDatabase().databaseDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnBack.onClick {
                findNavController().popBackStack()
            }

            checkboxSound.isChecked =
                MySharedPreferences.getBoolean(resources.getString(R.string.shp_sound), true)
            checkboxSound.setOnCheckedChangeListener { _, isChecked ->
                MySharedPreferences.putBoolean(resources.getString(R.string.shp_sound), isChecked)
            }

            checkboxMusic.isChecked =
                MySharedPreferences.getBoolean(resources.getString(R.string.shp_music), true)
            checkboxMusic.setOnCheckedChangeListener { _, isChecked ->
                MySharedPreferences.putBoolean(resources.getString(R.string.shp_music), isChecked)
            }

            checkboxClear.setOnCheckedChangeListener { _, _ ->
                if (database.getTotalSum() == 0) {
                    snackMessage(getString(R.string.text_no_records))
                } else {
                    showDialog()
                }
                checkboxClear.isChecked = false
            }
        }
    }

    private fun showDialog() {
        val currentBinding = DialogClearRecordsBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.setView(currentBinding.root)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        with(currentBinding) {
            buttonClearRecordNo.onClick {
                dialog.dismiss()
            }
            buttonClearRecordYes.onClick {
                database.clearData()
                snackMessage(getString(R.string.text_clear_record_success))
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}