package company.tap.google.pay.internal.interfaces

import com.google.android.gms.wallet.PaymentData
import company.tap.google.pay.internal.api.Token

interface SDKDelegate {
    fun onGooglePayToken(token:String)
    fun onTapToken(token:Token)
    fun onFailed(error:String)

}