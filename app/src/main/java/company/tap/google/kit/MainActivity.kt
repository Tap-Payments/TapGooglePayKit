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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import company.tap.google.pay.internal.api.responses.Token
import company.tap.google.pay.open.SDKDelegate
import company.tap.google.pay.open.DataConfiguration
import company.tap.google.pay.open.GooglePayButton

import java.math.BigDecimal

class MainActivity : AppCompatActivity() , SDKDelegate {
    var dataConfig: DataConfiguration = DataConfiguration //** Required**//
    lateinit var googlePayView: GooglePayButton
    lateinit var googlePayButton: View
    private  val TAG = "MainActivity"
    private var settingsManager: SettingsManager? = null

   

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settingsManager = SettingsManager
        settingsManager?.setPref(this)
        googlePayView = findViewById(R.id.googlePayView)
        val defaultPref=    settingsManager?.getString("token_type_key","GET GOOGLEPAY TOKEN")
       // googlePayView.googlePayBuyWith?.visibility=View.VISIBLE
        googlePayView.setGooglePayButtonType(settingsManager?.getGPAYButtonType("button_type_key"))

        googlePayView.buttonView.setOnClickListener {
            if(defaultPref.toString() == "GET GOOGLEPAY TOKEN"){
                dataConfig.getGooglePayToken(this, googlePayView)

            }else if(defaultPref.toString() == "GET TAP TOKEN"){
                dataConfig.getTapToken(this, googlePayView)
            }

        }
        initializeSDK()
        configureSDKData()
    }

    private fun configureSDKData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        dataConfig.addSDKDelegate(this) //** Required **

      //  dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST)
        settingsManager?.getSDKMode("key_sdkmode")?.let { dataConfig.setEnvironmentMode(it) } //**Required SDK MODE**/

        dataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/

        settingsManager?.getString("key_merchant_id", "1124340")
            ?.let { dataConfig.setGatewayMerchantID(it) } //**Required GATEWAY Merchant ID**/

        settingsManager?.getString("key_amount_name", "23.7")?.let { BigDecimal(it) }?.let {
            dataConfig.setAmount(
                      it
                )
             } //**Required Amount**/
       // dataConfig.setAmount(BigDecimal.valueOf(23))

        settingsManager?.getAllowedMethods("allowed_card_auth_key")
            ?.let { dataConfig.setAllowedCardAuthMethods(it) } //**Required type of auth PAN_ONLY, CRYPTOGRAM , ALL**/


        settingsManager?.getString("key_currency_code","USD")
            ?.let { dataConfig.setTransactionCurrency(it) } //**Required Currency **/

        settingsManager?.getString("country_code_key","US")?.let { dataConfig.setCountryCode(it) } //**Required Country **/

        println("settings are"+settingsManager?.getSet("key_payment_networks"))

//        val SUPPORTED_NETWORKS = mutableListOf<String>(
//            "AMEX",
//            "MASTERCARD",
//            "VISA")

        dataConfig.setAllowedCardNetworks(settingsManager?.getSet("key_payment_networks")?.toMutableList()) //**Required Payment Networks **/
    }

    private fun initializeSDK() {
        settingsManager?.getString("key_test_name", "sk_test_kovrMB0mupFJXfNZWx6Etg5y")?.let {
            dataConfig.initSDK(this@MainActivity as Context, it,
                settingsManager?.getString("key_package_name", "company.tap.goSellSDKExample")!!
            )
        }

    }

    override fun onGooglePayToken(token: String) {
        customAlertBox("onGooglePayToken",token)
       // Toast.makeText(this, token, Toast.LENGTH_SHORT).show()

    }


    override fun onTapToken(token: Token) {
        customAlertBox("onTapToken",token.id.toString())
        println("onTapToken"+token.id)
       // Toast.makeText(this, "onTapToken"+token.id, Toast.LENGTH_SHORT).show()
    }

    override fun onFailed(error:String) {
        customAlertBox("onFailed",error)

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
    private fun customAlertBox(title:String,message:String){
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(this)

        // Set the message show for the Alert time
        builder.setMessage(message)

        // Set Alert Title
        builder.setTitle(title)

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes") {
            // When the user click yes button then app will close
                dialog, which -> dialog.dismiss()
        }

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }



}