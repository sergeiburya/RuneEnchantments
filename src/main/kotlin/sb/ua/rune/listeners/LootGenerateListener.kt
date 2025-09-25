package sb.ua.rune.listeners

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.items.RuneBookFactory
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
            RuneEnchantments.instance.logger.fine("Книга VeinSmelt додана до луту (${key})")

        } catch (e: Exception) {
            RuneEnchantments.instance.logger.log(Level.SEVERE, "Помилка при генерації луту", e)
        }
    }

    /**
     * Перевіряє, чи підходить таблиця луту для додавання книжки
     */
    private fun isValidLootTable(lootTable: String): Boolean {
        return lootTable.contains("fortress") ||
                lootTable.contains("dungeon") ||
                lootTable.contains("stronghold")
    }
}
