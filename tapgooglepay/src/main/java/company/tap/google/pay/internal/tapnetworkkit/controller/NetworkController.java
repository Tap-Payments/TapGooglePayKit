package company.tap.google.pay.internal.tapnetworkkit.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import company.tap.google.pay.internal.tapnetworkkit.connection.RetrofitHelper;
import company.tap.google.pay.internal.tapnetworkkit.enums.TapMethodType;
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APIRequestCallback;
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APIRequestInterface;
import company.tap.google.pay.internal.tapnetworkkit.request.TapRequest;


public class NetworkController {

    private APIRequestInterface apiRequestInterface;

    /**
     * Required to set baseURL on retrofit
     */
    public void setBaseUrl(String baseURL, Context context, Boolean debugMode, String packageId, @Nullable AppCompatActivity activity ,@Nullable String authtoken) {
        apiRequestInterface = RetrofitHelper.getApiHelper(baseURL, context, debugMode,packageId, activity, authtoken);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void processRequest(TapMethodType method, String apiName, String requestBody, APIRequestCallback callback, int requestCode) {
        switch (method) {
            case GET:
                new TapRequest(apiRequestInterface.getRequest(apiName), callback, requestCode).run();
                break;
            case POST:
                Map<String, Object> requestMap = new Gson().fromJson(
                        requestBody, new TypeToken<HashMap<String, Object>>() {}.getType()
                );
                new TapRequest(apiRequestInterface.postRequest(apiName,requestMap), callback, requestCode).run();
                break;
            case PUT:
                new TapRequest(apiRequestInterface.putRequest(apiName), callback, requestCode).run();
                break;
            case DELETE:
                new TapRequest(apiRequestInterface.delete(apiName), callback, requestCode).run();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + method);
        }
    }

    private static class SingletonCreationAdmin {
        private static final NetworkController INSTANCE = new NetworkController();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static NetworkController getInstance() {
        return SingletonCreationAdmin.INSTANCE;
    }

}


