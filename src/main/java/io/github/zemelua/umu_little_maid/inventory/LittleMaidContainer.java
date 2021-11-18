package io.github.zemelua.umu_little_maid.inventory;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import io.github.zemelua.umu_little_maid.inventory.slot.MaidArmorSlot;
import io.github.zemelua.umu_little_maid.inventory.slot.MaidHeldItemSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class LittleMaidContainer extends AbstractContainerMenu {
	private final LittleMaidEntity maid;

	@SuppressWarnings("ConstantConditions")
	protected LittleMaidContainer(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
		this(id, playerInventory, (LittleMaidEntity) playerInventory.player.level.getEntity(buffer.readVarInt()));
	}

	public LittleMaidContainer(int id, Inventory playerInventory, LittleMaidEntity maid) {
		super(ModContainers.LITTLE_MAID.get(), id);

		this.maid = maid;

		this.addSlot(new MaidHeldItemSlot(this.maid.getInventory(), 0, 8, 18));

		for (int i = 0; i < LittleMaidEntity.ARMORS.length; i++) {
			this.addSlot(new MaidArmorSlot(
					LittleMaidEntity.ARMORS[i], this.maid, this.maid.getArmorSlots().iterator(),
					LittleMaidEntity.ARMORS[i].getIndex(), 8, 36 + i * 18)
			);
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
	}

	@Override
	public boolean stillValid(Player player) {
		return this.maid.isAlive() && this.maid.distanceTo(player) < 8.0F;
	}

	public LittleMaidEntity getMaid() {
		return this.maid;
	}
}
