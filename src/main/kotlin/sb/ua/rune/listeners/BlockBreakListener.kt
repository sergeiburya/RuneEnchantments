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
import util.ColorLogger
import java.util.*
import java.util.logging.Level

/**
 * Слухач події видобутку блоків для реалізації функціоналу VeinSmelt
 */
class BlockBreakListener : Listener {

    private val autoSmeltOres = mapOf(
        Material.IRON_ORE to Material.IRON_INGOT,
        Material.DEEPSLATE_IRON_ORE to Material.IRON_INGOT,
        Material.GOLD_ORE to Material.GOLD_INGOT,
        Material.DEEPSLATE_GOLD_ORE to Material.GOLD_INGOT
    )

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

    private val directions = listOf(
        BlockFace.UP, BlockFace.DOWN,
        BlockFace.NORTH, BlockFace.SOUTH,
        BlockFace.EAST, BlockFace.WEST
    )

    /**
     * Обробка події видобутку блоку
     */
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        try {
            val player = event.player
            val item = player.inventory.itemInMainHand
            val block = event.block

            /**
             * Перевірка, чи є на предметі зачарування VeinSmelt
             */
            if (!hasVeinRune(item))  {
                return
            }

             /**
             *Перевірка, чи включені функції в конфігурації
             */
            val config = RuneEnchantments.instance.config
            val autoSmeltEnabled = config.getBoolean("veinsmelt.autoSmeltEnabled", true)
            val veinMinerEnabled = config.getBoolean("veinsmelt.veinMinerEnabled", true)

            if (autoSmeltEnabled) handleAutoSmelt(event, block)
            if (veinMinerEnabled && veinMineOres.contains(block.type)) {
                handleVeinMiner(block, player)
            }

        } catch (e: Exception) {
            ColorLogger.severe( "Помилка при обробці видобутку блоку", e)
        }
    }

    private fun hasVeinRune(item: ItemStack?): Boolean {
        if (item == null) return false
        val meta = item.itemMeta ?: return false
        val key = RuneEnchantments.VEIN_KEY
        return meta.persistentDataContainer.has(key, PersistentDataType.INTEGER)
    }

    /**
     * Обробка автоматичної виплавки
     */
    private fun handleAutoSmelt(event: BlockBreakEvent, block: Block) {
        val resultMaterial = autoSmeltOres[block.type] ?: return

        event.isDropItems = false
        block.world.dropItemNaturally(block.location, ItemStack(resultMaterial))

        ColorLogger.info("AutoSmelt застосовано для ${block.type}")
    }

    /**
     * Обробка видобутку жили
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
