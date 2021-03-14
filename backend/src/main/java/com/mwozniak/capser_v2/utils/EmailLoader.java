package com.mwozniak.capser_v2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EmailLoader {


    public static String loadUpdateEmailEmail() {
        return loadFile("email/emailUpdated.html");
    }

    public static String loadUpdateUsernameEmail() {
        return loadFile("email/usernameUpdated.html");
    }

    public static String loadResetPasswordEmail() {
        return loadFile("email/passwordReset.html");
    }

    public static String loadRegisteredEmail() {
        return loadFile("email/registered.html");
    }

    public static String loadGameAcceptanceEmail() {
        return loadFile("email/gameAcceptance.html");
    }

    private static String loadFile(String name) {
        StringBuilder email = new StringBuilder();
        InputStream inputStream = EmailLoader.class.getClassLoader().getResourceAsStream(name);
        try (InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(streamReader);
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                email.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return email.toString();
    }
}
