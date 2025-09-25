package util

import java.util.logging.Level
import java.util.logging.Logger

object ColorLogger {
    private val logger: Logger = Logger.getLogger("RuneEnchantments")

    private const val RESET = "\u001B[0m"
    private const val RED = "\u001B[31m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val BLUE = "\u001B[34m"

    fun severe(message: String, throwable: Throwable? = null) {
        val coloredMessage = "$RED$message$RESET"
        logger.log(Level.SEVERE, coloredMessage, throwable)
    }

    fun info(message: String) {
        val coloredMessage = "$GREEN$message$RESET"
        logger.log(Level.INFO, coloredMessage)
    }

    fun warn(message: String) {
        val coloredMessage = "$YELLOW$message$RESET"
        logger.log(Level.WARNING, coloredMessage)
    }

    fun debug(message: String) {
        val coloredMessage = "$BLUE$message$RESET"
        logger.log(Level.FINE, coloredMessage)
    }
}
