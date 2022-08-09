package uz.gita.quizappMBF.extensions

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.gita.quizappMBF.R

fun View.onClick(action: (View) -> Unit) {
    setOnClickListener {
        if (it != null) action(it)
    }
}

fun Fragment.snackMessage(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
        setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_status_bar
            )
        )
    }.show()
}

/*
fun Fragment.getCurrentDate(): String {
    val date = Date()
    val simpleDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return simpleDateFormatter.format(date)
}*/

fun Fragment.getCurrentDate(): String {
    return java.sql.Date(System.currentTimeMillis()).toString()
}