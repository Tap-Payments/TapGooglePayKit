package company.tap.google.pay.internal.tapnetworkkit.exception;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorObject {
    @SerializedName("reason")
        @Expose
        private String reason;
        @SerializedName("message")
        @Expose
        private String message;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

