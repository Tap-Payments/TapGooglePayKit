package company.tap.google.pay.internal.tapnetworkkit.connection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

//import company.tap.nativenetworkkit.BuildConfig;

import company.tap.google.pay.BuildConfig;
import company.tap.google.pay.internal.tapnetworkkit.exception.NoAuthTokenProvidedException;
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APILoggInterface;
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APIRequestInterface;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The type Retrofit helper.
 */
public final class RetrofitHelper {
    private static Retrofit retrofit;
    private static APIRequestInterface helper;
    private static APILoggInterface APILoggInterface;
    static OkHttpClient okHttpClient;
    private static final String TAG = "RetrofitHelper";
    private static Activity activityListeningForLoggerEvents = null;


    /**
     * Gets api helper.
     *
     * @return the api helper
     */
    public static APIRequestInterface getApiHelper(String baseUrl, Context context, Boolean debugMode, String packageId, @Nullable AppCompatActivity activity , @Nullable String authtoken) {
        if (retrofit == null) {
            if (NetworkApp.getAuthToken() == null) {
                throw new NoAuthTokenProvidedException();
            }

            if(authtoken.contains("pk")){
                okHttpClient = getOkHttpClientPK(context, debugMode, packageId);
            }else{

                okHttpClient = getOkHttpClient(context, debugMode, packageId);
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(buildGsonConverter())
                    .client(okHttpClient)
                    .build();
        }

        if (helper == null) {
            helper = retrofit.create(APIRequestInterface.class);
        }
        if (activity != null) {
            activityListeningForLoggerEvents = activity;
            APILoggInterface = (APILoggInterface) activityListeningForLoggerEvents;
        }
        return helper;
    }

    private static OkHttpClient getOkHttpClientWithHeader(Context context) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new NetworkConnectionInterceptor(context));
        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader(APIConstants.TOKEN_PREFIX, APIConstants.AUTH_TOKEN_PREFIX +NetworkApp.getHeaderToken())
                    //.addHeader(APIConstants.APPLICATION, NetworkApp.getApplicationInfo())
                    .addHeader(APIConstants.ACCEPT_KEY, APIConstants.ACCEPT_VALUE)
                    .addHeader(APIConstants.CONTENT_TYPE_KEY, APIConstants.CONTENT_TYPE_VALUE).build();
            return chain.proceed(request);
        });
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(!BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY));

        return httpClientBuilder.build();
    }


    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson myGson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        // custom deserializers not added
        return GsonConverterFactory.create(myGson);
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "body null";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }

    }

    private static OkHttpClient getOkHttpClient(Context context, Boolean debugMode, String packageId) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                   // .addHeader(APIConstants.TOKEN_PREFIX, APIConstants.AUTH_TOKEN_PREFIX + NetworkApp.getHeaderToken())
                    .addHeader(APIConstants.AUTH_TOKEN_KEY, APIConstants.AUTH_TOKEN_PREFIX + NetworkApp.getAuthToken())
                    .addHeader(APIConstants.PACKAGE_ID, NetworkApp.getPackageId())
                    .addHeader(APIConstants.SESSION_PREFIX, NetworkApp.getHeaderToken())
                    .addHeader(APIConstants.APPLICATION, NetworkApp.getApplicationInfo())
                    .addHeader(APIConstants.ACCEPT_KEY, APIConstants.ACCEPT_VALUE)
                    .addHeader(APIConstants.CONTENT_TYPE_KEY, APIConstants.CONTENT_TYPE_VALUE)
                    .addHeader(APIConstants.IP_ADDRESS, NetworkApp.getUserIpAddress())

                    .build();
            if (debugMode) {
                Log.e("dataRequestBody Request", String.valueOf(request.toString()));
                if (request.body() != null) {
                    Log.e("dataRequestBody body", bodyToString(request.body()));
                }

                Log.e("dataRequestBody Headers", String.valueOf(request.headers().toString()));
                Response response = chain.proceed(request);
                try {
                    Log.e("dataRequestBody", response.toString());
                } catch (Exception ex) {
                    return response;
                }
            }

            return chain.proceed(request);
        });
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

           /* if(debugMode|| NetworkApp.debugMode){
                httpClientBuilder.addInterceptor(getLogging(debugMode));

            }else{
                httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(!BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.NONE));

            }
*/
        return httpClientBuilder.build();
    }
    private static OkHttpClient getOkHttpClientPK(Context context, Boolean debugMode, String packageId) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    // .addHeader(APIConstants.TOKEN_PREFIX, APIConstants.AUTH_TOKEN_PREFIX + NetworkApp.getHeaderToken())
                    .addHeader(APIConstants.AUTH_TOKEN_KEY,   NetworkApp.getAuthToken())
                    .addHeader(APIConstants.PACKAGE_ID, NetworkApp.getPackageId())
                    .addHeader(APIConstants.SESSION_PREFIX, NetworkApp.getHeaderToken())
                    .addHeader(APIConstants.APPLICATION, NetworkApp.getApplicationInfo())
                    .addHeader(APIConstants.ACCEPT_KEY, APIConstants.ACCEPT_VALUE)
                    .addHeader(APIConstants.CONTENT_TYPE_KEY, APIConstants.CONTENT_TYPE_VALUE)
                    .addHeader(APIConstants.IP_ADDRESS, NetworkApp.getUserIpAddress())

                    .build();
            if (debugMode) {
                Log.e("dataRequestBody Request", String.valueOf(request.toString()));
                if (request.body() != null) {
                    Log.e("dataRequestBody body", bodyToString(request.body()));
                }

                Log.e("dataRequestBody Headers", String.valueOf(request.headers().toString()));
                Response response = chain.proceed(request);
                try {
                    Log.e("dataRequestBody", response.toString());
                } catch (Exception ex) {
                    return response;
                }
            }

            return chain.proceed(request);
        });
        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

           /* if(debugMode|| NetworkApp.debugMode){
                httpClientBuilder.addInterceptor(getLogging(debugMode));

            }else{
                httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(!BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.NONE : HttpLoggingInterceptor.Level.NONE));

            }
*/
        return httpClientBuilder.build();
    }

    public static HttpLoggingInterceptor getLogging(Boolean debugMode) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (message != null) {
                    Log.d(TAG, "OkHttp: " + message);
                    if (activityListeningForLoggerEvents != null) {
                        APILoggInterface.onLoggingEvent(message);
                    }
                }
            }
        });
        if (debugMode || NetworkApp.debugMode) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        return logging;
    }
}