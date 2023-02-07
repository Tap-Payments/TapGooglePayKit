package company.tap.google.pay.open

import company.tap.google.pay.internal.api.responses.Token

interface SDKDelegate {
    fun onGooglePayToken(token:String)
    fun onTapToken(token: Token)
    fun onFailed(error:String)

}