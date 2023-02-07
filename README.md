# TapGooglePayKit
A standalone kit for handling google pay

[![Platform](https://img.shields.io/badge/platform-Android-inactive.svg?style=flat)](https://github.com/Tap-Payments/TapGooglePayKit/)
[![Documentation](https://img.shields.io/badge/documentation-100%25-bright%20green.svg)](https://github.com/Tap-Payments/TapGooglePayKit/)
[![SDK Version](https://img.shields.io/badge/minSdkVersion-21-blue.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)
[![SDK Version](https://img.shields.io/badge/targetSdkVersion-30-informational.svg)](https://stuff.mit.edu/afs/sipb/project/android/docs/reference/packages.html)

CheckoutSDK Android compatible version of CheckoutSDK library that fully covers payment/authorization/card saving process inside your Android application.
# Table of Contents
---
1. [Requirements](#requirements)
2. [Installation](#installation)
    1. [Include CheckoutSDK library as a dependency module in your project](#include_library_to_code_locally)
    2. [Installation with jitpack](#installation_with_jitpack)

3. [Setup](#setup)
    1. [CheckoutSDK Class Properties](#setup_checkoutsdk_class_properties)
4. [Usage](#usage)
    1. [Configure SDK with Required Data](#configure_sdk_with_required_data)
    2. [Configure SDK Look and Feel](#configure_sdk_look_and_feel)
   
   
5. [SDKSession Delegate](#sdk_delegate)
    1. [Payment Success Callback](#payment_success_callback)
    2. [Payment Failure Callback](#payment_failure_callback)
    3. [Authorization Success Callback](#authorization_success_callback)
   
6. [Documentation](#docs)

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

To integrate goSellSDK into your project add it in your **root** `build.gradle` at the end of repositories:
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
First of all, `tapGoooglePaySDK` should be set up. In this section secret key and application ID are required.

<a name="setup_gosellsdk_class_properties"></a>
## checkOutSDK Class Properties
First of all, `tapGoooglePaySDK` should be set up. To set it up, add the following lines of code somewhere in your project and make sure they will be called before any usage of `checkOutSDK`.

Below is the list of properties in checkOutSDK class you can manipulate. Make sure you do the setup before any usage of the SDK.

<a name="setup_checkoutsdk_class_properties_secret_key"></a>
### Secret Key and Application ID

To set it up, add the following line of code somewhere in your project and make sure it will be called before any usage of `checkOutSDK`, otherwise an exception will be thrown. **Required**.

*Java:*
```java
 dataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id");
```
*Kotlin:*
```kotlin
var dataConfig: DataConfiguration = DataConfiguration
dataConfig.initSDK(this@MainActivity as Context,"sk_test_kXXXXXXXXXXXXXXXXXXXXXXXX","app_id")
 ```    
1. **`authToken`** - to authorize your requests.// Secret key (format: "sk_XXXXXXXXXXXXXXXXXXXXXXXX")
2. **`app_id`** - replace it using your application ID "Application main package".

<a name="setup_checkoutsdk_class_properties_mode"></a>
### Mode

SDK mode defines which mode SDK is operating in, either **sandbox** or **production**.

SDK Mode is automatically identified in the backend based on the secrete key you defined earlier in setup process.

<a name="usage"></a>
#Usage
---
<a name="configure_sdk_with_required_data"></a>
### Configure SDK With Required Data

`checkOutSDK` should be set up. To set it up, add the following lines of code somewhere in your project and make sure they will be called before any usage of `checkOutSDK`.
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
            
            
        
<a name="setup_checkoutsdk_class_properties_secret_key"></a>
### Configure SDK Secret Key and Application ID and SDK Language

To set it up, add the following line of code somewhere in your project and make sure it will be called before any usage of `checkOutSDK`, otherwise an exception will be thrown. **Required**.

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
## Configure SDK Session
**SDKSession** is the main interface for  library from you application, so you can use it to start SDK with pay button or without pay button.
### Properties

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
    <th rowspan=1>Description</th>


   <tr>
	<td> sessionDelegate  </td>
	<td> Activity </td>
	<td> Activity. it is used to notify Merchant application with all SDK Events </td>
   <tr>


</table>

### Methods

<table style="text-align:center">
    <th colspan=1>Property</th>
    <th colspan=1>Type</th>
    <tr>
	 <td> addSessionDelegate  </td>
	 <td> pass your activity that implements SessionDelegate interface . you have to override all methods available through this interface </td>
    </tr>
    <tr>
	 <td> instantiatePaymentDataSource  </td>
	 <td> Payment Data Source Object is the main object that is responsible of holding all data required from our backend to return all payment options [ Debit Cards - Credit Cards ] available for this merchant . </td>
    </tr>
    <tr>
   	 <td> setTransactionCurrency  </td>
   	 <td> Set the transaction currency associated to your account. Transaction currency must be of type TapCurrency("currency_iso_code"). i.e new TapCurrency("KWD") </td>
    </tr>
    <tr>
	 <td> setTransactionMode  </td>
	 <td> SDK offers different transaction modes such as [ TransactionMode.PURCHASE - TransactionMode.AUTHORIZE_CAPTURE - TransactionMode.SAVE_CARD - TransactionMode.TOKENIZE_CARD]   </td>
    </tr>
    <tr>
	 <td> setCustomer </td>
	 <td> Pass your customer data. Customer must be of type Tap Customer. You can create Tap Customer as following
	  TapCustomer(
                "cust_id", "cust_firstname", "cust_middlename",
                "cust_lastname", "cust_email",
                PhoneNumber("country_code", "MobileNo"), "metdata",
            )</td>
    </tr>
    <tr>
	 <td> setAmount </td>
	 <td> Set Total Amount. Amount value must be of type BigDecimal i.e new BigDecimal(40) </td>
    </tr>
    <tr>
	 <td> setPaymentItems </td>
	 <td> ArrayList that contains payment items. each item of this array must be of type PaymentItem. in case of SAVE_CARD or TOKENIZE_CARD you can pass it null</td>
    </tr>
    <tr>
  	 <td> setTaxes </td>
  	 <td> ArrayList that contains Tax items. each item of this array must be of type Tax. in case of SAVE_CARD or TOKENIZE_CARD you can pass it null</td>
  	</tr>
  	<tr>
  	 <td> setShipping </td>
  	 <td> ArrayList that contains Shipping items. each item of this array must be of type Shipping. in case of SAVE_CARD or TOKENIZE_CARD you can pass it null</td>
  	</tr>
  	<tr>
  	 <td> setPostURL </td>
  	 <td> POST URL. </td>
  	</tr>
  	<tr>
  	 <td> setPaymentDescription </td>
  	 <td> Payment description. </td>
  	</tr>
  	<tr>
  	 <td> setPaymentMetadata </td>
  	 <td> HashMap that contains any other payment related data. </td>
  	</tr>
  	<tr>
  	 <td> setPaymentReference </td>
  	 <td> Payment reference. it must be of type Reference object or null </td>
  	</tr>
  	<tr>
  	 <td> setPaymentStatementDescriptor </td>
  	 <td> Payment Statement Description </td>
  	</tr>
  	<tr>
  	 <td> isRequires3DSecure </td>
  	 <td> Enable or Disable 3D Secure </td>
  	</tr>
  	<tr>
  	 <td> setReceiptSettings </td>
  	 <td> Identify Receipt Settings. You must pass  Receipt object or null </td>
  	</tr>
  	<tr>
  	 <td> setAuthorizeAction </td>
  	 <td> Identify AuthorizeAction. You must pass AuthorizeAction object or null </td>
  	</tr>
  	<tr>
  	 <td> setDestination </td>
  	 <td> Identify Array of destination. You must pass Destinations object or null </td>
  	</tr>
  
</table> private fun configureSDKData() {
        // pass your activity as a session delegate to listen to SDK internal payment process follow
        dataConfig.addSDKDelegate(this) //** Required **

      //  dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST)
        settingsManager?.getSDKMode("key_sdkmode")?.let { dataConfig.setEnvironmentMode(it) } //**Required SDK MODE**/

        dataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/

        dataConfig.setGatewayMerchantID("1124340") //**Required GATEWAY Merchant ID**/
        settingsManager?.getString("key_amount_name", "23")?.let { BigDecimal(it) }?.let {
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

        //println("settings are"+settingsManager?.getSet("key_payment_networks")) 

//        val SUPPORTED_NETWORKS = mutableListOf<String>(
//            "AMEX",
//            "MASTERCARD",
//            "VISA")

        dataConfig.setAllowedCardNetworks(settingsManager?.getSet("key_payment_networks")?.toMutableList()) //**Required Payment Networks **/
    }
**Configure SDK DATA Example** 

*Kotlin:*
```kotlin
 private fun configureSDKData() {
   // pass your activity as a session delegate to listen to SDK internal payment process follow
   dataConfig.addSDKDelegate(this) //** Required **

   //  dataConfig.setEnvironmentMode(SDKMode.ENVIRONMENT_TEST)
   settingsManager?.getSDKMode("key_sdkmode")?.let { dataConfig.setEnvironmentMode(it) } //**Required SDK MODE**/

   dataConfig.setGatewayId("tappayments")  //**Required GATEWAY ID**/

   dataConfig.setGatewayMerchantID("1124340") //**Required GATEWAY Merchant ID**/
   settingsManager?.getString("key_amount_name", "23")?.let { BigDecimal(it) }?.let {
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

   //println("settings are"+settingsManager?.getSet("key_payment_networks")) 

//        val SUPPORTED_NETWORKS = mutableListOf<String>(
//            "AMEX",
//            "MASTERCARD",
//            "VISA")

   dataConfig.setAllowedCardNetworks(settingsManager?.getSet("key_payment_networks")?.toMutableList()) //**Required Payment Networks **/
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
```kotlin
enum class AllowedMethods {
   PAN_ONLY,
   CRYPTOGRAM_3DS,ALL
}
```

2.SdkMode
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

<a name="docs"></a>
# Documentation
Documentation is available at [github-pages][2].<br>
Also documented sources are attached to the library.
[1]:https://www.tap.company/developers/
