# TapGooglePay™Kit
A standalone kit for handling Google Pay™

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://github.com/Tap-Payments/TapGooglePayKit/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://github.com/Tap-Payments/TapGooglePayKit/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-21-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-30-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)

https://user-images.githubusercontent.com/57221514/217261036-33b3367c-2d60-49f2-987e-776bc0b7e660.mov

# Table of Contents
---
1. [Requirements](#requirements)
2. [Installation](#installation)
    1. [Include TapGooglePayKit library as a dependency module in your project](#include_library_to_code_locally)
    2. [Installation with jitpack](#installation_with_jitpack)

3. [Setup](#setup)
    1. [TapGooglePay™Kit Class Properties](#setup_tapgoogle_pay_sdk_class_properties_secret_key)
    2. [TapGooglePay™Kit Button](#setup_tapgoogle_pay_button)
4. [Usage](#usage)
    1. [Configure SDK with Required Data](#configure_sdk_with_required_data)
    2. [Configure SDK Look and Feel](#configure_sdk_look_and_feel)
   
   
5. [SDKSession Delegate](#sdk_delegate)
    1. [OnTapToken Success Callback](#payment_success_callback)
    2. [OnFailure Callback](#payment_failure_callback)
    3. [OnGooglePay_Token_Success Callback](#authorization_success_callback)

6. [Additional_Configuration_ GooglePay](#additional_config_googlepay)

7. [Trouble_Shooting_ GooglePay](#trouble_shoot_googlepay)  

8. [Documentation](#docs)

<a name="requirements"></a>
# Requirements
---
To use the SDK the following requirements must be met:

1. **Android Studio 3.6** or newer
2. **Android SDK Tools 31** or newer
3. **Android Platform Version: API 31: Android 10  revision 7** or later
4. **Android targetSdkVersion: 31

<a name="installation"></a>
<a name="include_library_to_code_locally"></a>
### Include tapgooglepay library as a dependency module in your project
---
1. Clone checkoutSDK library from Tap repository
   ```
       https://github.com/Tap-Payments/TapGooglePayKit
    ```
2. Add goSellSDK library to your project settings.gradle file as following
    ```java
        include ':library', ':YourAppName'
    ```
3. Setup your project to include checkout as a dependency Module.
    1. File -> Project Structure -> Modules -> << your project name >>
    2. Dependencies -> click on **+** icon in the screen bottom -> add Module Dependency
    3. select checkout library

<a name="installation_with_jitpack"></a>
### Installation with JitPack
---
[JitPack](https://jitpack.io/) is a novel package repository for JVM and Android projects. It builds Git projects on demand and provides you with ready-to-use artifacts (jar, aar).

To integrate tapGooglePay™SDK into your project add it in your **root** `build.gradle` at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```java
	dependencies {
	        implementation 'com.github.Tap-Payments:TapGooglePayKit:0.0.33'
	}
```
<a name="setup"></a>
# Setup
---
First of all, `tapGooglePay™SDK` should be set up. In this section secret key and application ID are required.

<a name="setup_gosellsdk_class_properties"></a>
## tapGooglePay™SDK Class Properties
First of all, `tapGooglePay™SDK` should be set up. To set it up, add the following lines of code somewhere in your project and make sure they will be called before any usage of `tapGoooglePaySDK`.

Below is the list of properties in tapGooglePaySDK class you can manipulate. Make sure you do the setup before any usage of the SDK.

<a name="setup_tapgoogle_pay_sdk_class_properties_secret_key"></a>
### Secret Key and Application ID

To set it up, add the following line of code somewhere in your project and make sure it will be called before any usage of `checkOutSDK`, otherwise an exception will be thrown. **Required**.

*Java:*
```java
 dataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id");
```
*Kotlin:*
Here we need to make a Top level declaration
```kotlin
var dataConfig: DataConfiguration = DataConfiguration
```
```kotlin

dataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id")
 ```    
1. **`authToken`** - to authorize your requests.// Secret key (format: "sk_XXXXXXXXXXXXXXXXXXXXXXXX")
2. **`app_id`** - replace it using your application ID "Application main package".

<a name="setup_tapgoogle_pay_button"></a>
### SetUp Google Pay™ Button

Include the Google Pay™ button view inside the xml file as below

*Kotlin*

```kotlin
        <company.tap.google.pay.open.GooglePayButton
         android:id="@+id/googlePayView"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:layout_alignParentBottom="true"
        android:enabled="true"
        android:focusable="true"
/>
```
Then declare it in the class as follows:
1. In Top level add 

*Kotlin*
  
   ```kotlin
   lateinit var googlePayView: GooglePayButton
   ```

2. Set the Button Type you prefer from the list of enums we have

*Kotlin*
  
   ```kotlin
    googlePayView = findViewById(R.id.googlePayView)
    googlePayView.setGooglePayButtonType(GooglePayButtonType.CHECKOUT_WITH_GOOGLE_PAY)
   ```

3. Set The click event for the Google Pay™ view as below:

*Kotlin*
   
```kotlin
    googlePayView.buttonView.setOnClickListener {
   Here you choose what you prefer to call getGooglePayToken or getTapToken
            if(defaultPref.toString() == "GET GOOGLEPAY TOKEN"){
                dataConfig.getGooglePayToken(this, googlePayView)

            }else if(defaultPref.toString() == "GET TAP TOKEN"){
                dataConfig.getTapToken(this, googlePayView)
            }

        }
   ``` 



<a name="setup_tap_google_pay_sdk_class_properties_mode"></a>
### Mode

SDK mode defines which mode SDK is operating in, either **sandbox** or **production**.

SDK Mode is automatically identified in the backend based on the secrete key you defined earlier in setup process.

<a name="usage"></a>
#Usage
---
<a name="configure_sdk_with_required_data"></a>
### Configure SDK With Required Data

`tapGooglePay™SDK` should be set up. To set it up, add the following lines of code somewhere in your project and make sure they will be called before any usage of `tapGoooglePaySDK`.

*Kotlin:*

```kotlin

     

        /**
         * Required step.
         * Configure SDK with your choice from the given list.
         */   
            initializeSDK()
            
         /**
         * Required step.
         * Configure SDK Session with all required data.
         */
        configureSDKData()
            
 ```           
        
<a name="setup_tapGoooglePaySDK_class_properties_secret_key"></a>
### Configure SDK Secret Key and Application ID and SDK Language

To set it up, add the following line of code somewhere in your project and make sure it will be called before any usage of `tapGoooglePaySDK`, otherwise an exception will be thrown. **Required**.

*Kotlin:*
```kotlin
        /**
         * Required step.
         * Configure SDK with your Secret API key and App Bundle name registered with tap company.
         */
        private fun initializeSDK(){
           dataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id")

        }
```
1. **`authToken`** - to authorize your requests.// Secret key (format: "sk_XXXXXXXXXXXXXXXXXXXXXXXX")
2. **`app_id`** - replace it using your application ID "Application main package".


<a name="configure_sdk_Session"></a>
## Configure SDK Data
**DATACONFIGURATION** is the main interface for  library from you application
### Properties

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
    <th rowspan=1>Description</th>


   <tr>
	<td> sdkDelegate  </td>
	<td> Activity </td>
	<td> Activity. it is used to notify Merchant application with all SDK Events </td>
   <tr>


</table>

### Methods

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
    <tr>
	 <td> addSDKDelegate  </td>
	 <td> pass your activity that implements SdkDelegate interface . you have to override all methods available through this interface </td>
    </tr>
      <tr>
   	 <td> setTransactionCurrency  </td>
   	 <td> Set the transaction currency associated to your account. Transaction currency must be of type TapCurrency("currency_iso_code"). i.e new TapCurrency("KWD") </td>
    </tr>
    <tr>
	 <td> setEnvironmentMode  </td>
	 <td> SDK offers different environment modes such as [ TEST - PRODUCTION]   </td>
    </tr>
    <tr>
	 <td> setAmount </td>
	 <td> Set Total Amount. Amount value must be of type BigDecimal i.e new BigDecimal(40) </td>
    </tr>
    <tr>
	 <td> setGatewayId </td>
	 <td> Gateway id required to use TAP as PSP . Here it is tappayments</td>
    </tr>
    <tr>
  	 <td> setGatewayMerchantID </td>
  	 <td> MerchantID available with TAP</td>
  	</tr>
  	<tr>
  	 <td> setAllowedCardAuthMethods </td>
  	 <td> ALLOWED CARD auth methods here it is PAN_ONLY, CRYPTOGRAM_3DS , ALL</td>
  	</tr>
  	<tr>
  	 <td> setCountryCode </td>
  	 <td> Set country code. </td>
  	</tr>
  	<tr>
  	 <td> setAllowedCardNetworks </td>
  	 <td> CARD networks like VISA, AMEX, JCB ,MADA. </td>
  	</tr>
  	
</table>

**Configure SDK DATA Example** 

*Kotlin:*
```kotlin
 private fun configureSDKData() {
   // pass your activity as a session delegate to listen to SDK internal payment process follow
   dataConfig.addSDKDelegate(this) //** Required **
   
   dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST) //**Required SDK MODE**/

   dataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/

   dataConfig.setGatewayMerchantID("1124340") //**Required GATEWAY Merchant ID**/
   
    dataConfig.setAmount(BigDecimal.valueOf(23))  //**Required Amount**/

   settingsManager?.getAllowedMethods("allowed_card_auth_key")
      ?.let { dataConfig.setAllowedCardAuthMethods(it) } //**Required type of auth PAN_ONLY, CRYPTOGRAM , ALL**/


   settingsManager?.getString("key_currency_code","USD")
      ?.let { dataConfig.setTransactionCurrency(it) } //**Required Currency **/

   settingsManager?.getString("country_code_key","US")?.let { dataConfig.setCountryCode(it) } //**Required Country **/

  
   dataConfig.setAllowedCardNetworks(settingsManager?.getSet("key_payment_networks")?.toMutableList()) //**Required Payment Networks you want google to display for you **/
}
 ```

<a name="sdk_open_interfaces"></a>
## SDK Open Interfaces
 SDK open Interfaces available for implementation through Merchant Project:

1. SessionDelegate
```kotlin
  fun onGooglePayToken(token:String)
  fun onTapToken(token: Token)
  fun onFailed(error:String)
```

<a name="sdk_open_enums"></a>
## SDK Open ENUMs
SDK open Enums available for implementation through Merchant Project:
1. AllowedMethods

Choose your allowed methods like PAN,CRYPTO etc

```kotlin
enum class AllowedMethods {
   PAN_ONLY,
   CRYPTOGRAM_3DS,ALL
}
```

2.SdkMode

Setup the mode you want to test in

```kotlin
enum class SDKMode {
   /**
    * Sandbox is for testing purposes
    */

   ENVIRONMENT_TEST,

   /**
    * Production is for live
    */

   ENVIRONMENT_PRODUCTION
}
```
3.GooglePayButtonType

Choose the type of button you wish to use in your app

```kotlin
enum class GooglePayButtonType {
   BUY_WITH_GOOGLE_PAY,

   DONATE_WITH_GOOGLE_PAY,

   NORMAL_GOOGLE_PAY,

   PAY_WITH_GOOGLE_PAY,

   SUBSCRIBE_WITH_GOOGLE_PAY,

   CHECKOUT_WITH_GOOGLE_PAY,

   ORDER_WITH_GOOGLE_PAY,

   BOOK_WITH_GOOGLE_PAY
}
```


## SDK Delegate

**SDK Delegate** is an interface which you may want to implement to receive payment/authorization/card saving status updates and update your user interface accordingly when payment window closes.
Below are listed down all available callbacks:

<a name="google_pay_success_callback"></a>
### GooglePay Token Success Callback

Notifies the receiver that googlepay token has succeed.

#### Declaration
*Kotlin:*
```kotlin
-  fun onGooglePayToken(token:String)
```
#### Arguments

**token**: Successful Token object.

<a name="tap_token_success_callback"></a>
### TAP Token Success Callback

Notifies the receiver that token generated for TAP .
#### Declaration

*Kotlin:*
```kotlin
-  fun onTapToken(token: Token)
```

#### Arguments

**token**: Token object from TAP.

<a name=failed_callback"></a>
### Failure Callback

Notifies the receiver that failed.
#### Declaration

*Kotlin:*

```kotlin
- fun onFailed(error:String)
```

#### Arguments
**error**: Failure object.

<a name="additional_config_googlepay"></a>
# Additional Data Configuration
To use GooglePay in your app , you will be required to do additional configuration as follows:

1. In build.gradle file add

```kotlin
implementation "com.google.android.gms:play-services-wallet:18.1.3"
```

2. Ensure your min sdk is 21
```kotlin
minSdk 21
targetSdk 32
```

3. In Manifest file , Inside the <application tag do the below:

```kotlin
<meta-data
    android:name="com.google.android.gsm.wallet.api.enabled"
    android:value="true"
    />
```

<a name="trouble_shoot_googlepay"></a>
# Trouble shoot GooglePay

You might encounter the following errors at some point in your integration. This list provides some helpful troubleshooting advice should these errors arise.

1. This merchant is not enabled for Google Pay
The Google Pay API requires a Google merchantId for sites that configure PaymentsClient for a PRODUCTION environment. A Google merchantId is associated with one or more fully qualified domains through the Google Pay and Wallet Console. Check the returned error details for more information.

2. This merchant has not completed registration to use Google Pay API. Please go to console (https://pay.google.com/business/console) to verify.
You haven't completed the process to register your apps for the Google Pay API. Review Request production access to register using the Google Pay and Wallet Console and request a review of your app's use of the Google Pay API.

3. This merchant profile does not have access to this feature
Google hasn't configured your app to use the Google Pay API. Review Request production access to request a review of your app's use of the Google Pay API via the Google Pay and Wallet Console.
This Google Pay API integration is disabled. Please contact us for more information (https://developers.google.com/pay/api/faq#how-to-get-support).
Contact us to learn more about the required steps to re-enable the Google Pay API for your Google Account.



<a name="docs"></a>
# Documentation
Documentation is available at [github-pages][2].<br>
Also documented sources are attached to the library.
[1]:https://www.tap.company/developers/
