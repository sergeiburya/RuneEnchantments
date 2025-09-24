package sb.ua.rune.items

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.ChatColor

object RuneBookFactory {
    fun createVeinSmeltBook(): ItemStack {
        val book = ItemStack(Material.ENCHANTED_BOOK)
        val meta = book.itemMeta as EnchantmentStorageMeta

        meta.setDisplayName("${ChatColor.GOLD}Rune: VeinSmelt")
        meta.lore = listOf(
            "${NamedTextColor.GRAY}Видобувай руди особливим чином:",
            "${NamedTextColor.YELLOW}- Автоматична виплавка (AutoSmelt)",
            "${NamedTextColor.YELLOW}- Ламає всю жилу (VeinMiner)"
        )

        meta.addStoredEnchant(Enchantment.UNBREAKING, 1, true)

        book.itemMeta = meta
        return book
    }
}