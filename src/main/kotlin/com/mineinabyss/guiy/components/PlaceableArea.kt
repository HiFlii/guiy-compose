package com.mineinabyss.guiy.components

import androidx.compose.runtime.*
import com.mineinabyss.guiy.components.canvases.CHEST_WIDTH
import com.mineinabyss.guiy.components.canvases.MAX_CHEST_HEIGHT
import com.mineinabyss.guiy.layout.Size
import com.mineinabyss.guiy.modifiers.Modifier
import com.mineinabyss.guiy.modifiers.clickable
import com.mineinabyss.guiy.modifiers.draggable
import com.mineinabyss.guiy.modifiers.onSizeChanged
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import kotlin.math.abs

class ItemGridState(
    val resize: Boolean = false
) {
    val items = mutableStateListOf<ItemStack?>().apply {
        repeat(MAX_CHEST_HEIGHT * CHEST_WIDTH) { add(null) }
    }


}

@Composable
fun rememberItemGridState(): ItemGridState = remember {
    ItemGridState()
}

@Composable
fun ItemGrid(
    state: ItemGridState,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(Size(0, 0)) }
    Grid(modifier.onSizeChanged { size = it }.draggable {
        updatedItems.forEach { (i, item) ->
            cursor!!.amount -= abs(item.amount - (state.items[i]?.amount ?: 0))
            state.items[i] = item
        }
    }) {
        for (x in 0 until size.width) {
            for (y in 0 until size.height) {
                val index = x + y * CHEST_WIDTH
                val item = state.items[index]
                Item(item, Modifier.clickable {
                    val newItem = when (clickType) {
                        ClickType.LEFT -> {
                            if (item?.type == cursor?.type) {
                                if (item == null || cursor == null) return@clickable
                                val total = (item.amount + (cursor?.amount ?: 0))
                                item.amount = total.coerceAtMost(item.maxStackSize)
                                cursor?.amount = total - item.amount
                                item
                            } else {
                                val temp = cursor
                                cursor = item
                                temp
                            }
                        }
                        ClickType.RIGHT -> {
                            when {
                                cursor != null && item != null && cursor!!.type != item.type -> {
                                    val temp = cursor
                                    cursor = item
                                    temp
                                }
                                cursor == null && item != null -> {
                                    val c = item.clone()
                                    item.amount /= 2
                                    cursor = c.apply {
                                        amount -= item.amount
                                    }
                                    item
                                }
                                cursor != null -> {
                                    val newItem = cursor!!.clone().apply { amount = (item?.amount ?: 0) + 1 }
                                    cursor!!.amount -= 1
                                    newItem
                                }
                                else -> return@clickable
                            }
                        }
                        else -> return@clickable
                    }
                    state.items[index] = null
                    state.items[index] = newItem
                })
            }
        }
    }
}
