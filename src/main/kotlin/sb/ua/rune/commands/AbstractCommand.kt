package sb.ua.rune.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.NotNull
import sb.ua.rune.RuneEnchantments

abstract class AbstractCommand : CommandExecutor {

    constructor(command: String): super() {
        RuneEnchantments.instance.getCommand(command)?.setExecutor(this)
    }

    abstract fun execute(sender: CommandSender, label: String, args: Array<out String>): Any

    override fun onCommand(
        @NotNull sender: CommandSender,
        @NotNull command: Command,
        @NotNull label: String,
        @NotNull args: Array<out String>
    ): Boolean {
        execute(sender, label, args)
        return true
    }
}
