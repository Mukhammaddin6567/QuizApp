package uz.gita.quizappMBF.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.adapter.AdapterCategory
import uz.gita.quizappMBF.databinding.FragmentCategoriesBinding
import uz.gita.quizappMBF.extensions.onClick
import uz.gita.quizappMBF.listeners.OnCategorySelectedListener
import uz.gita.quizappMBF.repository.Repository
import uz.gita.quizappMBF.utils.DotsIndicatorDecoration


class CategoriesFragment : Fragment(R.layout.fragment_categories), OnCategorySelectedListener {

    private val repository: Repository = Repository.getRepository()
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private val adapter: AdapterCategory by lazy { AdapterCategory(this) }

    companion object {
        private const val TAG = "CategoriesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        onBackPressed()
        adapter.submitList(repository.getDataList(resources))
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnBack.onClick {
                findNavController().popBackStack()
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(true)
            val radius = resources.getDimension(R.dimen.radius).toInt()
            val dotsHeight = resources.getDimension(R.dimen.dots_height).toInt()
            val color = ContextCompat.getColor(requireContext(), R.color.color_white)
            recyclerView.addItemDecoration(
                DotsIndicatorDecoration(
                    radius,
                    radius * 4,
                    dotsHeight,
                    color,
                    color
                )
            )
            PagerSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_categoriesFragment_to_menuFragment)
                }
            })
    }

    override fun onClick(id: Int, title: String) {
        Timber.d(TAG, "$id and $title")
        findNavController().navigate(
            CategoriesFragmentDirections.actionCategoriesFragmentToQuizFragment(
                id,
                title
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}