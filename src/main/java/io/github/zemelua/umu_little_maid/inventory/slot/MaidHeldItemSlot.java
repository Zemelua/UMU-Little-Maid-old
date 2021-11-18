package io.github.zemelua.umu_little_maid.inventory.slot;

import com.mojang.datafixers.util.Pair;
import io.github.zemelua.umu_little_maid.client.screen.LittleMaidScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class MaidHeldItemSlot extends SlotItemHandler {
	public MaidHeldItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Nullable
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, LittleMaidScreen.EMPTY_HELD_SLOT_TEXTURE);
	}
}
