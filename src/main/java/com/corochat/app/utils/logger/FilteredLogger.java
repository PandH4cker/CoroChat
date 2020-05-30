package com.corochat.app.utils.logger;

import com.corochat.app.utils.logger.level.Level;

import java.util.function.Predicate;

/**
 * <h1>The filtered logger</h1>
 * <p>
 *     Filter over the Predicate of Level and
 *     logs if the test is conclusive.
 *     It implements the {@code public interface Logger} interface
 * </p>
 * //TODO Include diagram of FilteredLogger
 *
 * @author Raphael Dray
 * @version 0.0.5
 * @since 0.0.5
 * @see Logger
 * @see Predicate<Level>
 * @see Level
 */
public class FilteredLogger implements Logger {
    private final Logger delegateLogger;
    private final Predicate<Level> condition;

    /**
     * Logs if the Predicate is conclusive
     * @param message {@inheritDoc}
     * @param level {@inheritDoc}
     * @see Override
     * @see Level
     * @see Predicate<Level>
     * @see Logger
     */
    @Override
    public void log(String message, Level level) {
        if(this.condition.test(level)) delegateLogger.log(message, level);
    }

    /**
     * Initializes the logger and the predicate
     * @param delegateLogger The delegate logger
     * @param condition The condition to log
     */
    public FilteredLogger(final Logger delegateLogger, final Predicate<Level> condition) {
        this.delegateLogger = delegateLogger;
        this.condition = condition;
    }
}