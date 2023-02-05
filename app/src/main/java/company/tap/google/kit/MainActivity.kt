package company.tap.google.kit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import company.tap.google.pay.internal.interfaces.SDKDelegate
import company.tap.google.pay.open.DataConfiguration
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.GPayWalletMode
import java.math.BigDecimal

class MainActivity : AppCompatActivity() , SDKDelegate {
    var dataConfig: DataConfiguration = DataConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeSDK()
        configureSDKData()
    }

    private fun configureSDKData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        dataConfig.addSDKDelegate(this) //** Required **

        dataConfig.setEnvironmentMode(GPayWalletMode.ENVIRONMENT_TEST)

        dataConfig.setGatewayId("tappayments")

        dataConfig.setGatewayMerchantID("11230")

        dataConfig.setAmount(BigDecimal.valueOf(23))

        dataConfig.setAllowedCardAuthMethods(AllowedMethods.PAN_ONLY)

        dataConfig.setTransactionCurrency("AED")

        dataConfig.setCountryCode("AED")

        val SUPPORTED_NETWORKS = listOf(
            "AMEX",
            "DISCOVER",
            "JCB",
            "MASTERCARD",
            "VISA")
        dataConfig.setAllowedCardNetworks(SUPPORTED_NETWORKS)
    }

    private fun initializeSDK() {
        dataConfig.initSDK(this@MainActivity as Context,"sk_test_kovrMB0mupFJXfNZWx6Etg5y","company.tap.goSellSDKExample")

    }

    override fun onGooglePayToken() {
    }

    override fun onTapToken() {
    }

    override fun onFailed() {
    }
}