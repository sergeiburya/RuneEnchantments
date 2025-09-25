package sb.ua.rune.items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType
import sb.ua.rune.RuneEnchantments
import util.ColorLogger

/**
 * Фабрика для створення книжок з кастомними зачаруваннями
 */
object RuneBookFactory {

    /**
     * Створює книжку з зачаруванням VeinSmelt
     * @return ItemStack - книжка з зачаруванням
     */
    fun createVeinSmeltBook(): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as? EnchantmentStorageMeta ?: return book

        try {
            /**
            *Встановлення назви та опису
            */
            meta.displayName(
                Component.text("Rune: VeinSmelt")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
            )

            meta.lore(
                listOf(
                    Component.text("Ancient Rune infused with smelting power...")
                        .color(NamedTextColor.GRAY),
                    Component.text(""),
                    Component.text("• AutoSmelt - автоматична виплавка")
                        .color(NamedTextColor.YELLOW),
                    Component.text("• VeinMiner - видобуток всієї жили")
                        .color(NamedTextColor.YELLOW),
                    Component.text(""),
                    Component.text("Застосовується тільки на кайла")
                        .color(NamedTextColor.DARK_GRAY)
                )
            )

            /**
             * Додавання зачарування
             */
            val key = RuneEnchantments.VEIN_KEY
            meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, 1)

            book.itemMeta = meta
            ColorLogger.info("Книжка VeinSmelt успішно створена")
        } catch (e: Exception) {
            ColorLogger.severe( "Помилка при створенні книжки", e)
        }

        return book
    }
}
