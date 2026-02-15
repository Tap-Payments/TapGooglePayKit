package company.tap.google.pay.internal.tapnetworkkit.utils;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import company.tap.google.pay.internal.tapnetworkkit.utils.EmptyStringToEncryptException;

/**
 * Created by on 16.03.2023.
 * <br>
 * Util class for RSA encryption
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class CryptoUtil {
    /**
     * Encrypt json string string.
     *
     * @param jsonString    the json string
     * @param encryptionKey the encryption key
     * @return the string
     */
    public static String encryptJsonString(@NonNull String jsonString, String encryptionKey) {
        if(jsonString.length() == 0) {
            throw new EmptyStringToEncryptException();
        }

        return encrypt(jsonString, encryptionKey);
    }

    private static String encrypt(String encrypt, String encryptionKey) {
        String result = "";
        try {
            encryptionKey = encryptionKey.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");

            byte[] publicBytes = Base64.decode(encryptionKey, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Cipher encryptCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pubKey);
            result = Base64.encodeToString(encryptCipher.doFinal(encrypt.getBytes()), Base64.DEFAULT).replaceAll("\\n", "");
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
