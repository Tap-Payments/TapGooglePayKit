package company.tap.google.pay.internal.interfaces

interface SDKDelegate {
    fun onGooglePayToken()
    fun onTapToken()
    fun onFailed()

}