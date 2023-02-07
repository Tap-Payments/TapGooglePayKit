package company.tap.google.kit

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import company.tap.google.pay.internal.api.responses.Token
import company.tap.google.pay.internal.interfaces.SDKDelegate
import company.tap.google.pay.open.DataConfiguration
import company.tap.google.pay.open.enums.GooglePayButtonType
import java.math.BigDecimal

class MainActivity : AppCompatActivity() , SDKDelegate {
    var dataConfig: DataConfiguration = DataConfiguration
    lateinit var googlePayView: View
    lateinit var googlePayButton: View
   // lateinit var mainView: View
    private  val TAG = "MainActivity"
    private var settingsManager: SettingsManager? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingsManager = SettingsManager
        settingsManager?.setPref(this)
        googlePayView = findViewById(R.id.googlePayView)
       // mainView = googlePayView.rootView.findViewById(R.id.googlePayView)
       googlePayButton = googlePayView.rootView.findViewById(company.tap.google.pay.R.id.gPay)


        googlePayButton.setOnClickListener {
            dataConfig.startGooglePay(this, googlePayView,GooglePayButtonType.NORMAL_GOOGLE_PAY)

        }

        initializeSDK()
        configureSDKData()
    }

    private fun configureSDKData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        dataConfig.addSDKDelegate(this) //** Required **

      //  dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST)
        settingsManager?.getSDKMode("key_sdkmode")?.let { dataConfig.setEnvironmentMode(it) }

        dataConfig.setGatewayId("tappayments")

        dataConfig.setGatewayMerchantID("1124340")
        settingsManager?.getString("key_amount_name", "23")?.let { BigDecimal(it) }?.let {
            dataConfig.setAmount(
                      it
                )
             }
       // dataConfig.setAmount(BigDecimal.valueOf(23))

        settingsManager?.getAllowedMethods("allowed_card_auth_key")
            ?.let { dataConfig.setAllowedCardAuthMethods(it) }


        settingsManager?.getString("key_currency_code","USD")
            ?.let { dataConfig.setTransactionCurrency(it) }

        settingsManager?.getString("country_code_key","US")?.let { dataConfig.setCountryCode(it) }

        println("settings are"+settingsManager?.getSet("key_payment_networks"))

//        val SUPPORTED_NETWORKS = mutableListOf<String>(
//            "AMEX",
//            "MASTERCARD",
//            "VISA")

        dataConfig.setAllowedCardNetworks(settingsManager?.getSet("key_payment_networks")?.toMutableList())
    }

    private fun initializeSDK() {
        dataConfig.initSDK(this@MainActivity as Context,"sk_test_kovrMB0mupFJXfNZWx6Etg5y","company.tap.goSellSDKExample")

    }

    override fun onGooglePayToken(token: String) {
        Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

    }


    override fun onTapToken(token: Token) {
        Toast.makeText(this, "onTapToken"+token.id, Toast.LENGTH_SHORT).show()
    }

    override fun onFailed(error:String) {
        Log.e(TAG, "onFailed:$error")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menumain, menu)
        return true
    }
    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            finish()
            startActivity(intent)


            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


}