package io.github.zemelua.umu_little_maid.client.screen;

import io.github.zemelua.umu_little_maid.inventory.ModContainers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public final class ModScreens {
	private ModScreens() {
	}

	private static boolean initialized;

	public static void initialize(IEventBus forgeBus, IEventBus modBus) {
		if (initialized) throw new IllegalStateException("Entities already initialized!");

		modBus.addListener(ModScreens::onFMLClientSetup);

		initialized = true;
	}

	private static void onFMLClientSetup(final FMLClientSetupEvent event) {
		MenuScreens.register(ModContainers.LITTLE_MAID.get(), LittleMaidScreen::new);
	}
}
