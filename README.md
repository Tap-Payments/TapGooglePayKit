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
   [Installation with jitpack](#installation_with_jitpack)

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

7. [Documentation](#docs)

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
	        implementation ''
	}
```
<a name="setup"></a>
# Setup
---
First of all, `tapGooglePay™SDK` should be set up. In this section secret key and application ID are required.

<a name="setup_gosellsdk_class_properties"></a>

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
               tapDataConfig.getGooglePayToken(this, googlePayView)

            }else if(defaultPref.toString() == "GET TAP TOKEN"){
               tapDataConfig.getTapToken(this, googlePayView)
            }

        }
   ``` 

<a name="usage"></a>
### Usage
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
         * Configure Google Pay™ Data of your choice.
         */
        configureGooglePayData()
         /**
         * Required step.
         * Configure SDK Delegate with all required data.
         */
        configureTapData()
            
 ```           
## Configure Google Pay™ Data
**Google Pay™ DATA CONFIGURATION** is required to start a transaction with Google Pay™
### Properties

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
      <tr>
   	 <td> environment  </td>
   	 <td> The Google Pay™ environment to target.Note: in the "TEST" environment, fake payment credentials are returned.
      In order to use the "PRODUCTION" environment, your App must be registered with Google Pay. This can be done through the Google Pay Business Console.
     </td>
    </tr>
      <tr>
	 <td> authentication </td>
	 <td> Fields supported to authenticate a card transaction.
      1. PAN_ONLY: This authentication method is associated with payment cards stored on file with the user's Google Account. Returned payment data includes personal account number (PAN) with the expiration month and the expiration year.
      2. CRYPTOGRAM_3DS: This authentication method is associated with cards stored as Android device tokens. Returned payment data includes a 3-D Secure (3DS) cryptogram generated on the device. </td>
    </tr>
    <tr>
	 <td> supportedNetworks </td>
	 <td> One or more card networks that you support, also supported by the Google Pay™ API.
      AMEX ,DISCOVER ,INTERAC ,JCB ,MASTERCARD ,VISA</td>
    </tr>
    <tr>
  	 <td> amount </td>
  	 <td> Total monetary value of the transaction with an optional decimal precision of two decimal places.</td>
  	</tr>
  	<tr>
  	 <td> label </td>
  	 <td> Custom label for the total price within the display items.</td>
  	</tr>
  	<tr>
  	 <td> currency </td>
  	 <td> The ISO 4217 alphabetic currency code. </td>
  	</tr>


</table>


**Configure Google Pay™-DATA Example**

*Kotlin:*
```kotlin
 private fun configureGooglePayData() {
   googlePayView.setGooglePayData(GooglePayEnviroment.ENVIRONMENT_TEST,
      mutableListOf("PAN_ONLY", "CRYPTOGRAM_3DS"), mutableListOf("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA"),"Total",
      BigDecimal(2),"KWD")
}
 ```

## tapGooglePay™SDK Class Properties
First of all, `tapGooglePay™SDK` should be set up. To set it up, add the following lines of code somewhere in your project and make sure they will be called before any usage of `tapGoooglePaySDK`.

Below is the list of properties in tapGooglePaySDK class you can manipulate. Make sure you do the setup before any usage of the SDK.

<a name="setup_tapgoogle_pay_sdk_class_properties_secret_key"></a>
### Secret Key and Application ID

To set it up, add the following line of code somewhere in your project and make sure it will be called before any usage of `tapGoogle Pay™`, otherwise an exception will be thrown. **Required**.

*Java:*
```java
 tapDataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id");
```
*Kotlin:*
Here we need to make a Top level declaration
```kotlin
var tapDataConfig: TapDataConfiguration = TapDataConfiguration
```
```kotlin

tapDataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id")
 ```    
1. **`authToken`** - to authorize your requests.// Secret key (format: "sk_XXXXXXXXXXXXXXXXXXXXXXXX")
2. **`app_id`** - replace it using your application ID "Application main package".

<a name="configure_sdk_Session"></a>
## Configure TAP Data
**DATA CONFIGURATION** is the main interface for  library from you application

### Methods

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
    <tr>
	 <td> addSDKDelegate  </td>
	 <td> pass your activity that implements SdkDelegate interface . you have to override all methods available through this interface </td>
    </tr>
    <tr>
	 <td> setGatewayId </td>
	 <td> Gateway id required to use TAP as PSP . Here it is tappayments</td>
    </tr>
    <tr>
  	 <td> setGatewayMerchantID </td>
  	 <td> MerchantID available with TAP</td>
  	</tr>
</table>

**Configure TAP-DATA Example** 

*Kotlin:*
```kotlin
 private fun configureSDKData() {
   // pass your activity as a sdk delegate to listen to SDK internal payment process follow
   tapDataConfig.addSDKDelegate(this) //** Required **

   tapDataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/
  
   tapDataConfig.setGatewayMerchantID("1311313131")  //**Required GATEWAY Merchant ID**/

}
 ```


<a name="sdk_open_interfaces"></a>
## SDK Open Interfaces
 SDK open Interfaces available for implementation through Merchant Project:

1. SDKDelegate
```kotlin
  fun onGooglePayToken(token:String)
  fun onTapToken(token: Token)
  fun onFailed(error:String)
```

<a name="sdk_open_enums"></a>
## SDK Open ENUMs
SDK open Enums available for implementation through Merchant Project:

1.Google Pay™ Enviroment

Setup the mode you want to test in

```kotlin
enum class GooglePayEnviroment {
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
2.GooglePayButtonType

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
### Google Pay™ Token Success Callback

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
To use Google Pay™ in your app , you will be required to do additional configuration as follows:

1. In build.gradle file add

```kotlin
implementation "com.google.android.gms:play-services-wallet:18.1.3"
```

2. Ensure your min sdk is 24
```kotlin
minSdk 24
targetSdk 32
```

3. In Manifest file , Inside the <application tag do the below:

```kotlin
<meta-data
    android:name="com.google.android.gsm.wallet.api.enabled"
    android:value="true"
    />
```


<a name="docs"></a>
# Documentation
Documentation is available at [github-pages][2].<br>
Also documented sources are attached to the library.
[1]:https://www.tap.company/developers/
