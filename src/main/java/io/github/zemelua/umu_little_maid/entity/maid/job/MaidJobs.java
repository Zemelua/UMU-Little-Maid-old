package io.github.zemelua.umu_little_maid.entity.maid.job;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class MaidJobs {
	public static final MaidJob FENCER = new MaidJob(new MaidJob.Builder().setActive().setLeap());
	public static final MaidJob NONE = new MaidJob(new MaidJob.Builder());

	private static final Map<Predicate<ItemStack>, MaidJob> REGISTRY = new HashMap<>() {
		{
			put(itemStack -> itemStack.getItem() instanceof SwordItem, FENCER);
		}
	};

	public static MaidJob getByItem(ItemStack heldItem) {
		for (Predicate<ItemStack> itemPredicate : REGISTRY.keySet()) {
			if (itemPredicate.test(heldItem)) return REGISTRY.get(itemPredicate);
		}

		return NONE;
	}
}
