package company.tap.google.pay.open

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentsClient
import company.tap.google.pay.R
import company.tap.google.pay.internal.GoogleApiActivity
import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.PaymentsUtil
import company.tap.google.pay.open.enums.GooglePayButtonType
import company.tap.google.pay.open.enums.GooglePayEnviroment
import java.math.BigDecimal


@SuppressLint("ViewConstructor")
 class GooglePayButton: LinearLayout{
    @SuppressLint("StaticFieldLeak")
    private lateinit var paymentsClient: PaymentsClient
    lateinit var _activity: Activity
    val mainView by lazy { findViewById<LinearLayout>(R.id.mainLL) }
     lateinit var googlePayButton :View
    lateinit var buttonView: View
    private var paymentDataSource: PaymentDataSource? = null

    @JvmField var googlePayButtonType:GooglePayButtonType?=GooglePayButtonType.NORMAL_GOOGLE_PAY

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
        initPaymentDataSource()
  }





    private fun initPaymentDataSource() {
        paymentDataSource = PaymentDataSource

    }
    fun setGooglePayButtonType(__googlePayButtonType: GooglePayButtonType?){
        this.googlePayButtonType= __googlePayButtonType
        setButtonType(googlePayButtonType)
    }
    private fun setButtonType(_googlePayButtonType:GooglePayButtonType?){
        if(_googlePayButtonType?.name == GooglePayButtonType.NORMAL_GOOGLE_PAY.name){
            buttonView  = LayoutInflater.from(context).inflate(R.layout.plain_googlepay_button, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.BUY_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.buy_with_google_pay, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.DONATE_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.donate_with_google_pay, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.PAY_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.pay_with_google_pay, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.SUBSCRIBE_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.subscribe_with_google_pay, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.CHECKOUT_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.checkout_with_googlepay_button, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.ORDER_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.order_with_googlepay_button, mainView, false)
            mainView.addView(buttonView)
        }else if(_googlePayButtonType?.name == GooglePayButtonType.BOOK_WITH_GOOGLE_PAY.name){
            buttonView = LayoutInflater.from(context).inflate(R.layout.book_with_googlepay_button, mainView, false)
            mainView.addView(buttonView)
        }

        mainView.isFocusable= true
        mainView.isEnabled= true
    }
    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see [](https://developers.google.com/android/reference/com/google/android/gms/wallet/
    PaymentsClient.html.isReadyToPay
    ) */
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun possiblyShowGooglePayButton(activity: Activity, _googlePayButton: View ,googlePayToken :Boolean ,googlePayButtonType: GooglePayButtonType?=null) {
     //   _googlePayButton.visibility = VISIBLE
       this.googlePayButtonType = googlePayButtonType
       // setVisibilityOfButtons(googlePayButtonType)
        //this.googlePayTokenRqd = googlePayToken
       // this.tapTokenRqd = tapToken

        googlePayButton = _googlePayButton
        _googlePayButton.isEnabled= true
        _googlePayButton.isFocusable= true
        _googlePayButton.isClickable= true
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
                setGooglePayAvailable(completedTask.getResult(ApiException::class.java),_googlePayButton.rootView,googlePayToken)
            } catch (exception: ApiException) {
                // Process error
                Log.w("isReadyToPay failed", exception)
                TapDataConfiguration.getListener()?.onFailed(exception.toString())
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
    private fun setGooglePayAvailable(available: Boolean,___googlePayButton: View,googlePayToken: Boolean) {
        println("available$available")
        if (available) {
            println("googlePayButton$___googlePayButton")
            ___googlePayButton.visibility= View.VISIBLE
            ___googlePayButton.isEnabled= true
            ___googlePayButton.isFocusable= true
            ___googlePayButton.isClickable= true
            val intent = Intent(_activity as Context, GoogleApiActivity::class.java)
            intent.putExtra("googlePayToken",googlePayToken)
            (_activity as Context).startActivity(intent)


        } else {
            ___googlePayButton.visibility= View.GONE
            TapDataConfiguration.getListener()?.onFailed("Google Pay Not Supported")
            // Toast.makeText(holder.itemView.getContext(), R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show()
        }
    }

    fun setGooglePayData(environment: GooglePayEnviroment, authentication:MutableList<String>?, supportedNetworks:MutableList<String>?,
                         amount:BigDecimal, currency: String,country:String){
        paymentDataSource?.setEnvironmentMode(environment)
        paymentDataSource?.setAllowedCardAuthMethods(authentication)
        paymentDataSource?.setAllowedCardNetworks(supportedNetworks)
        paymentDataSource?.setAmount(amount)
        paymentDataSource?.setTransactionCurrency(currency)
        paymentDataSource?.setCountryCode(country)


    }




}