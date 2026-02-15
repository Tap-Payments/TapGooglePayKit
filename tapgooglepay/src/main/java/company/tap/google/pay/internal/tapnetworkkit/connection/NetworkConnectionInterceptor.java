package company.tap.google.pay.internal.tapnetworkkit.connection;

import android.content.Context;


import java.io.IOException;


import company.tap.google.pay.internal.tapnetworkkit.exception.NoInternetConnectionException;
import company.tap.google.pay.internal.tapnetworkkit.utils.NetworkUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {

    private Context context;

    NetworkConnectionInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetworkUtils.isNetworkConnected(context))
            return chain.proceed(request);
        else
            throw new NoInternetConnectionException(context);
    }
}