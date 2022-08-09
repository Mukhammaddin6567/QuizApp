package uz.gita.quizappMBF

import android.app.Application
import timber.log.Timber
import uz.gita.quizappMBF.database.AppDatabase
import uz.gita.quizappMBF.database.MySharedPreferences

class App : Application() {
    override fun onCreate() {

        super.onCreate()
        // Timber
        val checkDebug = true
        if (checkDebug) Timber.plant(Timber.DebugTree())
        Timber.d("onCreate()")

        // SharedPreferences
        MySharedPreferences.initPreferences(this)

        // Room Database
        AppDatabase.init(this)
    }
}