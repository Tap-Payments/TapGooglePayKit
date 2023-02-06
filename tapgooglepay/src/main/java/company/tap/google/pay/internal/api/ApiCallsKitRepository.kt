package company.tap.google.pay.internal.api

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonElement
import company.tap.google.pay.open.DataConfiguration
import company.tap.tapnetworkkit.controller.NetworkController
import company.tap.tapnetworkkit.enums.TapMethodType
import company.tap.tapnetworkkit.exception.GoSellError
import company.tap.tapnetworkkit.interfaces.APIRequestCallback
import retrofit2.Response

class ApiCallsKitRepository : APIRequestCallback{
    lateinit var tokenResponse: Token
    @RequiresApi(Build.VERSION_CODES.N)
    fun getGPayTokenRequest(
        _context: Context,
       createTokenGPayRequest: CreateTokenGPayRequest
    ) {
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
            }
        }
    }

    override fun onFailure(requestCode: Int, errorDetails: GoSellError?) {
        TODO("Not yet implemented")
    }

    companion object {

        private const val CREATE_GPAY_TOKEN_CODE =1

    }
}