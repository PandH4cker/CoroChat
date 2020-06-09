package com.corochat.app.client.utils.logger;

import com.corochat.app.client.utils.logger.level.Level;

/**
 * <h1>The composite logger</h1>
 * <p>
 *     A logger composed by two logger:
 *     <li>A dev logger</li>
 *     <li>A file logger</li>
 *     It implements the {@code public interface Logger} interface.
 * </p>
 * //TODO Include diagram of CompositeLogger
 *
 * @author Dray Raphael
 * @version 0.0.8
 * @since 0.0.5
 * @see Logger
 */
public class CompositeLogger implements Logger {
    private final Logger devLog;
    private final Logger fileLog;

    /**
     * Log with the two loggers
     * @param message {@inheritDoc}
     * @param level {@inheritDoc}
     * @see Override
     * @see Level
     */
    @Override
    public void log(String message, Level level) {
        this.devLog.log(message, level);
        this.fileLog.log(message, level);
    }

    /**
     * Compose two logger
     * @param devLog The dev logger
     * @param fileLog The file logger
     * @see Logger
     */
    public CompositeLogger(final Logger devLog, final Logger fileLog) {
        this.devLog = devLog;
        this.fileLog = fileLog;
    }
}
