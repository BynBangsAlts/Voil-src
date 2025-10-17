package net.fabricmc.fabric.utils.encryption;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private final SecretKeySpec secretKey;

    public AESUtil(String staticKey) {
        byte[] decodedKey = Base64.getDecoder().decode(staticKey);
        if (decodedKey.length != 32) {
            throw new IllegalArgumentException(decodedKey.length + " bytes long.");
        }
        this.secretKey = new SecretKeySpec(decodedKey, "AES");
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, this.secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String base64EncryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        byte[] encryptedData = Base64.getDecoder().decode(base64EncryptedData);
        cipher.init(2, this.secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    public static String generateKey(String ip) throws Exception {
        long timeSegment = Instant.now().getEpochSecond() / 1800L;
        String input = ip + "a@jfa" + timeSegment;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec("a@jfa".getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
