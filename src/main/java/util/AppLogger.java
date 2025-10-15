package util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Application-wide logger utility.
 * Logs application events to console and app.log file.
 */
public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("NovaBookApp");

    static {
        try {
            // Remove default console handler
            LOGGER.setUseParentHandlers(false);

            // Console Handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(consoleHandler);

            // File Handler (app.log)
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);

            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    // Private constructor to prevent instantiation
    private AppLogger() {}

    /**
     * Logs an informational message (simulating HTTP GET/POST).
     * @param message The message to log
     */
    public static void logInfo(String message) {
        LOGGER.info(message);
    }

    /**
     * Logs an error with exception details.
     * @param message The error message
     * @param e The exception
     */
    public static void logError(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }

    /**
     * Logs a warning message.
     * @param message The warning message
     */
    public static void logWarning(String message) {
        LOGGER.warning(message);
    }

    /**
     * Simulates an HTTP request log entry.
     * @param method HTTP method (GET, POST, PATCH, DELETE)
     * @param endpoint The endpoint path
     * @param details Additional details
     */
    public static void logHttpRequest(String method, String endpoint, String details) {
        LOGGER.info(String.format("[HTTP] %s %s - %s", method, endpoint, details));
    }

    /**
     * Logs successful operations.
     * @param operation The operation performed
     * @param details Additional details
     */
    public static void logSuccess(String operation, String details) {
        LOGGER.info(String.format("[SUCCESS] %s - %s", operation, details));
    }
}