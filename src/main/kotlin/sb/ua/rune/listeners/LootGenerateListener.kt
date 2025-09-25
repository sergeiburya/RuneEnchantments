package sb.ua.rune.listeners

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.items.RuneBookFactory
import sb.ua.rune.util.ColorLogger

/**
 * Event listener for loot generation that adds VeinSmelt rune books to specific chests.
 * Handles the addition of custom enchantment books to dungeon, fortress, and stronghold chests.
 *
 * @constructor Creates a new LootGenerateListener
 *
 * @since 1.0.0
 * @see Listener
 * @see LootGenerateEvent
 */
class LootGenerateListener : Listener {

    /**
     * Set of loot table keys where VeinSmelt books can spawn.
     * Includes Nether fortresses, dungeons, and stronghold libraries.
     */
    private val allowedLootTables = setOf(
        NamespacedKey.minecraft("chests/nether_bridge"),
        NamespacedKey.minecraft("chests/simple_dungeon"),
        NamespacedKey.minecraft("chests/stronghold_library")
    )

    /**
     * Handles loot generation events to add VeinSmelt books to appropriate chests.
     * Checks the loot table and spawn chance before adding the book to the generated loot.
     *
     * @param event The LootGenerateEvent triggered when loot is generated in a container
     *
     * @sample
     * // This method is automatically called by Bukkit when loot is generated
     *
     * @see LootGenerateEvent
     * @see RuneBookFactory.createVeinSmeltBook
     */
    @EventHandler
    fun onLootGenerate(event: LootGenerateEvent) {
        try {
            val key = event.lootTable.key
            if (!allowedLootTables.contains(key)) return

            val spawnChance = RuneEnchantments.instance.config.getDouble("loot.spawnChance", 0.05)
            if (Math.random() > spawnChance) return

            val book = RuneBookFactory.createVeinSmeltBook()
            event.loot.add(book)
            ColorLogger.info("Книга VeinSmelt додана до луту (${key})")

        } catch (e: Exception) {
            ColorLogger.severe("Помилка при генерації луту", e)
        }
    }
}
