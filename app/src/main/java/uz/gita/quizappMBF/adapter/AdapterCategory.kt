package uz.gita.quizappMBF.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.databinding.ItemCategoriesNewBinding
import uz.gita.quizappMBF.extensions.onClick
import uz.gita.quizappMBF.listeners.OnCategorySelectedListener
import uz.gita.quizappMBF.model.DataCategory


class AdapterCategory(var listener: OnCategorySelectedListener) :
    ListAdapter<DataCategory, AdapterCategory.ViewHolder>(CustomDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categories_new, parent, false)
        val value = parent.measuredWidth / 1
        view.minimumWidth = value
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_category)
        holder.itemView.startAnimation(animation)
        holder.bind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoriesNewBinding.bind(view)
        fun bind(position: Int) {
            binding.imgQuiz.setImageResource(getItem(position).icon)
            binding.category.text = getItem(position).title
            binding.categoryDescription.text = getItem(position).description
            itemView.onClick {
                listener.onClick(position, getItem(position).title)
            }
        }
    }

    private class CustomDiffUtil() : DiffUtil.ItemCallback<DataCategory>() {
        override fun areItemsTheSame(oldItem: DataCategory, newItem: DataCategory): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: DataCategory, newItem: DataCategory): Boolean {
            return oldItem == newItem
        }
    }
}