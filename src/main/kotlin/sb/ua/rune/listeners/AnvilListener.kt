package sb.ua.rune.listeners

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.persistence.PersistentDataType
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.util.ColorLogger

/**
 * Event listener for anvil preparation that handles applying VeinSmelt rune books to pickaxes.
 * Allows players to combine a pickaxe with a VeinSmelt rune book in an anvil to apply the enchantment.
 *
 * @constructor Creates a new AnvilListener with plugin dependency injection
 *
 * @since 1.0.0
 * @see Listener
 * @see PrepareAnvilEvent
 */
class AnvilListener : Listener {

    /**
     * Handles anvil preparation events to enable VeinSmelt rune application.
     * Checks if the combination is valid (pickaxe in left slot + VeinSmelt rune book in right slot)
     * and creates the resulting enchanted pickaxe with VeinSmelt NBT data.
     *
     * @param event The PrepareAnvilEvent triggered when items are placed in an anvil
     *
     * @sample
     * // Valid combination example:
     * // - Left slot: Diamond pickaxe
     * // - Right slot: VeinSmelt rune book
     * // - Result: Diamond pickaxe with VeinSmelt enchantment applied
     *
     * @throws Exception if any error occurs during anvil preparation process
     *
     * @see RuneEnchantments.VEIN_KEY
     */
    @EventHandler
    fun onPrepareAnvil(event: PrepareAnvilEvent) {
        try {
            val inventory = event.inventory
            val leftItem = inventory.getItem(0) ?: return
            val rightItem = inventory.getItem(1) ?: return

            // Verify right item is an enchanted book with VeinSmelt NBT data
            if (rightItem.type != Material.ENCHANTED_BOOK) return

            val rightItemMeta = rightItem.itemMeta ?: return
            val veinSmeltKey = RuneEnchantments.VEIN_KEY

            if (!rightItemMeta.persistentDataContainer.has(veinSmeltKey, PersistentDataType.INTEGER)) return

            // Verify left item is a pickaxe
            if (!isPickaxe(leftItem.type)) return

            // Create result - clone of left item with VeinSmelt NBT tag added
            val resultingItem = leftItem.clone()
            val resultMeta = resultingItem.itemMeta ?: return

            resultMeta.persistentDataContainer.set(veinSmeltKey, PersistentDataType.INTEGER, 1)
            resultingItem.itemMeta = resultMeta

            // Set the result in the anvil
            event.result = resultingItem
            ColorLogger.debug("VeinSmelt rune applied via anvil for item combination")
        } catch (e: Exception) {
            ColorLogger.severe("Помилка в AnvilListener під час підготовки ковадла", e)
        }
    }

    /**
     * Checks if the given material is a pickaxe.
     *
     * @param material The material to check
     * @return true if the material is a pickaxe, false otherwise
     *
     * @sample
     * isPickaxe(Material.DIAMOND_PICKAXE) // returns true
     * isPickaxe(Material.STONE_PICKAXE)  // returns true
     * isPickaxe(Material.DIAMOND_SWORD)  // returns false
     */
    private fun isPickaxe(material: Material): Boolean {
        return material.name.endsWith("_PICKAXE", ignoreCase = true)
    }
}
