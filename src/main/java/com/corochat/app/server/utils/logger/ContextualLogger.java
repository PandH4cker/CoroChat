package com.corochat.app.server.utils.logger;

import com.corochat.app.server.utils.logger.color.Color;
import com.corochat.app.server.utils.logger.level.Level;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h1>The contextual logger</h1>
 * <p>
 *     Logs the messages with a format.
 *     It implements the {@code public interface Logger} interface.
 * </p>
 * //TODO Include diagram of ContextualLogger
 *
 * @author Raphael Dray
 * @version 0.0.5
 * @since 0.0.5
 * @see Logger
 * @see SimpleDateFormat
 * @see Level
 */
public class ContextualLogger implements Logger {
    private final Logger delegateLogger;
    private final String callerClass;

    /**
     * {@value "YYYY-MM-dd HH:mm:ss.SSS"}
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");

    /**
     * Logs the message with a format
     * @param message {@inheritDoc}
     * @param level {@inheritDoc}
     * @see Override
     * @see Level
     * @see Color
     * @see Logger
     */
    @Override
    public void log(String message, Level level) {
        this.delegateLogger.log(String.format("%s%s %s%s%s: %s",
                Color.GREEN,
                DATE_FORMAT.format(new Date()),
                Color.YELLOW,
                this.callerClass,
                Color.RESET,
                message), level);
    }

    /**
     * Initialize the caller class and the delegate logger
     * @param callerClass The caller class
     * @param delegateLogger The delegate logger
     * @see Logger
     */
    public ContextualLogger(final String callerClass, final Logger delegateLogger) {
        this.callerClass = callerClass;
        this.delegateLogger = delegateLogger;
    }
}