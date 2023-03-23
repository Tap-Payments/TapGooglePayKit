package company.tap.google.pay.internal

import androidx.annotation.RestrictTo
import company.tap.google.pay.open.enums.Authentication
import company.tap.google.pay.open.enums.GooglePayEnviroment
import java.math.BigDecimal
@RestrictTo(RestrictTo.Scope.LIBRARY)
object PaymentDataSource : company.tap.google.pay.internal.interfaces.PaymentDataSource {
    private var currency: String? = null
    private var amount: BigDecimal? = null
    private lateinit var gatewayMerchantId: String
    private lateinit var gatewayId: String
    private lateinit var googlePayEnviroment: GooglePayEnviroment
    private lateinit var authentication: MutableList<String>
   // private lateinit var allowedCardNetworks: ArrayList<String>
    private var allowedCardNetworks: MutableList<String>? = java.util.ArrayList()
    private lateinit var countryCode: String
    /**
     * Set transaction currency.
     *
     * @param transactionCurrency the tap currency
     */
    fun setTransactionCurrency(transactionCurrency: String) {
        currency = transactionCurrency
    }
    /**
     * Set amount.
     *
     * @param amount the amount
     */
    fun setAmount(amount: BigDecimal?) {
        this.amount = amount
    }
    fun setEnvironmentMode(googlePayEnviroment: GooglePayEnviroment) {
        this.googlePayEnviroment = googlePayEnviroment
    }

    fun setGatewayId(gatewayId: String) {
        this.gatewayId = gatewayId
    }
    fun setGatewayMerchantId(gatewayMerchantId: String) {
        this.gatewayMerchantId = gatewayMerchantId
    }
    fun setAllowedCardNetworks(allowedCardNetworks: MutableList<String>?) {
        this.allowedCardNetworks = allowedCardNetworks
    }

    fun setAllowedCardAuthMethods(authentication:  MutableList<String>?) {
        if (authentication != null) {
            this.authentication = authentication
        }
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }
//////////////////////////////Getters///////////////////////
    override fun getCurrency(): String? {
        return currency
    }

    override fun getAmount(): BigDecimal? {
        return amount
    }

    override fun getEnvironment(): GooglePayEnviroment? {
       return googlePayEnviroment
    }

    override fun getAllowedCardMethod(): MutableList<String> {
      return authentication
    }

    override fun getAllowedNetworks(): MutableList<String>? {
        return allowedCardNetworks
    }

    override fun getGatewayId(): String {
       return gatewayId
    }

    override fun getGatewayMerchantId(): String {
       return gatewayMerchantId
    }

    override fun getCountryCode(): String {
       return countryCode
    }
}