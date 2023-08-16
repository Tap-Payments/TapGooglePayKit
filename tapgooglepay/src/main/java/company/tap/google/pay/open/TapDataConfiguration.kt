package company.tap.google.pay.open

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.R
import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.api.ApiService
import company.tap.google.pay.open.enums.GooglePayEnviroment
import company.tap.tapnetworkkit.connection.NetworkApp
import java.math.BigDecimal


object TapDataConfiguration {
    private var sdkDelegate: SDKDelegate? = null
    private var paymentDataSource: PaymentDataSource? = null

    init {
        initPaymentDataSource()

    }

    private fun initPaymentDataSource() {
        paymentDataSource = PaymentDataSource

    }

    fun addSDKDelegate(_sdkDelegate: SDKDelegate) {
        println("addSDKDelegate sdk ${_sdkDelegate}")
        sdkDelegate = _sdkDelegate


    }

    fun getListener(): SDKDelegate? {
        return sdkDelegate
    }

    /**
     * set gatewayId
     *
     * @param gatewayId of TAP
     */
    fun setGatewayId(gatewayId: String) {
        println("gatewayId>>" + gatewayId)
        paymentDataSource?.setGatewayId(gatewayId)
    }

    /**
     * set gatewayMerchantId
     *
     * @param gatewayMerchantId of TAP
     */
    fun setGatewayMerchantID(gatewayMerchantId: String) {
        paymentDataSource?.setGatewayMerchantId(gatewayMerchantId)
    }

    /// Inidcates the tap provided keys for this merchant to use for his transactions. If not set, any transaction will fail. Please if you didn't get a tap key yet, refer to https://www.tap.company/en/sell
    fun initSDK(context: Context, secretKeys: String, packageID: String) {
        initNetworkCallOfKit(context, secretKeys, packageID)
    }

    private fun initNetworkCallOfKit(context: Context, secretKeys: String, packageID: String) {
        NetworkApp.initNetwork(
            context,
            secretKeys,
            packageID,
            ApiService.BASE_URL,
            //   sdkIdentifier,BuildConfig.EncryptAPIKEY)
            "NATIVE", true, context.resources.getString(company.tap.google.pay.R.string.enryptkey), context as AppCompatActivity?
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startGooglePay(activity: Activity, googlePayButton: GooglePayButton) {
        googlePayButton.possiblyShowGooglePayButton(activity, googlePayButton, false)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getGooglePayToken(activity: Activity, googlePayButton: GooglePayButton) {
        googlePayButton.possiblyShowGooglePayButton(activity, googlePayButton, true)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTapToken(activity: Activity, googlePayButton: GooglePayButton) {
        googlePayButton.possiblyShowGooglePayButton(activity, googlePayButton, false)

    }
}

