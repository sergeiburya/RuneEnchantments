package sb.ua.rune

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin
import sb.ua.rune.commands.RuneCommandExecutor

class RuneEnchantments : JavaPlugin() {

    companion object {
        lateinit var instance: RuneEnchantments
    }

    override fun onEnable() {
        instance = this
        RuneCommandExecutor("rune")

        val enabledMessage = Component.text("Plugin RuneEnchantments enabled!", NamedTextColor.GREEN)
        server.consoleSender.sendMessage(enabledMessage)
    }

    override fun onDisable() {
        val disabledMessage = Component.text("Plugin RuneEnchantments disabled!", NamedTextColor.BLUE)
        server.consoleSender.sendMessage(disabledMessage)
    }
}
