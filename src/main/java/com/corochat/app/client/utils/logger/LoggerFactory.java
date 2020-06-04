package com.corochat.app.client.utils.logger;

import com.corochat.app.client.utils.logger.level.Level;

/**
 * <h1>The logger factory</h1>
 * <p>
 *     Used to get an instance of a logger
 * </p>
 * //TODO Include diagram of LoggerFactory
 *
 * @author Raphael Dray
 * @version 0.0.5
 * @since 0.0.5
 * @see Logger
 * @see ContextualLogger
 * @see CompositeLogger
 * @see ConsoleLogger
 * @see FilteredLogger
 * @see FileLogger
 * @see java.util.function.Predicate< Level >
 */
public class LoggerFactory {

    /**
     * Get an instance of a logger
     * @param name The name of the class
     * @return Logger An instance of a logger
     */
    public static Logger getLogger(String name) {
        return new ContextualLogger(name,
                new CompositeLogger(
                        new ConsoleLogger(),
                        new FilteredLogger(
                                new FileLogger("files/log.txt"),
                                level -> level == Level.ERROR
                                        || level == Level.WARNING)));
    }
}
