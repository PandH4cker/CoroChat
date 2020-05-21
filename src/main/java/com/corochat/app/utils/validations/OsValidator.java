package com.corochat.app.utils.validations;

public class OsValidator {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isMac(){
        return OS.indexOf("mac") >= 0;
    }
}
