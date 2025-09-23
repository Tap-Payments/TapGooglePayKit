package company.tap.google.pay.internal.tapnetworkkit.connection;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

import company.tap.google.pay.internal.tapnetworkkit.controller.NetworkController;
import company.tap.google.pay.internal.tapnetworkkit.utils.CryptoUtil;
import okhttp3.logging.HttpLoggingInterceptor;
import  company.tap.google.pay.internal.tapnetworkkit.logger.lo;


/**
 * The type App info.
 */
public class NetworkApp {
    //auth information for headers
    private static String authToken;
    private static String headerToken;
    private static String packageId;
    private static String encryptionKey;
    private static LinkedHashMap<Object, Object> applicationInfo;
    private static String localeString = "en";
    private static TelephonyManager manager;
    private static String deviceName;
    static Boolean debugMode;

    private static String userIpAddress="";

    /**
     * Sets auth token.
     *
     * @param context   the context
     * @param authToken the auth token
     */
    public static void initNetwork(Context context, String authToken, String appId, String baseUrl, @Nullable String sdkIdentifier, Boolean debugMode, @Nullable String _encryptionKey,@Nullable AppCompatActivity activity) {
        NetworkApp.authToken = authToken;
        NetworkApp.packageId = appId;

        encryptionKey = _encryptionKey;
        if (manager != null)
            manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        deviceName = Settings.Global.getString(context.getContentResolver(), Settings.Global.DEVICE_NAME);
        NetworkApp.debugMode = debugMode;

        initApplicationInfo(appId, sdkIdentifier, encryptionKey);
        NetworkController.getInstance().setBaseUrl(baseUrl, context, debugMode, appId, activity, authToken);
        lo.init(context);
    }

    /**
     * Sets header token.
     *
     * @param _headerToken the headertoken
     */

    public static void initNetworkToken(String _headerToken, Context context, String baseUrl, Boolean debugMode,@Nullable AppCompatActivity activity) {
        NetworkApp.headerToken = _headerToken;
        RetrofitHelper.getApiHelper(baseUrl, context, debugMode, packageId, activity,authToken);
        lo.init(context);
    }

    /**
     * Gets header token.
     *
     * @return the _header token
     */
    static String getHeaderToken() {
        if (headerToken == null) return "";
        else return headerToken;
    }

    /**
     * Gets PackageID .
     *
     * @return the _packageID
     */
    static String getPackageId() {
        return CryptoUtil.encryptJsonString(packageId, encryptionKey);
    }

    /**
     * Gets auth token.
     *
     * @return the auth token
     */
    static String getAuthToken() {
        return authToken;
    }

    /**
     * Sets locale.
     *
     * @param locale the locale
     */
    public static void setLocale(String locale) {
        NetworkApp.localeString = locale.length() < 2 ? locale : locale.substring(0, 2);
        NetworkApp.applicationInfo.put("al", SupportedLocales.findByString(localeString).language);
    }

    private static void initApplicationInfo(String applicationId, String sdkIdentifier, String encryptionKey) {
        applicationInfo = new LinkedHashMap<>();

        applicationInfo.put("cu", CryptoUtil.encryptJsonString(applicationId, encryptionKey));
        applicationInfo.put("aid", CryptoUtil.encryptJsonString(sdkIdentifier, encryptionKey));
        applicationInfo.put("at", CryptoUtil.encryptJsonString("app", encryptionKey));
        applicationInfo.put("av", CryptoUtil.encryptJsonString(String.valueOf(Build.VERSION.SDK_INT), encryptionKey));
        applicationInfo.put("rn", CryptoUtil.encryptJsonString(deviceName, encryptionKey));
        applicationInfo.put("rt", CryptoUtil.encryptJsonString(Build.MANUFACTURER, encryptionKey));
        applicationInfo.put("rb", CryptoUtil.encryptJsonString(Build.BRAND, encryptionKey));
        applicationInfo.put("rm", CryptoUtil.encryptJsonString(Build.MODEL, encryptionKey));
        applicationInfo.put("ro", CryptoUtil.encryptJsonString("Android", encryptionKey));
        applicationInfo.put("rov", CryptoUtil.encryptJsonString(Build.VERSION.RELEASE, encryptionKey));

        applicationInfo.put("al", CryptoUtil.encryptJsonString(SupportedLocales.findByString(localeString).language, encryptionKey));

        if (manager != null) {
            applicationInfo.put("rsn", manager.getSimOperatorName());
            applicationInfo.put("rsc", manager.getSimCountryIso());
        }

    }

    /**
     * Gets locale string.
     *
     * @return the locale string
     */
    public static String getLocaleString() {
        return localeString;
    }

    /**
     * Gets application info.
     *
     * @return the application info
     */
    public static String getApplicationInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : applicationInfo.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("|");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }


    public static HttpLoggingInterceptor getIntercepters(Boolean debugMode) {
        return RetrofitHelper.getLogging(debugMode);
    }

    public static String getUserIpAddress() {
        return userIpAddress;
    }

    public static void setUserIpAddress(String userIpAddress) {
        NetworkApp.userIpAddress = userIpAddress;
    }

    private enum SupportedLocales {
        /**
         * En supported locales.
         */
        EN("en"), AR("ar");

        private final String language;

        SupportedLocales(String language) {
            this.language = language;
        }

        /**
         * Find by string supported locales.
         *
         * @param localeString the locale string
         * @return the supported locales
         */
        static SupportedLocales findByString(String localeString) {
            for (SupportedLocales locale : values()) {
                if (locale.language.equalsIgnoreCase(localeString)) {
                    return locale;
                }
            }

            return EN;
        }

    }


}
 