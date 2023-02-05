package company.tap.google.pay.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import company.tap.google.pay.open.DataConfiguration
import org.json.JSONObject

internal object GooglePayAPI :Activity() {
    @JvmField
    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    @SuppressLint("StaticFieldLeak")
    private lateinit var paymentsClient: PaymentsClient


    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/
    PaymentsClient.html.isReadyToPay
    ) */
    @RequiresApi(api = Build.VERSION_CODES.N)
     fun possiblyShowGooglePayButton(activity: Activity) {
        paymentsClient = PaymentsUtil.createPaymentsClient(activity)
        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient?.isReadyToPay(request)
        task?.addOnCompleteListener { completedTask ->
            try {
                completedTask.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
            }
        }
    }
    fun handleCheckGPayStatus(activity: Activity){
        // Disables the button to prevent multiple clicks.
        //googlePayButton?.isClickable = false

       // isGooglePayClicked = true
        val paymentDataRequestJson: JSONObject? = PaymentDataSource.getAmount()?.toLong()?.let { PaymentsUtil.getPaymentDataRequest(it) }
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }

        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        println("request value is>>>" + request.toJson())
       // println("Activity is>>>" + this as Activity)

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), activity, LOAD_PAYMENT_DATA_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("<<<<onActivityResult>>>"+resultCode)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val paymentData = data?.let { PaymentData.getFromIntent(it) }
                        if (paymentData != null) {
                          //  tapCheckoutFragment._viewModel?.handlePaymentSuccess(paymentData)
                        }else {
                            AutoResolveHelper.getStatusFromIntent(data)?.statusCode

                        }

                     //   isGooglePayClicked = false

                    }
                    RESULT_CANCELED -> {

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
            // googlePayButton.setVisibility(View.VISIBLE)
        } else {
            // Toast.makeText(holder.itemView.getContext(), R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show()
        }
    }
}