package uz.gita.quizappMBF.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.database.Entity
import uz.gita.quizappMBF.databinding.ItemStatisticsBinding

class AdapterStatistics : ListAdapter<Entity, AdapterStatistics.ViewHolder>(CustomDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (
                ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_statistics, parent, false)
                ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_in_left)
        holder.itemView.startAnimation(animation)
        holder.bind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemStatisticsBinding.bind(view)
        fun bind(position: Int) {
            binding.textDay.text = getItem(position).date
            binding.textCoins.text = getItem(position).coins.toString()
        }
    }

    private class CustomDiffUtil() : DiffUtil.ItemCallback<Entity>() {
        override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean {
            return oldItem == newItem
        }

    }
}