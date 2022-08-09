package uz.gita.quizappMBF.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.adapter.AdapterStatistics
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.Entity
import uz.gita.quizappMBF.databinding.FragmentStatisticsBinding
import uz.gita.quizappMBF.extensions.onClick
import kotlin.random.Random


class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { AdapterStatistics() }
    private val database by lazy { AppDatabase.getDatabase().databaseDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        adapter.submitList(database.getAllDataBySortingDate())
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            btnBack.onClick {
                findNavController().popBackStack()
            }
        }
        return binding.root
    }

    private fun fakeData(): List<Entity> {
        val list = mutableListOf<Entity>()
        for (value in 30 downTo 1) {
            if (value <= 9) list.add(
                Entity(
                    id = value,
                    date = "2022-03-0$value",
                    Random.nextInt(1, 100),
                    dailySpinCount = 5
                )
            ) else list.add(
                Entity(
                    id = value,
                    date = "2022-03-$value",
                    Random.nextInt(1, 100),
                    dailySpinCount = 5
                )
            )
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}