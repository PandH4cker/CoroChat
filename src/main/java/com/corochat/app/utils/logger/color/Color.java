package com.corochat.app.utils.logger.color;

/**
 * <h1>The color code enumeration</h1>
 * <p>
 *     The color codes for the console logger:
 *     <li>RESET - <b>reset the color of terminal</b></li>
 *     <li>RED - <b>used to error level logger</b></li>
 *     <li>BLUE - <b>used to info level logger</b></li>
 *     <li>GREEN - <b>used to the date in logs messages</b></li>
 *     <li>YELLOW - <b>used to the class name in logs messages</b></li>
 *     <li>MAGENTA - <b>used to the warning level logger</b></li>
 * </p>
 * //TODO Include diagram of Color
 *
 * @author Raphael Dray
 * @version 1.0.0
 * @since 1.0.0
 */
public enum Color {
    RESET("\033[0m"),

    // Regular Colors
    RED("\033[0;31m"),      // RED
    BLUE("\033[0;34m"),     // BLUE
    GREEN("\033[0;32m"),    // GREEN
    YELLOW("\033[0;33m"),   // YELLOW
    MAGENTA("\033[0;35m");  // MAGENTA

    private final String code;

    /**
     * This constructor initialize the {@code private final String code} attribute
     * @param code The code of the color
     */
    Color(String code) {
        this.code = code;
    }

    /**
     * Overriding toString method from the Object class
     * @return String - The code of the color
     * @see Override
     */
    @Override
    public String toString() {
        return this.code;
    }
}