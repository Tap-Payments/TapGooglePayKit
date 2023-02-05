package company.tap.google.kit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import company.tap.google.pay.internal.interfaces.SDKDelegate
import company.tap.google.pay.open.DataConfiguration
import company.tap.google.pay.open.enums.AllowedMethods
import company.tap.google.pay.open.enums.SDKMode
import java.math.BigDecimal

class MainActivity : AppCompatActivity() , SDKDelegate {
    var dataConfig: DataConfiguration = DataConfiguration
    lateinit var checkGPay:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkGPay = findViewById(R.id.checkGPay)
        checkGPay.setOnClickListener {
            dataConfig.checkGooglePayStatus(activity = this)
        }
        initializeSDK()
        configureSDKData()
    }

    private fun configureSDKData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        dataConfig.addSDKDelegate(this) //** Required **

        dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST)

        dataConfig.setGatewayId("tappayments")

        dataConfig.setGatewayMerchantID("11230")

        dataConfig.setAmount(BigDecimal.valueOf(23))

        dataConfig.setAllowedCardAuthMethods(AllowedMethods.CRYPTOGRAM_3DS)

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