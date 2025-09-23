package company.tap.google.pay.internal.api

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonElement
import company.tap.google.pay.internal.api.requests.CreateTokenGPayRequest
import company.tap.google.pay.internal.api.responses.Token
import company.tap.google.pay.internal.tapnetworkkit.controller.NetworkController
import company.tap.google.pay.internal.tapnetworkkit.enums.TapMethodType
import company.tap.google.pay.internal.tapnetworkkit.exception.GoSellError
import company.tap.google.pay.internal.tapnetworkkit.interfaces.APIRequestCallback
import company.tap.google.pay.open.DataConfiguration


import retrofit2.Response

class ApiCallsKitRepository : APIRequestCallback {
    lateinit var tokenResponse: Token
    lateinit var activity: Activity
    @RequiresApi(Build.VERSION_CODES.N)
    fun getGPayTokenRequest(
        _activity: Activity,
       createTokenGPayRequest: CreateTokenGPayRequest
    ) {
        activity =_activity
        val jsonString = Gson().toJson(createTokenGPayRequest)
        NetworkController.getInstance().processRequest(
            TapMethodType.POST, ApiService.TOKEN, jsonString,
            this, CREATE_GPAY_TOKEN_CODE
        )
    }
    override fun onSuccess(responseCode: Int, requestCode: Int, response: Response<JsonElement>?) {
        if (requestCode == CREATE_GPAY_TOKEN_CODE) {
            response?.body().let {
                println("response is"+response)
                tokenResponse = Gson().fromJson(it, Token::class.java)
              DataConfiguration.getListener()?.onTapToken(tokenResponse)
                activity.finish()
            }
        }
    }


    override fun onFailure(requestCode: Int, errorDetails: GoSellError?) {
        if (requestCode == CREATE_GPAY_TOKEN_CODE) {
            errorDetails?.errorBody.let {

                errorDetails?.errorBody?.let { it1 -> DataConfiguration.getListener()?.onFailed(it1) }
                activity.finish()
            }
        }
    }


    companion object {

        private const val CREATE_GPAY_TOKEN_CODE =1

    }
}