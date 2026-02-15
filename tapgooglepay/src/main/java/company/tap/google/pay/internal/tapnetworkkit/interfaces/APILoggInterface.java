package company.tap.google.pay.internal.tapnetworkkit.interfaces;


public interface APILoggInterface {
    /**
     * General Logging callback
     *
     * @param logs  representing a log event
     */
    void onLoggingEvent(String logs);
}
