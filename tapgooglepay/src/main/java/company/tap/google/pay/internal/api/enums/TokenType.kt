package company.tap.google.pay.internal.api.enums

import com.google.gson.annotations.SerializedName

/**
 * The enum Token type.
 */
enum class TokenType {
    /**
     * Card token type.
     */
    @SerializedName("CARD")
    CARD,

    /**
     * Saved card token type.
     */
    @SerializedName("SAVED_CARD")
    SAVED_CARD
}