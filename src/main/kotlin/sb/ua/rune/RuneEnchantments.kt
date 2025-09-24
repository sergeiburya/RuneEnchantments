package sb.ua.rune

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin
import sb.ua.rune.commands.RuneCommandExecutor
import sb.ua.rune.listeners.RuneListener

class RuneEnchantments : JavaPlugin() {

    companion object {
        lateinit var instance: RuneEnchantments
    }

    override fun onEnable() {
        instance = this

        val enabledMessage = Component.text("Plugin RuneEnchantments enabled!", NamedTextColor.GREEN)
        server.consoleSender.sendMessage(enabledMessage)

        RuneCommandExecutor("rune")
        server.pluginManager.registerEvents(RuneListener(this), this)

    }

    override fun onDisable() {
        val disabledMessage = Component.text("Plugin RuneEnchantments disabled!", NamedTextColor.BLUE)
        server.consoleSender.sendMessage(disabledMessage)
    }
}
