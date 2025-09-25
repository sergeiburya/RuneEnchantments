package sb.ua.rune.listeners

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.plugin.java.JavaPlugin
import sb.ua.rune.items.RuneBookFactory
import sb.ua.rune.util.ColorLogger

/**
 * Event listener for loot generation that adds VeinSmelt rune books to specific chests.
 * Handles the addition of custom enchantment books to dungeon, fortress, and stronghold chests.
 *
 * The loot tables are configurable via the plugin configuration file.
 *
 * @param plugin The plugin instance for accessing configuration
 * @constructor Creates a new LootGenerateListener with plugin dependency injection
 *
 * @since 1.0.0
 * @see Listener
 * @see LootGenerateEvent
 */
class LootGenerateListener(
    private val plugin: JavaPlugin
) : Listener {

    /**
     * Set of enabled loot table keys where VeinSmelt books can spawn.
     * Initialized from plugin configuration on class instantiation.
     *
     * @see mapStructureToKey
     */
    private val enabledLootTables: Set<NamespacedKey> = run {
        val config = plugin.config
        val structures = config.getStringList("loot.enabledStructures")
        structures.mapNotNull { mapStructureToKey(it) }.toSet()
    }


    /**
     * Handles loot generation events to add VeinSmelt books to appropriate chests.
     * Checks if loot generation is enabled, verifies the loot table against configured structures,
     * and applies spawn chance before adding the book to generated loot.
     *
     * @param event The LootGenerateEvent triggered when loot is generated in a container
     *
     * @sample
     * // This method is automatically called by Bukkit when loot is generated
     * // Only processes events for loot tables specified in 'loot.enabledStructures' config
     * // Respects both 'loot.enabled' toggle and 'loot.dropChance' probability settings
     *
     * @see LootGenerateEvent
     * @see RuneBookFactory.createVeinSmeltBook
     * @see enabledLootTables
     */
    @EventHandler
    fun onLootGenerate(event: LootGenerateEvent) {
        try {
            if (event.lootTable.key !in enabledLootTables) return
            if (!plugin.config.getBoolean("loot.enabled")) return

            val spawnChance = plugin.config.getDouble("loot.dropChance", 0.05)
            if (Math.random() > spawnChance) return

            val book = RuneBookFactory.createVeinSmeltBook()
            event.loot.add(book)

        } catch (e: Exception) {
            ColorLogger.severe("Помилка при генерації луту", e)
        }
    }

    /**
     * Maps configuration structure names to Minecraft loot table namespaced keys.
     *
     * @param structure The structure name from configuration (case-insensitive)
     * @return Corresponding NamespacedKey for the loot table, or null if invalid structure
     *
     * @sample
     * // Example mappings:
     * mapStructureToKey("dungeon")    -> NamespacedKey.minecraft("chests/simple_dungeon")
     * mapStructureToKey("fortress")   -> NamespacedKey.minecraft("chests/nether_bridge")
     * mapStructureToKey("library")    -> NamespacedKey.minecraft("chests/stronghold_library")
     */
    private fun mapStructureToKey(structure: String): NamespacedKey? =
        when (structure.lowercase()) {
            "dungeon" -> NamespacedKey.minecraft("chests/simple_dungeon")
            "fortress" -> NamespacedKey.minecraft("chests/nether_bridge")
            "library" -> NamespacedKey.minecraft("chests/stronghold_library")
            else -> null
        }
}
