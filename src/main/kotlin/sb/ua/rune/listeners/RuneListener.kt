package sb.ua.rune.listeners

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import sb.ua.rune.RuneEnchantments

class RuneListener(private val plugin: RuneEnchantments) : Listener {

    private val maxBlocks = plugin.config.getInt("veinsmelt.veinMinerBlockLimit", 30)

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player: Player = event.player
        val block: Block = event.block

        val item = player.inventory.itemInMainHand
        if (!item.hasItemMeta()) return
        val meta = item.itemMeta ?: return
        if (meta.displayName == null || !meta.displayName.contains("Rune: VeinSmelt")) return

        when (block.type) {
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> {
                event.isDropItems = false
                block.world.dropItemNaturally(block.location, org.bukkit.inventory.ItemStack(Material.IRON_INGOT))
                veinMine(block, player, Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE)
            }
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE -> {
                event.isDropItems = false
                block.world.dropItemNaturally(block.location, org.bukkit.inventory.ItemStack(Material.GOLD_INGOT))
                veinMine(block, player, Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE)
            }
            else -> return
        }
    }

    private fun veinMine(origin: Block, player: Player, vararg targets: Material) {
        val queue = ArrayDeque<Block>()
        val visited = mutableSetOf<Block>()
        queue.add(origin)
        var count = 0

        while (queue.isNotEmpty() && count < maxBlocks) {
            val current = queue.removeFirst()
            if (!visited.add(current)) continue

            for (dx in -1..1) for (dy in -1..1) for (dz in -1..1) {
                val nearby = current.location.add(dx.toDouble(), dy.toDouble(), dz.toDouble()).block
                if (targets.contains(nearby.type) && !visited.contains(nearby)) {
                    nearby.breakNaturally(player.inventory.itemInMainHand)
                    queue.add(nearby)
                    count++
                    if (count >= maxBlocks) return
                }
            }
        }
    }
}