package company.tap.google.pay.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import company.tap.google.pay.internal.api.ApiCallsKitRepository
import company.tap.google.pay.internal.api.requests.CreateTokenGPayRequest
import company.tap.google.pay.open.DataConfiguration
import company.tap.google.pay.open.GooglePayButton
import org.json.JSONException
import org.json.JSONObject

@RestrictTo(RestrictTo.Scope.LIBRARY)
class GoogleApiActivity : Activity() {
    @JvmField
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    @SuppressLint("StaticFieldLeak")
    private lateinit var paymentsClient: PaymentsClient
     var _activity: Activity = this

     lateinit var GPAY: View
      var googlePayTokenOnly: Boolean =false


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.statusBarColor = resources.getColor(android.R.color.transparent)
        }
        _activity =this
        paymentsClient = PaymentsUtil.createPaymentsClient(this)
        val googlePayTokenval =intent.getBooleanExtra("googlePayToken",false)
        googlePayTokenOnly = googlePayTokenval
        possiblyShowGooglePayButton(_activity)

    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }
    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/
    PaymentsClient.html.isReadyToPay
    ) */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun possiblyShowGooglePayButton(activity: Activity) {
       // _activity = activity
        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        println("request is" + request.toJson())
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                //completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
                setGooglePayAvailable(completedTask.getResult(ApiException::class.java))
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }

    }

    fun handleGPayCall(__paymentsClient: PaymentsClient,activity: Activity) {

        this.paymentsClient = __paymentsClient
        // Disables the button to prevent multiple clicks.
        //googlePayButton.isClickable = false

        // isGooglePayClicked = true
        val paymentDataRequestJson: JSONObject? =
            PaymentDataSource.getAmount()?.toLong()?.let { PaymentsUtil.getPaymentDataRequest(it) }
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }

        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        println("request value is>>>" + request.toJson())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {

            AutoResolveHelper.resolveTask(
                __paymentsClient.loadPaymentData(request), _activity, LOAD_PAYMENT_DATA_REQUEST_CODE
            )

        }

    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("<<<<onActivityResult>>>" + resultCode)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val paymentData = data?.let { PaymentData.getFromIntent(it) }
                        if (paymentData != null) {
                            println("paymentData" + paymentData.toJson())
                            handleSuccessCallBack(paymentData)

                        } else {
                            AutoResolveHelper.getStatusFromIntent(data)?.statusCode

                        }

                        //   isGooglePayClicked = false

                    }
                    RESULT_CANCELED -> {
                        DataConfiguration.getListener()?.onFailed("RESULT_CANCELED")
                        finish()
                        // isGooglePayClicked = false

                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        if (status != null) println(if ("status values are>>$status" != null) status.statusMessage else status.toString() + " >> code " + status.statusCode)
                        //    tapCheckoutFragment._viewModel?.handleError(status?.statusCode ?: 400)
                        // isGooglePayClicked = false

                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleSuccessCallBack(paymentData: PaymentData) {
        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).

        val paymentInfo = paymentData.toJson() ?: return
       // LoadingScreenManager.getInstance().showLoadingScreen(this)
        try {
            val paymentMethodData = JSONObject(paymentInfo).getJSONObject("paymentMethodData")
            println("paymentMethodData$paymentMethodData")
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            val tokenizationData = paymentMethodData.getJSONObject("tokenizationData")
            println("tokenizationData>>>$tokenizationData")
            // final String tokenizationType = tokenizationData.getString("type");
            val token = tokenizationData.getString("token")

           if(googlePayTokenOnly) {
               DataConfiguration.getListener()?.onGooglePayToken(token)
               finish()
               return

           }else {
              // DataConfiguration.getListener()?.onGooglePayToken(token)
               //  System.out.println("token is"+token);
               val gson = Gson()
               val jsonToken = gson.fromJson(token, JsonObject::class.java)

               /**
                * At this stage, Passing the googlePaylaod to Tap Backend TokenAPI call followed by chargeAPI.
                */
               val createTokenGPayRequest =
                   CreateTokenGPayRequest(
                       "googlepay",
                       jsonToken
                   )
               ApiCallsKitRepository().getGPayTokenRequest(this, createTokenGPayRequest)
           }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * If isReadyToPay returned `true`, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns `false`.
     *
     * @param available isReadyToPay API response.
     */
    private fun setGooglePayAvailable(available: Boolean) {
        println("available$available")
        if (available) {
            handleGPayCall( paymentsClient,_activity)

        } else {
            // Toast.makeText(holder.itemView.getContext(), R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show()
        }
    }
    companion object{

    }
}