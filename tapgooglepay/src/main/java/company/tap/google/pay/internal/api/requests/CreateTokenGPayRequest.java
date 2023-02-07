package company.tap.google.pay.internal.api.requests;

import androidx.annotation.RestrictTo;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class CreateTokenGPayRequest implements Serializable {
    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("token_data")
    @Expose
    private JsonObject tokenData;

    public CreateTokenGPayRequest(String type, JsonObject tokenData) {
        this.type = type;
        this.tokenData = tokenData;
    }

    /**
     * The type Builder.
     */
    public final static class Builder {
        private CreateTokenGPayRequest createTokenGPayRequest;

        /**
         * Instantiates a new Builder.
         *
         * @param type  the type
         * @param tokenData the tokenData
         */
        public Builder(String type, JsonObject tokenData) {
            createTokenGPayRequest = new CreateTokenGPayRequest(type, tokenData);
        }

        /**
         * Build create otp verification request.
         *
         * @return the createTokenGPayRequest request
         */
        public CreateTokenGPayRequest build() {
            return createTokenGPayRequest;
        }
    }



}
