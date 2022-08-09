package uz.gita.quizappMBF.repository

import android.content.res.Resources
import uz.gita.quizappMBF.R
import uz.gita.quizappMBF.model.DataCategory
import uz.gita.quizappMBF.model.DataQuestion

class Repository() {
    companion object {
        private var instance: Repository? = null
        fun getRepository(): Repository {
            if (instance == null) instance = Repository()
            return instance!!
        }
    }

    fun getDataList(resources: Resources): MutableList<DataCategory> {
        val list = mutableListOf<DataCategory>()

        list.add(
            DataCategory(
                id = 1,
                R.drawable.img_science,
                resources.getString(R.string.text_category_science),
                resources.getString(R.string.text_category_science_description)
            )
        )

        list.add(
            DataCategory(
                id = 2,
                R.drawable.img_geography,
                resources.getString(R.string.text_category_geography),
                resources.getString(R.string.text_category_geography_description)
            )
        )

        list.add(
            DataCategory(
                id = 3,
                R.drawable.img_sport,
                resources.getString(R.string.text_category_sport),
                resources.getString(R.string.text_category_sport_description),
            )
        )

        list.add(
            DataCategory(
                id = 4,
                R.drawable.img_movie,
                resources.getString(R.string.text_category_movie),
                resources.getString(R.string.text_category_movie_description)
            )
        )

        return list
    }

    fun getScienceQuestionsList(resources: Resources): List<DataQuestion> {
        val list = mutableListOf<DataQuestion>()
        val questions = resources.getStringArray(R.array.questions_science).toList()
        val answers = resources.getStringArray(R.array.questions_science_answers).toList()
        val options1 = resources.getStringArray(R.array.question_science_option1).toList()
        val options2 = resources.getStringArray(R.array.question_science_option2).toList()
        val options3 = resources.getStringArray(R.array.question_science_option3).toList()
        val options4 = resources.getStringArray(R.array.question_science_option4).toList()
        for (i in questions.indices) {
            list.add(
                DataQuestion(
                    id = i + 1,
                    question = questions[i].toString(),
                    correctAnswer = answers[i].toString(),
                    option1 = options1[i].toString(),
                    option2 = options2[i].toString(),
                    option3 = options3[i].toString(),
                    option4 = options4[i].toString()
                )
            )
        }
        list.shuffle()
        return list
    }

    fun getGeographyQuestionsList(resources: Resources): List<DataQuestion> {
        val list = mutableListOf<DataQuestion>()
        val questions = resources.getStringArray(R.array.questions_geography).toList()
        val answers = resources.getStringArray(R.array.questions_geography_answers).toList()
        val options1 = resources.getStringArray(R.array.question_geography_option1).toList()
        val options2 = resources.getStringArray(R.array.question_geography_option2).toList()
        val options3 = resources.getStringArray(R.array.question_geography_option3).toList()
        val options4 = resources.getStringArray(R.array.question_geography_option4).toList()
        for (i in questions.indices) {
            list.add(
                DataQuestion(
                    id = i + 1,
                    question = questions[i].toString(),
                    correctAnswer = answers[i].toString(),
                    option1 = options1[i].toString(),
                    option2 = options2[i].toString(),
                    option3 = options3[i].toString(),
                    option4 = options4[i].toString()
                )
            )
        }
        list.shuffle()
        return list
    }

    fun getSportQuestionsList(resources: Resources): List<DataQuestion> {
        val list = mutableListOf<DataQuestion>()
        val questions = resources.getStringArray(R.array.questions_sport).toList()
        val answers = resources.getStringArray(R.array.questions_sport_answers).toList()
        val options1 = resources.getStringArray(R.array.question_sport_option1).toList()
        val options2 = resources.getStringArray(R.array.question_sport_option2).toList()
        val options3 = resources.getStringArray(R.array.question_sport_option3).toList()
        val options4 = resources.getStringArray(R.array.question_sport_option4).toList()
        for (i in questions.indices) {
            list.add(
                DataQuestion(
                    id = i + 1,
                    question = questions[i].toString(),
                    correctAnswer = answers[i].toString(),
                    option1 = options1[i].toString(),
                    option2 = options2[i].toString(),
                    option3 = options3[i].toString(),
                    option4 = options4[i].toString()
                )
            )
        }
        list.shuffle()
        return list
    }

    fun getMovieQuestionsList(resources: Resources): List<DataQuestion> {
        val list = mutableListOf<DataQuestion>()
        val questions = resources.getStringArray(R.array.questions_movie).toList()
        val answers = resources.getStringArray(R.array.questions_movie_answers).toList()
        val options1 = resources.getStringArray(R.array.question_movie_option1).toList()
        val options2 = resources.getStringArray(R.array.question_movie_option2).toList()
        val options3 = resources.getStringArray(R.array.question_movie_option3).toList()
        val options4 = resources.getStringArray(R.array.question_movie_option4).toList()
        for (i in questions.indices) {
            list.add(
                DataQuestion(
                    id = i + 1,
                    question = questions[i].toString(),
                    correctAnswer = answers[i].toString(),
                    option1 = options1[i].toString(),
                    option2 = options2[i].toString(),
                    option3 = options3[i].toString(),
                    option4 = options4[i].toString()
                )
            )
        }
        list.shuffle()
        return list
    }
}