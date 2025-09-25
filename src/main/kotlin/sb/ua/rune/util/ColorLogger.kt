package sb.ua.rune.util

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Utility object for colored console logging.
 * Provides methods for logging messages with different colors and severity levels.
 *
 * @since 1.0.0
 * @see Logger
 */
object ColorLogger {
    private val logger: Logger = Logger.getLogger("RuneEnchantments")

    // ANSI color codes for console output
    private const val RESET = "\u001B[0m"
    private const val RED = "\u001B[31m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val BLUE = "\u001B[34m"

    /**
     * Logs a severe error message with red color.
     * Used for critical errors that may prevent normal operation.
     *
     * @param message The error message to log
     * @param throwable Optional exception associated with the error
     *
     * @sample
     * // Example usage:
     * ColorLogger.severe("Failed to load configuration", exception)
     */
    fun severe(message: String, throwable: Throwable? = null) {
        val coloredMessage = "$RED$message$RESET"
        logger.log(Level.SEVERE, coloredMessage, throwable)
    }

    /**
     * Logs an informational message with green color.
     * Used for general information about plugin operation.
     *
     * @param message The informational message to log
     *
     * @sample
     * // Example usage:
     * ColorLogger.info("Plugin enabled successfully")
     */
    fun info(message: String) {
        val coloredMessage = "$GREEN$message$RESET"
        logger.log(Level.INFO, coloredMessage)
    }

    /**
     * Logs a warning message with yellow color.
     * Used for non-critical issues that should be noted.
     *
     * @param message The warning message to log
     *
     * @sample
     * // Example usage:
     * ColorLogger.warn("Configuration value is using default")
     */
    fun warn(message: String) {
        val coloredMessage = "$YELLOW$message$RESET"
        logger.log(Level.WARNING, coloredMessage)
    }

    /**
     * Logs a debug message with blue color.
     * Used for detailed debugging information (typically disabled in production).
     *
     * @param message The debug message to log
     *
     * @sample
     * // Example usage:
     * ColorLogger.debug("Processing block break at ${location}")
     */
    fun debug(message: String) {
        val coloredMessage = "$BLUE$message$RESET"
        logger.log(Level.FINE, coloredMessage)
    }
}
