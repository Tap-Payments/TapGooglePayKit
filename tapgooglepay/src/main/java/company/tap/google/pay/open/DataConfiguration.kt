package company.tap.google.pay.open

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import company.tap.google.pay.internal.GoogleApiActivity
import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.api.ApiService
import company.tap.google.pay.internal.interfaces.SDKDelegate
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.SDKMode
import company.tap.tapnetworkkit.connection.NetworkApp
import java.math.BigDecimal


object  DataConfiguration {
    private var sdkDelegate: SDKDelegate? = null
    private var paymentDataSource: PaymentDataSource? = null

    init {
        initPaymentDataSource()

    }

    private fun initPaymentDataSource() {
        paymentDataSource = PaymentDataSource
      /*  if (paymentDataSource != null) {
            println("paymentDataSource sdk ${paymentDataSource.toString()}")
            PaymentDataProvider().setExternalDataSource(paymentDataSource!!)
        }*/


    }
    fun addSDKDelegate(_sdkDelegate: SDKDelegate) {
        println("addSDKDelegate sdk ${_sdkDelegate}")
        sdkDelegate = _sdkDelegate


    }
    fun getListener(): SDKDelegate? {
        return sdkDelegate
    }
    /**
     * set amount  The total amount you want to collect
     */
    fun setAmount(amount: BigDecimal) {
        println("amount ... $amount")
        paymentDataSource?.setAmount(amount)
    }

    /**
     * set transaction currency
     *
     * @param transactionCurrency the tap currency
     */
    fun setTransactionCurrency(transactionCurrency: String) {
        paymentDataSource?.setTransactionCurrency(transactionCurrency)
    }
    /// Indicates the mode the merchant wants to run the sdk with. Default is sandbox mode
    fun setEnvironmentMode(sdkMode: SDKMode) {
        paymentDataSource?.setEnvironmentMode(sdkMode)
    }
    /**
     * set gatewayId
     *
     * @param gatewayId of TAP
     */
    fun setGatewayId(gatewayId: String) {
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
   /// The payment networks you  want to limit the payment to default [.Amex,.Visa,.Mada,.MasterCard]
    fun setAllowedCardNetworks(allowedCardNetworks:MutableList<String>) {
        paymentDataSource?.setAllowedCardNetworks(allowedCardNetworks)
    }
   // Defines type of authentication you want PAN_ONLY, CRYPTOGRAM_3DS, ALL
    fun setAllowedCardAuthMethods(allowedMethods: AllowedMethods) {
        paymentDataSource?.setAllowedCardAuthMethods(allowedMethods)
    }
/// - Parameter countryCode: The country code where the user transacts default .AED
    fun setCountryCode(countryCode: String) {
        paymentDataSource?.setCountryCode(countryCode)
    }
    /// Inidcates the tap provided keys for this merchant to use for his transactions. If not set, any transaction will fail. Please if you didn't get a tap key yet, refer to https://www.tap.company/en/sell
    fun initSDK(context:Context,secretKeys :String , packageID: String){
        initNetworkCallOfKit(context,secretKeys,packageID)
    }

    private fun initNetworkCallOfKit(context: Context,secretKeys: String,packageID: String) {
        NetworkApp.initNetwork(
            context,
            secretKeys,
            packageID,
            ApiService.BASE_URL,
            //   sdkIdentifier,BuildConfig.EncryptAPIKEY)
            "NATIVE",true)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startGooglePay(activity: Activity, googlePayButton: View){
        GooglePayButton(activity as Context).possiblyShowGooglePayButton(activity,googlePayButton)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getGooglePayToken(activity: Activity,googlePayButton: GooglePayButton){
        GooglePayButton(activity as Context).possiblyShowGooglePayButton(activity,googlePayButton)
    }
}

