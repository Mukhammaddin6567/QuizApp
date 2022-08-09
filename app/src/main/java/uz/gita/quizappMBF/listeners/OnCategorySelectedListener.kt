package uz.gita.quizappMBF.listeners

@FunctionalInterface
interface OnCategorySelectedListener {
    fun onClick(id: Int, title: String)
}