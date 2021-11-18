package io.github.zemelua.umu_little_maid.inventory;

import io.github.zemelua.umu_little_maid.UMULittleMaid;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModContainers {
	private static final DeferredRegister<MenuType<?>> REGISTRY = UMULittleMaid.registry(ForgeRegistries.CONTAINERS);

	public static final RegistryObject<MenuType<LittleMaidContainer>> LITTLE_MAID = REGISTRY.register("secret_pocket_in_the_skirt", ()
			-> IForgeContainerType.create(LittleMaidContainer::new)
	);

	private ModContainers() {
	}

	private static boolean initialized;

	public static void initialize(IEventBus forgeBus, IEventBus modBus) {
		if (initialized) throw new IllegalStateException("Entities already initialized!");

		REGISTRY.register(modBus);

		initialized = true;
	}
}
