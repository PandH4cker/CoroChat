package com.corochat.app.utils.validations;

public class OSValidator {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isMac(){
        return OS.contains("mac");
    }
}
