package company.tap.google.kit

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

@SuppressLint("StaticFieldLeak")
object SettingsManager {
    private var pref: SharedPreferences? = null
    private var context: Context? = null
    fun setPref(ctx: Context?) {
        context = ctx
        if (pref == null) pref = PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    /**
     *
     * @param key
     * @return
     */
    fun getBoolean(key: String?, defaultValue: Boolean): Boolean? {
        return pref?.getBoolean(key, defaultValue)
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @SuppressLint("StaticFieldLeak")
    fun getString(key: String?, defaultValue: String?): String? {
        return pref?.getString(key, defaultValue)
    }

    fun getInt(key: String?, defaultValue: Int): Int? {
        return pref?.getInt(key, defaultValue)
    }
}