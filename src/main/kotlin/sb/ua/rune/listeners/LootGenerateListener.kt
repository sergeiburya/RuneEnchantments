package sb.ua.rune.listeners

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.items.RuneBookFactory
import util.ColorLogger
import java.util.logging.Level

/**
 * Слухач генерації луту для додавання книжок у скрині
 */
class LootGenerateListener : Listener {

    private val allowedLootTables = setOf(
        NamespacedKey.minecraft("chests/nether_bridge"),
        NamespacedKey.minecraft("chests/simple_dungeon"),
        NamespacedKey.minecraft("chests/stronghold_library")
    )

    /**
     * Обробка генерації луту в скринях
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
            ColorLogger.severe( "Помилка при генерації луту", e)
        }
    }
}
