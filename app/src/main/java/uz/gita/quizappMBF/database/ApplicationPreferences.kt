package uz.gita.quizappMBF.database

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun initPreferences(context: Context) {
        if (sharedPreferences == null) sharedPreferences =
            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
    }

    fun putStringSet(key: String?, value: MutableSet<String>?) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putStringSet(key, value)
        preferenceEditor.apply()
    }

    fun getStringSet(key: String?, defaultValue: MutableSet<String>?): MutableSet<String>? {
        return sharedPreferences!!.getStringSet(key, defaultValue)
    }

    fun putString(key: String?, value: String?) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putString(key, value)
        preferenceEditor.apply()
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return sharedPreferences!!.getString(key, defaultValue)
    }

    fun putInt(key: String?, value: Int) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putInt(key, value)
        preferenceEditor.apply()
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return sharedPreferences!!.getInt(key, defaultValue)
    }

    fun putLong(key: String?, value: Long) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putLong(key, value)
        preferenceEditor.apply()
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return sharedPreferences!!.getLong(key, defaultValue)
    }

    fun putBoolean(key: String?, value: Boolean) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putBoolean(key, value)
        preferenceEditor.apply()
    }

    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun putFloat(key: String?, value: Float) {
        val preferenceEditor = sharedPreferences!!.edit()
        preferenceEditor.putFloat(key, value)
        preferenceEditor.apply()
    }

    fun getFloat(key: String?, defaultValue: Float): Float {
        return sharedPreferences!!.getFloat(key, defaultValue)
    }
}