package company.tap.google.pay.internal.api.responses

import androidx.annotation.RestrictTo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import company.tap.google.pay.internal.api.enums.Address
import java.io.Serializable

@RestrictTo(RestrictTo.Scope.LIBRARY)
data class TokenizedCard(
    @SerializedName("id") @Expose
    var id: String? = null,

    @SerializedName("object")
    @Expose
    val `object`: String? = null,

    @SerializedName("address")
    @Expose
    val address: Address? = null,

    @SerializedName("funding")
    @Expose
    val funding: String? = null,

    @SerializedName("fingerprint")
    @Expose
    val fingerprint: String? = null,

    @SerializedName("brand")
   // val brand: CardBrand? = null,
    val brand: String? = null,


    @SerializedName("exp_month")
    @Expose
    val expirationMonth: Int = 0,

    @SerializedName("exp_year")
    @Expose
    val expirationYear: Int = 0,

    @SerializedName("last_four")
    @Expose
    val lastFour: String? = null,


    @SerializedName("first_six")
    @Expose
    val firstSix: String? = null,


    @SerializedName("name")
    @Expose
    var name: String? = null
) : Serializable
