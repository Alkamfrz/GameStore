package com.dethrone.gamestore.service;

import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import com.dethrone.gamestore.model.User;

public class SecurityService {

    public String hashPassword(String password, String salt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(10)
                .withMemoryPowOfTwo(16)
                .withParallelism(1)
                .withSalt(salt.getBytes());

        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());

        byte[] result = new byte[32];
        gen.generateBytes(password.toCharArray(), result);

        return new String(result);
    }

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public boolean checkPassword(String password, String hashedPassword, String salt) {
        String result = hashPassword(password, salt);
        return hashedPassword.equals(result);
    }

    public boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (!password.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}")) {
            return false;
        }
        return true;
    }

    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public void changePassword(User user, String newPassword) {
        validatePassword(newPassword);
        String salt = generateSalt();
        String hashedPassword = hashPassword(newPassword, salt);
        user.setPassword(hashedPassword);
        user.setSalt(salt);
    }
}