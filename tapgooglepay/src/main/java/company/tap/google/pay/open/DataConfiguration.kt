package company.tap.google.pay.open


import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import company.tap.google.pay.R
import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.api.ApiService
import company.tap.google.pay.internal.tapnetworkkit.connection.NetworkApp
import company.tap.google.pay.internal.tapnetworkkit.utils.CryptoUtil
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.GooglePayButtonType
import company.tap.google.pay.open.enums.SDKMode
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException


import java.math.BigDecimal
import java.util.HashMap


object  DataConfiguration {
    private var sdkDelegate: SDKDelegate? = null
    private var paymentDataSource: PaymentDataSource? = null
    private var testEncKey: String? = null
    private var prodEncKey: String? = null
  //  var hasGooglePay = false
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
    fun setAllowedCardNetworks(allowedCardNetworks: MutableList<String>?) {
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
    fun initSDK(context:Context,publicKey :String) {
       val merchantId = paymentDataSource?.getGatewayMerchantId() ?: ""
        val amount = paymentDataSource
            ?.getAmount()?.toPlainString()
            ?.toBigDecimalOrNull()
            ?: BigDecimal.ZERO
        val currency = paymentDataSource?.getCurrency() ?: "KWD"


        val requestMap = hashMapOf<String, Any>(
            "merchant_id" to merchantId,
            "payment_type" to "DEVICE",
            "total_amount" to amount,
            "currency" to currency,
            "transaction_mode" to "PURCHASE",
            "order" to createOrder(merchantId, amount, currency),
            "customer" to createCustomer(),
            "items" to listOf(createItem(amount, currency))
        )

        initNetworkCallOfKit(context, publicKey, requestMap)
    }

    private fun createOrder(
        merchantId: String,
        amount: BigDecimal,
        currency: String
    ): HashMap<String, Any> {

        return hashMapOf(
            "amount" to amount,
            "currency" to currency,
            "payment_type" to "DEVICE",
            "transaction_mode" to "PURCHASE",
            "merchant" to hashMapOf("id" to merchantId),
            "items" to listOf(createItem(amount, currency)),
            "customer" to createCustomer()
        )
    }
    private fun createCustomer(): HashMap<String, Any> {

        return hashMapOf(
            "editable" to true,
            "email" to "taptestingemail@gmail.com",
            "locale" to "en",
            "name_on_card" to "",
            "first_name" to "Tap Testing Default",
            "phone" to hashMapOf(
                "number" to "90064542",
                "country_code" to "965"
            )
        )
    }
    private fun createItem(amount: BigDecimal, currency: String): HashMap<String, Any> {

        return hashMapOf(
            "description" to "GOOGLE PAY",
            "category" to "DIGITAL_GOODS",
            "quantity" to 1,
            "account_code" to "",
            "item_code" to "",
            "amount" to amount,
            "tags" to "",
            "product_id" to "",
            "fulfillment_service" to "",
            "requires_shipping" to false,
            "currency" to currency,
            "name" to "PAY"
        )
    }
    private fun initNetworkCallOfKit(context: Context,publicKey: String , configuraton: HashMap<String, Any>) {


          val  testEncKey = context.resources.getString(R.string.enryptkey)
        val encodedeky = getPublicEncryptionKey(publicKey,context)

        NetworkApp.initNetwork(
            context ,
            publicKey ?: "",
            context.packageName,
            ApiService.BASE_URL,
            "android-googlepay",
            true,
            encodedeky,
            null
        )

        // Determine if test or production environment based on public key
        val isTestMode = publicKey?.startsWith("pk_test_") ?: false
      callCheckOutProfileAPI(configuraton,isTestMode,context,publicKey)


    }
    private fun getPublicEncryptionKey(
        publicKey: String? ,context: Context
    ): String? {
        if (!testEncKey.isNullOrBlank() && !prodEncKey.isNullOrBlank()) {
            return if (publicKey?.contains("test") == true) {
                // println("EncKey>>>>>" + testEncKey)
                testEncKey
            } else {
                //  println("EncKey<<<<<<" + prodEncKey)
                prodEncKey
            }
        } else {
            //  println("EncKey<<<<<<>>>>>>>>>" + testEncKey)
            return if (publicKey?.contains("test") == true) {
                context.resources?.getString(R.string.enryptkeyTest)
            }else{
                context.resources?.getString(R.string.enryptkeyProduction)
            }


        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
   // fun startGooglePay(activity: Activity, googlePayButton: View,googleButtonType: GooglePayButtonType?){
    fun startGooglePay(activity: Activity, googlePayButton: GooglePayButton){
        googlePayButton.possiblyShowGooglePayButton(activity,googlePayButton,false,null)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getGooglePayToken(activity: Activity,googlePayButton: GooglePayButton){
        googlePayButton.possiblyShowGooglePayButton(activity,googlePayButton,true,null)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTapToken(activity: Activity,googlePayButton: GooglePayButton){
        googlePayButton.possiblyShowGooglePayButton(activity,googlePayButton,false,null)

    }
    private fun callCheckOutProfileAPI(configuraton: java.util.HashMap<String, Any>, isTestMode: Boolean = true , context: Context ,publicKey: String) {


        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()



        // ✅ Convert HashMap → JSONObject dynamically
        val jsonObject = JSONObject(configuraton as Map<*, *>)

        // ✅ Select encryption key based on test/prod mode
        val encryptionKey = if (isTestMode) {
            context.resources.getString(R.string.enryptkeyTest)
        } else {
            context.resources.getString(R.string.enryptkeyProduction)
        }

        val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        println("body in >>"+jsonObject)
        val request = Request.Builder()
            .url(ApiService.BASE_URL + "checkoutprofile")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization",  publicKey)
            .addHeader("application", NetworkApp.getApplicationInfo())
            .addHeader(
                "mdn",
                CryptoUtil.encryptJsonString(
                    context.packageName,
                    encryptionKey
                )
            )
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) throw IOException("Unexpected code $it")

                    val responseBody = it.body?.string()
                    println(responseBody)

                    try {
                        val jsonResponse = JSONObject(responseBody ?: "")

                        if (!jsonResponse.toString().contains("errors")) {


                            // ✅ Check Google Pay inside payment_options
                            val paymentOptions = jsonResponse.optJSONObject("payment_options")
                            val paymentMethods = paymentOptions?.optJSONArray("payment_methods")



           /*                 if (paymentMethods != null) {
                                for (i in 0 until paymentMethods.length()) {
                                    val method = paymentMethods.optJSONObject(i)

                                    val paymentType = method?.optString("payment_type", "")
                                    val name = method?.optString("name", "")

                                    if (paymentType.equals("google_pay", true) ||
                                        name.equals("GOOGLE_PAY", true)
                                    ) {
                                        hasGooglePay = true
                                        break
                                    }
                                }
                            }

                            println("Google Pay Available: $hasGooglePay")*/
                            // Safe session extraction
                            val session = jsonResponse.optString("session", "")

                            if (session.isNotEmpty()) {
                                println("Session Value: $session")
                                NetworkApp.initNetworkToken(session,
                                    context,
                                    ApiService.BASE_URL,
                                    true,
                                    null
                                )
                            } else {
                                println("Session not found")
                            }
                        }

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        })

    }
}

