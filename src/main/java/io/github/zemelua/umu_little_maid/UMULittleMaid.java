package io.github.zemelua.umu_little_maid;

import io.github.zemelua.umu_little_maid.client.ClientHandler;
import io.github.zemelua.umu_little_maid.entity.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(UMULittleMaid.MOD_ID)
public class UMULittleMaid {
	public static final String MOD_ID = "umu_little_maid";
	public static final Logger LOGGER = LogManager.getLogger();

	public UMULittleMaid() {
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModEntities.initialize(forgeBus, modBus);

		ClientHandler clientHandler = new ClientHandler(forgeBus, modBus);
		clientHandler.initialize();
	}

	public static ResourceLocation location(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> registry(IForgeRegistry<T> type) {
		return DeferredRegister.create(type, MOD_ID);
	}
}
