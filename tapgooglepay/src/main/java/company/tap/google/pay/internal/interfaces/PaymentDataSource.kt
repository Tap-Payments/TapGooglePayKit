package company.tap.google.pay.internal.interfaces

import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.SDKMode
import java.math.BigDecimal

interface PaymentDataSource {
    /**
     * Transaction currency. @return the currency
     */
    fun getCurrency(): String?

    fun getAmount(): BigDecimal?

    fun getEnvironment(): SDKMode?

    fun getAllowedCardMethod(): AllowedMethods

    fun getAllowedNetworks(): List<String>

    fun getGatewayId(): String

    fun getGatewayMerchantId(): String

    fun getCountryCode(): String

}