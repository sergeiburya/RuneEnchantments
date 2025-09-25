package sb.ua.rune

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import sb.ua.rune.commands.RuneCommandExecutor
import sb.ua.rune.listeners.BlockBreakListener
import sb.ua.rune.listeners.LootGenerateListener
import util.ColorLogger

class RuneEnchantments : JavaPlugin() {

    companion object {
        lateinit var instance: RuneEnchantments

        val VEIN_KEY: NamespacedKey
            get() = NamespacedKey(instance, "vein_smelt")
    }

    override fun onEnable() {
        instance = this

        try {
            saveDefaultConfig()
            registerListeners()
            registerCommands()
            ColorLogger.info("RuneEnchantments успішно включено!")
        } catch (e: Exception) {
            ColorLogger.severe( "Помилка при включенні плагіна", e)
        }
    }

    override fun onDisable() {
        ColorLogger.info("RuneEnchantments виключено")
    }

    private fun registerListeners() {
        try {
            server.pluginManager.registerEvents(BlockBreakListener(), this)
            server.pluginManager.registerEvents(LootGenerateListener(), this)
            ColorLogger.info("Слухачі подій зареєстровані")
        } catch (e: Exception) {
            ColorLogger.severe( "Помилка при реєстрації слухачів", e)
        }
    }

    private fun registerCommands() {
        try {
            getCommand("rune")?.setExecutor(RuneCommandExecutor())
            ColorLogger.info("Команди зареєстровані")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка при реєстрації команд", e)
        }
    }
}

