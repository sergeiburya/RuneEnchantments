package sb.ua.rune.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.items.RuneBookFactory
import java.util.logging.Level

/**
 * Обробник команди /rune
 */
class RuneCommandExecutor : TabExecutor {

    /**
     * Виконання команди
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            if (args.isEmpty()) {
                sendUsage(sender)
                return true
            }

            when (args[0].lowercase()) {
                "give" -> handleGiveCommand(sender, args)
                "apply" -> handleApplyCommand(sender, args)
                else -> sendUsage(sender)
            }

            return true
        } catch (e: Exception) {
            RuneEnchantments.instance.logger.log(Level.SEVERE, "Помилка при виконанні команди", e)
            sender.sendMessage(Component.text("Сталася помилка при виконанні команди").color(NamedTextColor.RED))
            return true
        }
    }

    /**
     * Обробка підкоманди give
     */
    private fun handleGiveCommand(sender: CommandSender, args: Array<out String>) {
        /**
        * Перевірка прав
        */
        if (!sender.hasPermission("rune.give")) {
            sender.sendMessage(Component.text("У вас немає дозволу для цієї команди!").color(NamedTextColor.RED))
            return
        }

        /**
        * Валідація аргументів
        */
        if (args.size < 3) {
            sender.sendMessage(Component.text("Використання: /rune give <гравець> <тип>").color(NamedTextColor.YELLOW))
            return
        }

        val targetName = args[1]
        val target: Player? = Bukkit.getPlayerExact(targetName)

        if (target == null) {
            sender.sendMessage(Component.text("Гравця '$targetName' не знайдено або він не онлайн").color(NamedTextColor.RED))
            return
        }

        val type = args[2].lowercase()
        when (type) {
            "veinsmelt" -> giveVeinSmeltBook(sender, target)
            else -> {
                sender.sendMessage(Component.text("Невідомий тип руни: $type").color(NamedTextColor.RED))
                sender.sendMessage(Component.text("Доступні типи: veinsmelt").color(NamedTextColor.YELLOW))
            }
        }
    }

    /**
     * Видача книжки з зачаруванням VeinSmelt
     */
    private fun giveVeinSmeltBook(sender: CommandSender, target: Player) {
        val book = RuneBookFactory.createVeinSmeltBook()
        val inventory = target.inventory

        /**
        * Перевірка місця в інвентарі
        */
        if (inventory.firstEmpty() == -1) {
            /**
            * Інвентар повний - кидаємо на землю
            */
            target.world.dropItemNaturally(target.location, book)
            sender.sendMessage(
                Component.text("Інвентар гравця ")
                    .color(NamedTextColor.YELLOW)
                    .append(Component.text(target.name).color(NamedTextColor.GREEN))
                    .append(Component.text(" заповнений - книга впала на землю").color(NamedTextColor.YELLOW))
            )
        } else {
            /**
            * Додаємо в інвентар
            */
            inventory.addItem(book)
            sender.sendMessage(
                Component.text("Гравець ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(target.name).color(NamedTextColor.YELLOW))
                    .append(Component.text(" отримав книгу Rune: VeinSmelt").color(NamedTextColor.GREEN))
            )
        }

        RuneEnchantments.instance.logger.info("Книжка VeinSmelt видана гравцю ${target.name}")
    }

    private fun handleApplyCommand(sender: CommandSender, args: Array<out String>) {
        /**
        * Permissions
        */
        if (!sender.hasPermission("rune.apply")) {
            sender.sendMessage(Component.text("У вас немає дозволу для цієї команди!").color(NamedTextColor.RED))
            return
        }

        val (target: Player?, typeArg: String?) = when {
            /**
            * /rune apply <type>  -> apply to self
            */
            sender is Player && args.size == 2 -> Pair(sender, args[1].lowercase())

            /**
            * /rune apply <player> <type> -> admin applies to target
            */
            args.size >= 3 -> Pair(Bukkit.getPlayerExact(args[1]), args[2].lowercase())
            else -> {
                sender.sendMessage(Component.text("Використання: /rune apply <type> або /rune apply <player> <type>").color(NamedTextColor.YELLOW))
                return
            }
        }

        if (target == null) {
            sender.sendMessage(Component.text("Гравця не знайдено або він не онлайн").color(NamedTextColor.RED))
            return
        }

        when (typeArg) {
            "veinsmelt" -> applyVeinToHeldItem(sender, target)
            else -> sender.sendMessage(Component.text("Невідомий тип руни: $typeArg").color(NamedTextColor.RED))
        }
    }

    private fun applyVeinToHeldItem(sender: CommandSender, target: Player) {
        val item = target.inventory.itemInMainHand
        if (item == null || item.type.isAir) {
            sender.sendMessage(Component.text("У гравця немає предмета в руці").color(NamedTextColor.RED))
            return
        }

        if (!item.type.name.endsWith("_PICKAXE")) {
            sender.sendMessage(Component.text("Руну можна застосувати лише до кайла").color(NamedTextColor.RED))
            return
        }

        val meta = item.itemMeta ?: return
        val key = RuneEnchantments.VEIN_KEY
        meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, 1)
        item.itemMeta = meta

        sender.sendMessage(Component.text("Руна VeinSmelt застосована до предмета у руці гравця ${target.name}").color(NamedTextColor.GREEN))
        RuneEnchantments.instance.logger.info("Руна VeinSmelt застосована до ${target.name}")
    }

    /**
     * Відправка повідомлення про використання команди
     */
    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage(Component.text("=== RuneEnchantments ===").color(NamedTextColor.GOLD))
        sender.sendMessage(Component.text("/rune give <гравець> <тип>").color(NamedTextColor.YELLOW))
        sender.sendMessage(Component.text(" - Видає книжку з руною").color(NamedTextColor.GRAY))
        sender.sendMessage(Component.text(" - Типи: veinsmelt").color(NamedTextColor.GRAY))
    }

    /**
     * Автодоповнення команди
     */
    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        return when (args.size) {
            1 -> listOf("give")
            2 -> Bukkit.getOnlinePlayers().map { it.name }
            3 -> listOf("veinsmelt")
            else -> emptyList()
        }
    }
}
