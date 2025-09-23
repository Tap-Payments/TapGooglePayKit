package company.tap.google.pay.internal.tapnetworkkit.interfaces;

import com.google.gson.JsonElement;

import company.tap.google.pay.internal.tapnetworkkit.exception.GoSellError;

import retrofit2.Response;

/**
 * Base callback to process API responses
 */
public interface APIRequestCallback {
    /**
     * Success callback. Request is considered as successful when response code is between 200 and 299
     *
     * @param responseCode Response code, from 200 to 299
     * @param response     Serialized response of type or null in case when response could not be serialized into json element
     */
    void onSuccess(int responseCode, int requestCode, Response<JsonElement> response);

    /**
     * General failure callback
     *
     * @param errorDetails {@link GoSellError} representing a failure reason
     */
    void onFailure(int requestCode, GoSellError errorDetails);

}