package com.mineinabyss.guiy.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import com.mineinabyss.guiy.nodes.BoxNode
import com.mineinabyss.guiy.nodes.GuiyNodeApplier
import com.mineinabyss.guiy.nodes.ItemNode
import org.bukkit.inventory.ItemStack

@Composable
fun Item(itemStack: ItemStack, onClick: (() -> Unit)? = null) {
    ComposeNode<ItemNode, GuiyNodeApplier>(
        factory = { ItemNode() },
        update = {
            set(itemStack) { this.item = itemStack }
            set(onClick) { this.onClick = onClick }
        }
    )
}
