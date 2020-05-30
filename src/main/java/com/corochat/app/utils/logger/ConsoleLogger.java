package com.corochat.app.utils.logger;

import com.corochat.app.utils.logger.color.Color;
import com.corochat.app.utils.logger.level.Level;

/**
 * <h1>The console logger</h1>
 * <p>
 *     Log the messages into the console.
 *     It implements the {@code public interface Logger} interface
 * </p>
 * //TODO Include diagram of ConsoleLogger
 *
 * @author Raphael Dray
 * @version 0.0.5
 * @since 0.0.5
 * @see Logger
 * @see Level
 */
public class ConsoleLogger implements Logger{

    /**
     * Logs messages with a color depending on the level
     * Infos are in blue
     * Warnings are in magenta
     * Errors are in red
     * @param message {@inheritDoc}
     * @param level {@inheritDoc}
     * @see Level
     * @see Color
     * @see Override
     */
    @Override
    public void log(String message, Level level) {
        switch (level) {
            case INFO:
                System.out.print(Color.BLUE);
                System.out.print(level.toString());
                System.out.print(Color.RESET);
                System.out.println(": " + message);
                break;
            case WARNING:
                System.out.print(Color.MAGENTA);
                System.out.print(level.toString());
                System.out.print(Color.RESET);
                System.out.println(": " + message);
                break;
            case ERROR:
                System.out.print(Color.RED);
                System.out.print(level.toString());
                System.out.print(Color.RESET);
                System.out.println(": " + message);
                break;
        }
    }
}