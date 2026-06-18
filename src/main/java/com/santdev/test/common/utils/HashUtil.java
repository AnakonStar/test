package com.santdev.test.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class HashUtil {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private HashUtil() {
        throw new IllegalStateException(
                "Utility class");
    }

    /*
     * ==================================================
     * PASSWORD HASH
     * ==================================================
     */

    public static String hashPassword(
            String password) {

        try {

            byte[] salt = new byte[16];

            RANDOM.nextBytes(
                    salt);

            byte[] hash = pbkdf2(
                    password.toCharArray(),
                    salt);

            String saltHex = HexFormat.of().formatHex(
                    salt);

            String hashHex = HexFormat.of().formatHex(
                    hash);

            return saltHex + ":" + hashHex;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error hashing password",
                    e);
        }
    }

    /*
     * ==================================================
     * PASSWORD COMPARE
     * ==================================================
     */

    public static boolean comparePasswords(

            String password,
            String hashedPassword

    ) {

        try {

            String[] parts = hashedPassword.split(":");

            String saltHex = parts[0];

            String originalHashHex = parts[1];

            byte[] salt = HexFormat.of()
                    .parseHex(
                            saltHex);

            byte[] generatedHash = pbkdf2(
                    password.toCharArray(),
                    salt);

            byte[] originalHash = HexFormat.of()
                    .parseHex(
                            originalHashHex);

            System.out.println("SENHA");
            System.out.println(HexFormat.of().formatHex(generatedHash));

            System.out.println("SENHA GERADA");
            System.out.println(HexFormat.of().formatHex(originalHash));

            return MessageDigest.isEqual(
                    generatedHash,
                    originalHash);

        } catch (Exception e) {

            return false;
        }
    }

    /*
     * ==================================================
     * GENERATE UUID HASH
     * ==================================================
     */

    public static String generateHash() {

        return UUID.randomUUID()

                .toString()

                .replace("-", "");
    }

    /*
     * ==================================================
     * NUMERIC CODE
     * ==================================================
     */

    public static String generateRandomNumericalCode(
            int length) {

        StringBuilder result = new StringBuilder();

        byte[] bytes = new byte[length];

        RANDOM.nextBytes(
                bytes);

        for (int i = 0; i < length; i++) {

            result.append(
                    bytes[i] % 10 < 0
                            ? -(bytes[i] % 10)
                            : bytes[i] % 10);
        }

        return result.toString();
    }

    /*
     * ==================================================
     * ALPHANUMERIC CODE
     * ==================================================
     */

    public static String generateRandomAlphaNumericalCode(
            int length) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = RANDOM.nextInt(
                    ALPHA_NUMERIC.length());

            result.append(
                    ALPHA_NUMERIC.charAt(
                            index));
        }

        return result.toString();
    }

    /*
     * ==================================================
     * PBKDF2 INTERNAL
     * ==================================================
     */

    private static byte[] pbkdf2(

            char[] password,
            byte[] salt

    ) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        PBEKeySpec spec = new PBEKeySpec(

                password,
                salt,
                ITERATIONS,
                KEY_LENGTH);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(
                ALGORITHM);

        return factory

                .generateSecret(spec)

                .getEncoded();
    }
}