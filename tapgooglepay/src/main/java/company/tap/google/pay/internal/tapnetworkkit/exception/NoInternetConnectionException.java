package company.tap.google.pay.internal.tapnetworkkit.exception;

import android.content.Context;

import java.io.IOException;

import company.tap.google.pay.R;


public class NoInternetConnectionException extends IOException {

    private Context context;

    public NoInternetConnectionException(Context context) {
        this.context = context;
    }

    @Override
    public String getMessage() {
        return context.getResources().getString(R.string.internet_connectivity_message);
    }
}
