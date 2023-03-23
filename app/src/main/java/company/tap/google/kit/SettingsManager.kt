package company.tap.google.kit

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import company.tap.google.pay.open.enums.Authentication
import company.tap.google.pay.open.enums.GooglePayButtonType
import company.tap.google.pay.open.enums.GooglePayEnviroment

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

    fun getAllowedMethods(key: String): Authentication {
        val trx_mode = pref?.getString(key, Authentication.ALL.name)
        println("trx_mode are" + trx_mode)
        if (trx_mode.equals(
                Authentication.ALL.name,
                ignoreCase = true
            )
        ) return Authentication.ALL
        if (trx_mode.equals(
                Authentication.CRYPTOGRAM_3DS.name,
                ignoreCase = true
            )
        ) return Authentication.CRYPTOGRAM_3DS
        return if (trx_mode.equals(
                Authentication.PAN_ONLY.name,
                ignoreCase = true
            )
        ) return Authentication.PAN_ONLY
        else Authentication.ALL
    }

    fun getInt(key: String?, defaultValue: Int): Int? {
        return pref?.getInt(key, defaultValue)
    }
    fun getSDKMode(key: String): GooglePayEnviroment {
        val trx_mode = pref?.getString(key, GooglePayEnviroment.ENVIRONMENT_TEST.name)
        if (trx_mode.equals(
                GooglePayEnviroment.ENVIRONMENT_TEST.name,
                ignoreCase = true
            )
        ) return  GooglePayEnviroment.ENVIRONMENT_TEST
        return if (trx_mode.equals(
                GooglePayEnviroment.ENVIRONMENT_PRODUCTION.name,
                ignoreCase = true
            )
        ) return GooglePayEnviroment.ENVIRONMENT_PRODUCTION
        else GooglePayEnviroment.ENVIRONMENT_TEST
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
        if (trx_mode.equals(
                GooglePayButtonType.CHECKOUT_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.CHECKOUT_WITH_GOOGLE_PAY
        if (trx_mode.equals(
                GooglePayButtonType.BOOK_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.BOOK_WITH_GOOGLE_PAY
        if (trx_mode.equals(
                GooglePayButtonType.ORDER_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.ORDER_WITH_GOOGLE_PAY
       return if (trx_mode.equals(
                GooglePayButtonType.PAY_WITH_GOOGLE_PAY.name,
                ignoreCase = true
            )
        ) return  GooglePayButtonType.PAY_WITH_GOOGLE_PAY
        else GooglePayButtonType.NORMAL_GOOGLE_PAY
    }
}