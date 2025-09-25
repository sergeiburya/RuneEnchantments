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
import sb.ua.rune.util.ColorLogger

/**
 * Command executor for the /rune command.
 * Handles giving rune books and applying runes to items.
 *
 * @constructor Creates a new RuneCommandExecutor
 *
 * @sample
 * // Example usage in plugin initialization:
 * getCommand("rune")?.setExecutor(RuneCommandExecutor())
 *
 * @see TabExecutor
 * @since 1.0.0
 */
class RuneCommandExecutor : TabExecutor {

    /**
     * Executes the /rune command with provided arguments.
     *
     * @param sender The sender of the command (player or console)
     * @param command The command being executed
     * @param label The alias used for the command
     * @param args The command arguments
     * @return true if the command was handled successfully, false otherwise
     *
     * @throws Exception if any error occurs during command execution
     *
     * @sample
     * // Examples of valid commands:
     * // /rune give PlayerName veinsmelt
     * // /rune apply veinsmelt
     * // /rune apply PlayerName veinsmelt
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
            ColorLogger.severe("Помилка при виконанні команди", e)
            sender.sendMessage(Component.text("Сталася помилка при виконанні команди").color(NamedTextColor.RED))
            return true
        }
    }

    /**
     * Handles the 'give' subcommand for giving rune books to players.
     *
     * @param sender The command sender
     * @param args The command arguments [give, playerName, runeType]
     *
     * @requires sender.hasPermission("rune.give")
     * @requires args.size >= 3
     * @requires Bukkit.getPlayerExact(args[1]) != null
     * @requires args[2] is a valid rune type
     *
     * @sample
     * // Valid usage:
     * handleGiveCommand(sender, arrayOf("give", "PlayerName", "veinsmelt"))
     */
    private fun handleGiveCommand(sender: CommandSender, args: Array<out String>) {
        // Permission check
        if (!sender.hasPermission("rune.give")) {
            sender.sendMessage(Component.text("У вас немає дозволу для цієї команди!").color(NamedTextColor.RED))
            return
        }

        // Argument validation
        if (args.size < 3) {
            sender.sendMessage(Component.text("Використання: /rune give <гравець> <тип>").color(NamedTextColor.YELLOW))
            return
        }

        val targetName = args[1]
        val target: Player? = Bukkit.getPlayerExact(targetName)

        if (target == null) {
            sender.sendMessage(
                Component.text("Гравця '$targetName' не знайдено або він не онлайн").color(NamedTextColor.RED)
            )
            return
        }

        when (val type = args[2].lowercase()) {
            "veinsmelt" -> giveVeinSmeltBook(sender, target)
            else -> {
                sender.sendMessage(Component.text("Невідомий тип руни: $type").color(NamedTextColor.RED))
                sender.sendMessage(Component.text("Доступні типи: veinsmelt").color(NamedTextColor.YELLOW))
            }
        }
    }

    /**
     * Gives a VeinSmelt rune book to the target player.
     * Handles both inventory placement and dropping on ground if inventory is full.
     *
     * @param sender The command sender to receive feedback
     * @param target The target player receiving the book
     *
     * @ensures target receives VeinSmelt book in inventory or on ground
     *
     * @sample
     * // Gives book to player with empty inventory slot
     * giveVeinSmeltBook(sender, player)
     */
    private fun giveVeinSmeltBook(sender: CommandSender, target: Player) {
        val book = RuneBookFactory.createVeinSmeltBook()
        val inventory = target.inventory

        // Check inventory space
        if (inventory.firstEmpty() == -1) {
            // Inventory full - drop on ground
            target.world.dropItemNaturally(target.location, book)
            sender.sendMessage(
                Component.text("Інвентар гравця ")
                    .color(NamedTextColor.YELLOW)
                    .append(Component.text(target.name).color(NamedTextColor.GREEN))
                    .append(Component.text(" заповнений - книга впала на землю").color(NamedTextColor.YELLOW))
            )
        } else {
            // Add to inventory
            inventory.addItem(book)
            sender.sendMessage(
                Component.text("Гравець ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(target.name).color(NamedTextColor.YELLOW))
                    .append(Component.text(" отримав книгу Rune: VeinSmelt").color(NamedTextColor.GREEN))
            )
        }

        ColorLogger.info("Книжка VeinSmelt видана гравцю ${target.name}")
    }

    /**
     * Handles the 'apply' subcommand for applying runes directly to held items.
     *
     * @param sender The command sender
     * @param args The command arguments [apply, (player?), runeType]
     *
     * @requires sender.hasPermission("rune.apply") for applying to other players
     * @requires target player to be online
     * @requires target to be holding a valid item
     *
     * @sample
     * // Apply to self: /rune apply veinsmelt
     * handleApplyCommand(player, arrayOf("apply", "veinsmelt"))
     *
     * // Apply to other: /rune apply PlayerName veinsmelt
     * handleApplyCommand(console, arrayOf("apply", "PlayerName", "veinsmelt"))
     */
    private fun handleApplyCommand(sender: CommandSender, args: Array<out String>) {
        // Permissions check
        if (!sender.hasPermission("rune.apply")) {
            sender.sendMessage(Component.text("У вас немає дозволу для цієї команди!").color(NamedTextColor.RED))
            return
        }

        val (target: Player?, typeArg: String?) = when {
            // /rune apply <type>  -> apply to self
            sender is Player && args.size == 2 -> Pair(sender, args[1].lowercase())

            // /rune apply <player> <type> -> admin applies to target
            args.size >= 3 -> Pair(Bukkit.getPlayerExact(args[1]), args[2].lowercase())
            else -> {
                sender.sendMessage(
                    Component.text("Використання: /rune apply <type> або /rune apply <player> <type>")
                        .color(NamedTextColor.YELLOW)
                )
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

    /**
     * Applies the VeinSmelt rune to the target player's held item.
     *
     * @param sender The command sender for feedback
     * @param target The target player whose item will be enchanted
     *
     * @requires target.inventory.itemInMainHand != null
     * @requires target.inventory.itemInMainHand.type is a pickaxe
     *
     * @ensures target's held item receives VeinSmelt NBT tag
     *
     * @throws IllegalStateException if item meta cannot be modified
     */
    private fun applyVeinToHeldItem(sender: CommandSender, target: Player) {
        val item = target.inventory.itemInMainHand
        if (item == null || item.type.isAir) {
            sender.sendMessage(Component.text("У гравця немає предмета в руці").color(NamedTextColor.RED))
            return
        }

        if (!item.type.name.endsWith("_pickaxe")) {
            sender.sendMessage(Component.text("Руну можна застосувати лише до кайла").color(NamedTextColor.RED))
            return
        }

        val meta = item.itemMeta ?: return
        val key = RuneEnchantments.VEIN_KEY
        meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, 1)
        item.itemMeta = meta

        sender.sendMessage(
            Component.text("Руна VeinSmelt застосована до предмета у руці гравця ${target.name}")
                .color(NamedTextColor.GREEN)
        )
        ColorLogger.info("Руна VeinSmelt застосована до ${target.name}")
    }

    /**
     * Sends command usage information to the sender.
     *
     * @param sender The recipient of the usage message
     */
    private fun sendUsage(sender: CommandSender) {
        sender.sendMessage(Component.text("=== RuneEnchantments ===").color(NamedTextColor.GOLD))
        sender.sendMessage(Component.text("/rune give <гравець> <тип>").color(NamedTextColor.YELLOW))
        sender.sendMessage(Component.text(" - Видає книжку з руною").color(NamedTextColor.GRAY))
        sender.sendMessage(Component.text(" - Типи: veinsmelt").color(NamedTextColor.GRAY))
    }

    /**
     * Provides tab completion for the /rune command.
     *
     * @param sender The command sender
     * @param command The command
     * @param alias The alias used
     * @param args The current arguments
     * @return List of possible completions
     *
     * @sample
     * // Typing "/rune " returns ["give", "apply"]
     * // Typing "/rune give " returns online player names
     * // Typing "/rune give PlayerName " returns ["veinsmelt"]
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return when (args.size) {
            1 -> listOf("give")
            2 -> Bukkit.getOnlinePlayers().map { it.name }
            3 -> listOf("veinsmelt")
            else -> emptyList()
        }
    }
}
