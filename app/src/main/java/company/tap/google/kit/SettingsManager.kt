package company.tap.google.kit

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.GooglePayButtonType
import company.tap.google.pay.open.enums.SDKMode

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

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @SuppressLint("StaticFieldLeak")
    fun getSet(key: String): MutableSet<String>? {
        return pref?.getStringSet("key_payment_networks", null)
      //  return pref?.getString(key, defaultValue)
    }

    fun getAllowedMethods(key: String): AllowedMethods {
        val trx_mode = pref?.getString(key, AllowedMethods.ALL.name)
        println("trx_mode are" + trx_mode)
        if (trx_mode.equals(
                AllowedMethods.ALL.name,
                ignoreCase = true
            )
        ) return AllowedMethods.ALL
        if (trx_mode.equals(
                AllowedMethods.CRYPTOGRAM_3DS.name,
                ignoreCase = true
            )
        ) return AllowedMethods.CRYPTOGRAM_3DS
        return if (trx_mode.equals(
                AllowedMethods.PAN_ONLY.name,
                ignoreCase = true
            )
        ) return AllowedMethods.PAN_ONLY
        else AllowedMethods.ALL
    }

    fun getInt(key: String?, defaultValue: Int): Int? {
        return pref?.getInt(key, defaultValue)
    }
    fun getSDKMode(key: String): SDKMode {
        val trx_mode = pref?.getString(key, SDKMode.ENVIRONMENT_TEST.name)
        if (trx_mode.equals(
                SDKMode.ENVIRONMENT_TEST.name,
                ignoreCase = true
            )
        ) return  SDKMode.ENVIRONMENT_TEST
        return if (trx_mode.equals(
                SDKMode.ENVIRONMENT_PRODUCTION.name,
                ignoreCase = true
            )
        ) return SDKMode.ENVIRONMENT_PRODUCTION
        else SDKMode.ENVIRONMENT_TEST
    }
    fun getGPAYButtonType(key: String): GooglePayButtonType {
        val trx_mode = pref?.getString(key, GooglePayButtonType.NORMAL_GOOGLE_PAY.name)
        if (trx_mode.equals(
                GooglePayButtonType.NORMAL_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.NORMAL_GOOGLE_PAY
         if (trx_mode.equals(
                GooglePayButtonType.BUY_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.BUY_WITH_GOOGLE_PAY
         if (trx_mode.equals(
                GooglePayButtonType.DONATE_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.DONATE_WITH_GOOGLE_PAY
        if (trx_mode.equals(
                GooglePayButtonType.SUBSCRIBE_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.SUBSCRIBE_WITH_GOOGLE_PAY
       return if (trx_mode.equals(
                GooglePayButtonType.PAY_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.PAY_WITH_GOOGLE_PAY
        else GooglePayButtonType.NORMAL_GOOGLE_PAY
    }
}