package sb.ua.rune

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import sb.ua.rune.commands.RuneCommandExecutor
import sb.ua.rune.listeners.AnvilListener
import sb.ua.rune.listeners.BlockBreakListener
import sb.ua.rune.listeners.LootGenerateListener
import sb.ua.rune.util.ColorLogger

/**
 * Main plugin class for RuneEnchantments.
 * Handles plugin initialization, configuration, and registration of components.
 *
 * @constructor Creates a new RuneEnchantments plugin instance
 *
 * @since 1.0.0
 * @see JavaPlugin
 */
class RuneEnchantments : JavaPlugin() {

    companion object {
        /**
         * The singleton instance of the RuneEnchantments plugin.
         */
        lateinit var instance: RuneEnchantments

        /**
         * The NamespacedKey used for identifying VeinSmelt rune in NBT data.
         *
         * @return NamespacedKey with plugin namespace for VeinSmelt identification
         */
        val VEIN_KEY: NamespacedKey
            get() = NamespacedKey(instance, "vein_smelt")
    }

    /**
     * Called when the plugin is enabled.
     * Initializes configuration, registers listeners and commands.
     *
     * @sample
     * // This method is automatically called by Bukkit when the plugin is enabled
     *
     * @throws Exception if initialization fails
     */
    override fun onEnable() {
        instance = this

        try {
            saveDefaultConfig()
            registerListeners()
            registerCommands()
            ColorLogger.info("RuneEnchantments успішно включено!")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка при включенні плагіна", e)
        }
    }

    /**
     * Called when the plugin is disabled.
     * Performs cleanup operations.
     */
    override fun onDisable() {
        ColorLogger.info("RuneEnchantments виключено")
    }

    /**
     * Registers all event listeners used by the plugin.
     *
     * @throws Exception if listener registration fails
     *
     * @see BlockBreakListener
     * @see LootGenerateListener
     */
    private fun registerListeners() {
        try {
            val pm = server.pluginManager
            pm.registerEvents(BlockBreakListener(), this)
            pm.registerEvents(LootGenerateListener(this), this)
            pm.registerEvents(AnvilListener(), this)
            ColorLogger.info("Слухачі подій зареєстровані")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка при реєстрації слухачів", e)
        }
    }

    /**
     * Registers all commands used by the plugin.
     *
     * @throws Exception if command registration fails
     *
     * @see RuneCommandExecutor
     */
    private fun registerCommands() {
        try {
            getCommand("rune")?.setExecutor(RuneCommandExecutor())
            ColorLogger.info("Команди зареєстровані")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка при реєстрації команд", e)
        }
    }
}

