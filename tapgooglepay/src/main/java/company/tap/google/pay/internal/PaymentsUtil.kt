package company.tap.google.pay.internal

import android.app.Activity
import androidx.annotation.RestrictTo
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import company.tap.google.pay.open.enums.Authentication
import company.tap.google.pay.open.enums.GooglePayEnviroment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
@RestrictTo(RestrictTo.Scope.LIBRARY)
object PaymentsUtil {
    /**
     * Create a Google Pay API base request object with properties used in all requests.
     *
     * @return Google Pay API base request object.
     * @throws JSONException
     */
    private val baseRequest = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
    }

    /**
     * Gateway Integration: Identify your gateway and your app's gateway merchant identifier.
     *
     *
     * The Google Pay API response will return an encrypted payment method capable of being charged
     * by a supported gateway after payer authorization.
     *
     *
     * TODO: Check with your gateway on the parameters to pass and modify them in Constants.java.
     *
     * @return Payment data tokenization for the CARD payment method.
     * @throws JSONException
     * @see [PaymentMethodTokenizationSpecification](https://developers.google.com/pay/api/android/reference/object.PaymentMethodTokenizationSpecification)
     */
    private fun gatewayTokenizationSpecification(): JSONObject {
        return JSONObject().apply {
            put("type", "PAYMENT_GATEWAY")
            put("parameters", JSONObject(mapOf(
                "gateway" to PaymentDataSource.getGatewayId(),
                "gatewayMerchantId" to PaymentDataSource.getGatewayMerchantId())))
        }
    }

    private fun baseCardPaymentMethod(): JSONObject {
        var  jsonArray :JSONArray ?= null
        return JSONObject().apply {
            if(PaymentDataSource.getAllowedCardMethod().toString()==Authentication.ALL.name){
              jsonArray = JSONArray  (Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS"))
            }else {
                jsonArray = JSONArray(PaymentDataSource.getAllowedCardMethod())
            }

            println("PaymentDataSource.getAllowedNetworks()"+PaymentDataSource.getAllowedNetworks())
            val parameters = JSONObject().apply {
                put("allowedAuthMethods", jsonArray)
               // put("allowedCardNetworks", JSONArray(PaymentDataSource.getAllowedNetworks()))
                put("allowedCardNetworks",JSONArray(PaymentDataSource.getAllowedNetworks()))

            }

            put("type", "CARD")
            put("parameters", parameters)
        }
    }

    /**
     * An object describing accepted forms of payment by your app, used to determine a viewer's
     * readiness to pay.
     *
     * @return API version and payment methods supported by the app.
     * @see [IsReadyToPayRequest](https://developers.google.com/pay/api/android/reference/object.IsReadyToPayRequest)
     */
    fun isReadyToPayRequest(): JSONObject? {
        return try {
            baseRequest.apply {
                put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod()))
            }

        } catch (e: JSONException) {
            null
        }
    }
    /**
     * Creates an instance of [PaymentsClient] for use in an [Activity] using the
     * environment and theme set in [Constants].
     *
     * @param activity is the caller's activity.
     */
    fun createPaymentsClient(activity: Activity): PaymentsClient {
        var walletOptions: Wallet.WalletOptions? = null
        if (PaymentDataSource.getEnvironment() != null) {
            if (PaymentDataSource.getEnvironment() == GooglePayEnviroment.ENVIRONMENT_TEST) {
                walletOptions =
                    Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
            } else if (PaymentDataSource.getEnvironment() == GooglePayEnviroment.ENVIRONMENT_PRODUCTION
            ) {
                walletOptions =
                    Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION)
                        .build()
            } else walletOptions =
                Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        }
        return Wallet.getPaymentsClient((activity), (walletOptions!!))
    }

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status.
     *
     * @return information about the requested payment.
     * @throws JSONException
     * @see [TransactionInfo](https://developers.google.com/pay/api/android/reference/object.TransactionInfo)
     */
    @Throws(JSONException::class)
    private fun getTransactionInfo(price: String): JSONObject {
        return JSONObject().apply {
            put("totalPrice", price)
            put("totalPriceStatus", "FINAL")
            put("countryCode", PaymentDataSource.getCountryCode())
            put("currencyCode", PaymentDataSource.getCurrency())
        }
    }
    /**
     * Describe the expected returned payment data for the CARD payment method
     *
     * @return A CARD PaymentMethod describing accepted cards and optional fields.
     * @throws JSONException
     * @see [PaymentMethod](https://developers.google.com/pay/api/android/reference/object.PaymentMethod)
     */
    private fun cardPaymentMethod(): JSONObject {
        val cardPaymentMethod = baseCardPaymentMethod()
        cardPaymentMethod.put("tokenizationSpecification", gatewayTokenizationSpecification())

        return cardPaymentMethod
    }

    /**
     * An object describing information requested in a Google Pay payment sheet
     *
     * @return Payment data expected by your app.
     * @see [PaymentDataRequest](https://developers.google.com/pay/api/android/reference/object.PaymentDataRequest)
     */
    fun getPaymentDataRequest(priceCemts: Long): JSONObject? {
        return try {
            baseRequest.apply {
                put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod()))
                put("transactionInfo", getTransactionInfo(priceCemts.toString()))
                put("merchantInfo", merchantInfo)

            }
        } catch (e: JSONException) {
            null
        }
    }

    /**
     * Information about the merchant requesting payment information
     *
     * @return Information about the merchant.
     * @throws JSONException
     * @see [MerchantInfo](https://developers.google.com/pay/api/android/reference/object.MerchantInfo)
     */
    private val merchantInfo: JSONObject =
        JSONObject().put("merchantName", PaymentDataSource.getGatewayMerchantId())

}