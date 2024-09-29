package org.token.solana.utils
import android.content.Context
import android.content.SharedPreferences


class DataManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("Data", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putBoolean(key: String?, `val`: Boolean) {
        editor.putBoolean(key, `val`)
        editor.apply()
    }

    fun getBoolean(key: String?): Boolean {
        return preferences.getBoolean(key, false)
    }

    //
    fun putString(key: String?, `val`: String?) {
        editor.putString(key, `val`)
        editor.apply()
    }

    fun getString(key: String?): String? {
        return preferences.getString(key, null)
    }

    //
    fun putInteger(key: String?, `val`: Int) {
        editor.putInt(key, `val`)
        editor.apply()
    }

    fun getInteger(key: String?): Int {
        return preferences.getInt(key, 0)
    }

    fun putFloat(key: String?, `val`: Float) {
        editor.putFloat(key, `val`)
        editor.apply()
    }

    fun getFloat(key: String?): Float {
        return preferences.getFloat(key, 0f)
    }

    fun clear() {
        editor.clear()
    }
}