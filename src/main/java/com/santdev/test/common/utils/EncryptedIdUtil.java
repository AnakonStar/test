package com.santdev.test.common.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.santdev.test.common.exception.InvalidEncryptedIdException;
import com.santdev.test.common.security.SecurityConstants;

@Component
public class EncryptedIdUtil {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    public String encrypt(String table, Long id) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), new GCMParameterSpec(TAG_LENGTH_BITS, iv));

            String payload = normalizeTable(table) + ":" + id;
            byte[] encrypted = cipher.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(buffer.array());
        } catch (Exception exception) {
            throw new InvalidEncryptedIdException();
        }
    }

    public Long decrypt(String encryptedId, String expectedTable) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedId);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);

            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new GCMParameterSpec(TAG_LENGTH_BITS, iv));

            String payload = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
            String[] parts = payload.split(":", 2);

            if (parts.length != 2 || !parts[0].equals(normalizeTable(expectedTable))) {
                throw new InvalidEncryptedIdException();
            }

            return Long.valueOf(parts[1]);
        } catch (Exception exception) {
            throw new InvalidEncryptedIdException();
        }
    }

    private SecretKey getSecretKey() throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256")
                .digest(SecurityConstants.SECRET.getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(Arrays.copyOf(digest, 32), "AES");
    }

    private String normalizeTable(String table) {
        if (table == null || table.isBlank()) {
            throw new InvalidEncryptedIdException();
        }

        return table.trim().toLowerCase();
    }
}
