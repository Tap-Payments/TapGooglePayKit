package company.tap.google.pay.internal.tapnetworkkit.utils;

import android.content.Context;
import android.net.ConnectivityManager;



public class NetworkUtils {

/**
 * Checks internet connectivity
 * */
    public static boolean isNetworkConnected(Context context) {
        if (context == null)
            return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null;


    }

}
