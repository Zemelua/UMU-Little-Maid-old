package io.github.zemelua.umu_little_maid.inventory.slot;

import com.mojang.datafixers.util.Pair;
import io.github.zemelua.umu_little_maid.client.screen.LittleMaidScreen;
import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MaidArmorSlot extends SlotItemHandler {
	private final EquipmentSlot slot;
	private final LittleMaidEntity maid;

	public MaidArmorSlot(EquipmentSlot slot, LittleMaidEntity maid, int xPosition, int yPosition) {
		super(new ItemStackHandler(NonNullList.of(ItemStack.EMPTY, maid.getItemBySlot(slot))), 0, xPosition, yPosition);

		this.slot = slot;
		this.maid = maid;
	}

	@Override
	public boolean mayPlace(@Nonnull ItemStack itemStack) {
		return super.mayPlace(itemStack) && itemStack.canEquip(this.slot, this.maid);
	}

	@Override
	public boolean mayPickup(Player player) {
		return !(player.isCreative() && EnchantmentHelper.hasBindingCurse(this.getItem())) && super.mayPickup(player);
	}

	@Nullable
	@Override
	public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
		return Pair.of(InventoryMenu.BLOCK_ATLAS, LittleMaidScreen.EMPTY_ARMOR_SLOT_TEXTURES[this.slot.getIndex()]);
	}
}
