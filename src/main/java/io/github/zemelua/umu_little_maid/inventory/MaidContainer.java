package io.github.zemelua.umu_little_maid.inventory;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import io.github.zemelua.umu_little_maid.inventory.slot.MaidArmorSlot;
import io.github.zemelua.umu_little_maid.inventory.slot.MaidHeldItemSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MaidContainer extends AbstractContainerMenu {
	private final LittleMaidEntity maid;

	@SuppressWarnings("ConstantConditions")
	protected MaidContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(id, playerInventory, (LittleMaidEntity) playerInventory.player.level.getEntity(buffer.readVarInt()));
	}

	public MaidContainer(int id, Inventory playerInventory, LittleMaidEntity maid) {
		super(ModContainers.LITTLE_MAID.get(), id);

		this.maid = maid;

		this.addSlot(new MaidHeldItemSlot(this.maid.getMainHandItem(), 0, 8, 18));

		for (int i = 0; i < LittleMaidEntity.ARMORS.length; i++) {
			this.addSlot(new MaidArmorSlot(LittleMaidEntity.ARMORS[i], this.maid, 8, 36 + i * 18));
		}

		IItemHandler maidInventory = this.maid.getInventory();
		int inventorySize = maidInventory.getSlots();
		for (int i = 0; i < 3 && (i + 1) * 5 <= inventorySize; i++) {
			for (int j = 0; j < 5 && i * 5 + j + 1 <= inventorySize; j++) {
				this.addSlot(new SlotItemHandler(maidInventory, i * 5 + j, 80 + j * 18, 18 + i * 18));
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, 9 + i * 9 + j, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addSlotListener(this.new MaidContainerListener());
	}

	@Override
	public boolean stillValid(Player player) {
		return this.maid.isAlive() && this.maid.distanceTo(player) < 8.0F;
	}

	public LittleMaidEntity getMaid() {
		return this.maid;
	}

	private class MaidContainerListener implements ContainerListener {
		@Override
		public void slotChanged(AbstractContainerMenu container, int slot, ItemStack itemStack) {
			LittleMaidEntity maid = MaidContainer.this.maid;

			if (slot == 0) maid.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
			else if (slot == 1) maid.setItemSlot(EquipmentSlot.HEAD, itemStack);
			else if (slot == 2) maid.setItemSlot(EquipmentSlot.FEET, itemStack);
		}

		@Override
		public void dataChanged(AbstractContainerMenu container, int slot, int int0) {
		}
	}
}
