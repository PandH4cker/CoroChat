package com.corochat.app.server.utils.validations;

/**
 * <h1>The OSValidator object</h1>
 * <p>
 *     This class helps to check the OS of the computer
 * </p>
 * //TODO Include diagram of UserModel
 *
 * @author Thierry Khamphousone
 * @version 0.0.8
 * @since 0.0.2
 */
public class OSValidator {
    /**
     * Contains the OS of the computer
     */
    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Check if the computer is a Mac
     * @return boolean - The answer to the question of this computer is a Mac
     */
    public static boolean isMac(){
        return OS.contains("mac");
    }
}
