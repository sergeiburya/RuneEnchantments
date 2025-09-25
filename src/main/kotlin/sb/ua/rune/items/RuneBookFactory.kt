package sb.ua.rune.items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.util.ColorLogger

/**
 * Factory class for creating custom enchantment books.
 * Provides methods to generate enchanted books with specific rune enchantments.
 *
 * @since 1.0.0
 * @see ItemStack
 * @see EnchantmentStorageMeta
 */
object RuneBookFactory {

    /**
     * Creates an enchanted book with the VeinSmelt rune enchantment.
     * The book will have a custom display name, lore, and NBT data identifying it as a VeinSmelt rune.
     *
     * @return ItemStack representing the enchanted book with VeinSmelt rune
     *
     * @sample
     * // Example usage:
     * val veinSmeltBook = RuneBookFactory.createVeinSmeltBook()
     * player.inventory.addItem(veinSmeltBook)
     *
     * @throws Exception if there's an error during book creation (logged but not propagated)
     *
     * @see Material.ENCHANTED_BOOK
     * @see RuneEnchantments.VEIN_KEY
     */
    fun createVeinSmeltBook(): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as? EnchantmentStorageMeta ?: return book

        try {
            // Set display name and lore
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

            // Add NBT data to identify the rune
            val key = RuneEnchantments.VEIN_KEY
            meta.persistentDataContainer.set(key, PersistentDataType.INTEGER, 1)

            book.itemMeta = meta
            ColorLogger.info("Книжка VeinSmelt успішно створена")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка при створенні книжки", e)
        }

        return book
    }
}
