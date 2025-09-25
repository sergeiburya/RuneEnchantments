package sb.ua.rune.listeners

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import sb.ua.rune.RuneEnchantments
import sb.ua.rune.util.ColorLogger
import java.util.*

/**
 * Event listener for block break events that implements VeinSmelt functionality.
 * Handles both AutoSmelt (automatic smelting of ores) and VeinMiner (breaking connected ore veins).
 *
 * @constructor Creates a new BlockBreakListener
 *
 * @since 1.0.0
 * @see Listener
 * @see BlockBreakEvent
 */
class BlockBreakListener : Listener {

    /**
     * Mapping of ore materials to their smelted results for AutoSmelt functionality.
     */
    private val autoSmeltOres = mapOf(
        Material.IRON_ORE to Material.IRON_INGOT,
        Material.DEEPSLATE_IRON_ORE to Material.IRON_INGOT,
        Material.GOLD_ORE to Material.GOLD_INGOT,
        Material.DEEPSLATE_GOLD_ORE to Material.GOLD_INGOT
    )

    /**
     * Set of ore materials that are affected by VeinMiner functionality.
     */
    private val veinMineOres = setOf(
        Material.COAL_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.REDSTONE_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.EMERALD_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.LAPIS_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.NETHER_QUARTZ_ORE
    )

    /**
     * Directions to check for connected blocks during VeinMiner operation.
     */
    private val directions = listOf(
        BlockFace.UP, BlockFace.DOWN,
        BlockFace.NORTH, BlockFace.SOUTH,
        BlockFace.EAST, BlockFace.WEST
    )

    /**
     * Handles block break events to apply VeinSmelt functionality.
     * Checks if the player's tool has the VeinSmelt rune and applies AutoSmelt/VeinMiner as configured.
     *
     * @param event The BlockBreakEvent triggered when a block is broken
     *
     * @sample
     * // This method is automatically called by Bukkit when a block is broken
     *
     * @see BlockBreakEvent
     * @see hasVeinRune
     */
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        try {
            val player = event.player
            val item = player.inventory.itemInMainHand
            val block = event.block

            // Check if the item has VeinSmelt rune
            if (!hasVeinRune(item)) {
                return
            }

            // Check if features are enabled in config
            val config = RuneEnchantments.instance.config
            val autoSmeltEnabled = config.getBoolean("veinsmelt.autoSmeltEnabled", true)
            val veinMinerEnabled = config.getBoolean("veinsmelt.veinMinerEnabled", true)

            if (autoSmeltEnabled) handleAutoSmelt(event, block)
            if (veinMinerEnabled && veinMineOres.contains(block.type)) {
                handleVeinMiner(block, player)
            }

        } catch (e: Exception) {
            ColorLogger.severe("Помилка при обробці видобутку блоку", e)
        }
    }

    /**
     * Checks if an item has the VeinSmelt rune applied via NBT data.
     *
     * @param item The ItemStack to check for VeinSmelt rune
     * @return true if the item has VeinSmelt rune, false otherwise
     *
     * @see RuneEnchantments.VEIN_KEY
     * @see PersistentDataType
     */
    private fun hasVeinRune(item: ItemStack?): Boolean {
        if (item == null) return false
        val meta = item.itemMeta ?: return false
        val key = RuneEnchantments.VEIN_KEY
        return meta.persistentDataContainer.has(key, PersistentDataType.INTEGER)
    }

    /**
     * Handles AutoSmelt functionality by replacing ore drops with smelted ingots.
     *
     * @param event The BlockBreakEvent being processed
     * @param block The block that was broken
     *
     * @see autoSmeltOres
     */
    private fun handleAutoSmelt(event: BlockBreakEvent, block: Block) {
        val resultMaterial = autoSmeltOres[block.type] ?: return

        event.isDropItems = false
        block.world.dropItemNaturally(block.location, ItemStack(resultMaterial))

        ColorLogger.info("AutoSmelt застосовано для ${block.type}")
    }

    /**
     * Handles VeinMiner functionality by breaking connected ore blocks of the same type.
     * Uses a BFS algorithm to find and break connected ores up to the configured limit.
     *
     * @param startBlock The initial block that was broken
     * @param player The player who broke the block
     *
     * @see directions
     * @see veinMineOres
     */
    private fun handleVeinMiner(startBlock: Block, player: Player) {
        val maxBlocks = RuneEnchantments.instance.config.getInt("veinsmelt.veinMinerBlockLimit", 30)

        val queue: Queue<Block> = LinkedList()
        val visited = mutableSetOf<Block>()
        queue.add(startBlock)
        visited.add(startBlock)

        var count = 0
        val targetMat = startBlock.type

        while (queue.isNotEmpty() && count < maxBlocks) {
            val current = queue.poll()

            for (face in directions) {
                val nearby = current.getRelative(face)
                if (nearby.type == targetMat && visited.add(nearby)) {
                    queue.add(nearby)
                    nearby.breakNaturally(player.inventory.itemInMainHand)
                    count++
                    if (count >= maxBlocks) {
                        ColorLogger.info("Досягнуто ліміт VeinMiner: $maxBlocks блоків")
                        return
                    }
                }
            }
        }

        ColorLogger.info("VeinMiner видобув $count блоків")
    }
}
