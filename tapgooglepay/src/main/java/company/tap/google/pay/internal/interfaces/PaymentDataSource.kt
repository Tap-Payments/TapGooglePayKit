package company.tap.google.pay.internal.interfaces

import androidx.annotation.RestrictTo
import company.tap.google.pay.open.enums.GooglePayEnviroment
import java.math.BigDecimal
@RestrictTo(RestrictTo.Scope.LIBRARY)
interface PaymentDataSource {
    /**
     * Transaction currency. @return the currency
     */
    fun getCurrency(): String?

    fun getAmount(): BigDecimal?

    fun getEnvironment(): GooglePayEnviroment?

    fun getAllowedCardMethod(): MutableList<String>

    fun getAllowedNetworks(): MutableList<String>?

    fun getGatewayId(): String

    fun getGatewayMerchantId(): String

    fun getCountryCode(): String?

}