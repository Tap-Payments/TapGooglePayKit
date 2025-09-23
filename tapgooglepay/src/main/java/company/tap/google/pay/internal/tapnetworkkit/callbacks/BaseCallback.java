package company.tap.google.pay.internal.tapnetworkkit.callbacks;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import java.io.IOException;


import company.tap.google.pay.internal.tapnetworkkit.exception.GoSellError;
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APIRequestCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <br>
 * Base callback with response handling
 */
public final class BaseCallback implements Callback<JsonElement> {
    private static final String UNABLE_TO_FETCH_ERROR_INFO = "Unable to fetch error information";
    private APIRequestCallback requestCallback;
    private int requestCode;

    /**
     * Instantiates a new Base callback.
     *
     * @param requestCallback the request callback
     */
    public BaseCallback(APIRequestCallback requestCallback, int requestCode) {
        this.requestCallback = requestCallback;
        this.requestCode = requestCode;
    }

    @Override
    public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
        if (response.isSuccessful()) {
            requestCallback.onSuccess(response.code(), requestCode, response);
        } else {
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                try {
                    requestCallback.onFailure(requestCode, new GoSellError(response.code(), errorBody.string(), null));
                } catch (IOException e) {
                    requestCallback.onFailure(requestCode, new GoSellError(GoSellError.ERROR_CODE_UNAVAILABLE, null, e));
                }
            } else {
                requestCallback.onFailure(requestCode, new GoSellError(response.code(), UNABLE_TO_FETCH_ERROR_INFO, null));
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
        requestCallback.onFailure(requestCode, new GoSellError(GoSellError.ERROR_CODE_UNAVAILABLE, null, t));
    }
}
