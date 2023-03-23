package company.tap.google.kit

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import company.tap.google.pay.internal.api.responses.Token
import company.tap.google.pay.open.TapDataConfiguration
import company.tap.google.pay.open.GooglePayButton
import company.tap.google.pay.open.SDKDelegate
import company.tap.google.pay.open.enums.GooglePayEnviroment
import java.math.BigDecimal

class MainActivity : AppCompatActivity() , SDKDelegate {
    var tapDataConfig: TapDataConfiguration = TapDataConfiguration //** Required**//

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
                tapDataConfig.getGooglePayToken(this, googlePayView)

            }else if(defaultPref.toString() == "GET TAP TOKEN"){
                tapDataConfig.getTapToken(this, googlePayView)
            }

        }
        initializeSDK()
        configureGooglePayData()
        configureTapData()
    }

    private fun configureGooglePayData() {
        googlePayView.setGooglePayData(GooglePayEnviroment.ENVIRONMENT_TEST,
            mutableListOf("PAN_ONLY", "CRYPTOGRAM_3DS"), mutableListOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"),"Total",
        BigDecimal(2),"KWD")
    }

    private fun configureTapData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        tapDataConfig.addSDKDelegate(this) //** Required **
        tapDataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/
        settingsManager?.getString("key_merchant_id", "1124340")
            ?.let { tapDataConfig.setGatewayMerchantID(it) } //**Required GATEWAY Merchant ID**/


    }

    private fun initializeSDK() {
        settingsManager?.getString("key_test_name", "sk_test_kovrMB0mupFJXfNZWx6Etg5y")?.let {
            tapDataConfig.initSDK(this@MainActivity as Context, it,
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