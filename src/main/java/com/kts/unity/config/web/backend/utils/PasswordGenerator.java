package com.kts.unity.config.web.backend.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordGenerator {

    public String getNewAutoGeneratedPassword(int passwLength) {
        SecureRandom random = new SecureRandom();
        String password = (new BigInteger(130, random)).toString(32).substring(0, passwLength);
        return password;
    }
}
