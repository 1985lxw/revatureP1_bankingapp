package com.banking.app;

import at.favre.lib.crypto.bcrypt.BCrypt;

// hash() will hash + salt passwords
// checkPassword() will verify the user input's password against their stored hash

public class PasswordHasher {
    public static String hash(String password) {
        char[] passwordCharArr = password.toCharArray();
        String hashPassword = BCrypt.withDefaults().hashToString(12, passwordCharArr);
        return hashPassword;
    }

    public static boolean checkPassword(String password, String hash) {
        char[] passwordCharArr = password.toCharArray();
        BCrypt.Result result = BCrypt.verifyer().verify(passwordCharArr, hash);
        return result.verified;
    }
}
