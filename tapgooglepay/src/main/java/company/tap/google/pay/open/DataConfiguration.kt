package company.tap.google.pay.open

import company.tap.google.pay.internal.PaymentDataSource
import company.tap.google.pay.internal.interfaces.SDKDelegate
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.GPayWalletMode
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
     * set amount
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

    fun setEnvironmentMode(gPayMode: GPayWalletMode) {
        paymentDataSource?.setEnvironmentMode(gPayMode)
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

    fun setAllowedCardNetworks(allowedCardNetworks:List<String>) {
        paymentDataSource?.setAllowedCardNetworks(allowedCardNetworks)
    }
    fun setAllowedCardAuthMethods(allowedMethods: AllowedMethods) {
        paymentDataSource?.setAllowedCardAuthMethods(allowedMethods)
    }

    fun setCountryCode(countryCode: String) {
        paymentDataSource?.setCountryCode(countryCode)
    }

    fun initSDK(secretKeys :String , packageID: String){

    }
}