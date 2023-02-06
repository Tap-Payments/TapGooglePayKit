package company.tap.google.pay.internal.api

import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class Address(
    @SerializedName("format") var format: AddressFormat,

    @SerializedName("type") val type: AddressType,

    @SerializedName("country") val country: String,

    @SerializedName("line1") val line1: String,

    @SerializedName("line2")
    val line2: String,

    @SerializedName("city") val city: String,

    @SerializedName("state") val state: String,

    @SerializedName("zip_code")
    val zipCode: String,

    @SerializedName("country_governorate")
    val countryGovernorate: String,

    @SerializedName("area") val area: String,

    @SerializedName("block") val block: String,

    @SerializedName("avenue")
    val avenue: String,

    @SerializedName("street")
    val street: String,

    @SerializedName("building_house")
    val buildingHouse: String,

    @SerializedName("floor")
    val floor: String,

    @SerializedName("office")
    val office: String,

    @SerializedName("po_box")
    val poBox: String,

    @SerializedName("postal_code")
    val postalCode: String

) : Serializable
