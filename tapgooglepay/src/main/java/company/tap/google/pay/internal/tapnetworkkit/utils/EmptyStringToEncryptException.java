package company.tap.google.pay.internal.tapnetworkkit.utils;

/**
 * The type Empty string to encrypt exception.
 */
public class EmptyStringToEncryptException extends IllegalArgumentException{
    private static final String MESSAGE = "Parameter jsonString cannot be empty";

    /**
     * Instantiates a new Empty string to encrypt exception.
     */
    public EmptyStringToEncryptException() {
        super(MESSAGE);
    }
}
