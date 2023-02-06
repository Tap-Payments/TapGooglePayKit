package company.tap.google.pay.open

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import company.tap.google.pay.R
import company.tap.google.pay.internal.GoogleApiActivity
import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.PaymentsUtil
import org.json.JSONObject
@SuppressLint("ViewConstructor")
 class GooglePayButton: LinearLayout{
    @SuppressLint("StaticFieldLeak")
    private lateinit var paymentsClient: PaymentsClient
    lateinit var _activity: Activity
     //var googlePayButton :View

    /**
     * Simple constructor to use when creating a TapPayCardSwitch from code.
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     **/
    constructor(context: Context) : super(context)

    /**
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     *  @param attrs The attributes of the XML Button tag being used to inflate the view.
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {

       View.inflate(context, R.layout.google_pay_layout,this)
       // googlePayButton = findViewById(R.id.gPay)
  }

    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/
    PaymentsClient.html.isReadyToPay
    ) */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun possiblyShowGooglePayButton(activity: Activity, _googlePayButton: View) {
      /*  googlePayButton = _googlePayButton
        googlePayButton.isEnabled= true
        googlePayButton.isFocusable= true
        googlePayButton.isClickable= true*/
        _activity = activity
        paymentsClient = PaymentsUtil.createPaymentsClient(activity)
        val isReadyToPayJson = PaymentsUtil.isReadyToPayRequest() ?: return
        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString()) ?: return

        println("request is" + request.toJson())
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        val task = paymentsClient.isReadyToPay(request)
        task.addOnCompleteListener { completedTask ->
            try {
                setGooglePayAvailable(completedTask.getResult(ApiException::class.java),_googlePayButton)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
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
    private fun setGooglePayAvailable(available: Boolean,___googlePayButton: View) {
        println("available$available")
        if (available) {
            println("googlePayButton$___googlePayButton")
            ___googlePayButton.visibility= View.VISIBLE
            ___googlePayButton.isEnabled= true
            ___googlePayButton.isFocusable= true
            ___googlePayButton.isClickable= true
            val myIntent = Intent(_activity as Context, GoogleApiActivity::class.java)
           // GoogleApiActivity().GPAY = googlePayButton
            (_activity as Context).startActivity(myIntent)
           /* googlePayButton.setOnClickListener {
                val myIntent = Intent(_activity as Context, GoogleApiActivity::class.java)
                GoogleApiActivity().GPAY = googlePayButton
                (_activity as Context).startActivity(myIntent)
            }*/


        } else {
            ___googlePayButton.visibility= View.GONE
            DataConfiguration.getListener()?.onFailed("Google Pay Not Supported")
            // Toast.makeText(holder.itemView.getContext(), R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show()
        }
    }
}