package sb.ua.rune.commands

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import sb.ua.rune.items.RuneBookFactory

class RuneCommandExecutor(command: String) : AbstractCommand(command) {

    override fun execute(sender: CommandSender, 
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§eUsage: /rune give <player> <type>")
            return true
        }

        if (args[0].equals("give", ignoreCase = true)) {
            if (!sender.hasPermission("rune.give")) {
                sender.sendMessage("${NamedTextColor.RED}§cУ тебе немає прав!")
                return true
            }

            if (args.size < 3) {
                sender.sendMessage("${NamedTextColor.BLUE}§eUsage: /rune give <player> <type>")
                return true
            }

            val target: Player? = Bukkit.getPlayerExact(args[1])
            if (target == null) {
                sender.sendMessage("${NamedTextColor.DARK_RED}§cГравець ${args[1]} не знайдений.")
                return true
            }

            val type = args[2].lowercase()
            if (type != "veinsmelt") {
                sender.sendMessage("${NamedTextColor.DARK_RED}§cНевідомий тип руни: $type")
                return true
            }

            sender.sendMessage("${NamedTextColor.GREEN}§a(Тут ми дамо ${target.name} книгу з рунною $type)") //TODO clean
            if (type == "veinsmelt") {
                val book = RuneBookFactory.createVeinSmeltBook()
                val inventory = target.inventory

                if (inventory.firstEmpty() == -1) {
                    target.world.dropItemNaturally(target.location, book)
                    sender.sendMessage("${NamedTextColor.GREEN}§eІнвентар ${target.name} заповнений — книга впала на землю!")
                } else {
                    inventory.addItem(book)
                    sender.sendMessage("${NamedTextColor.GREEN}§aГравець ${target.name} отримав книгу Rune: VeinSmelt")
                }
            }
        }

        return true
    }
}
