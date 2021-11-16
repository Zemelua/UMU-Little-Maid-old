package io.github.zemelua.umu_little_maid.entity;

import io.github.zemelua.umu_little_maid.UMULittleMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModEntities {
	private static final DeferredRegister<EntityType<?>> REGISTRY = UMULittleMaid.registry(ForgeRegistries.ENTITIES);

	public static final RegistryObject<EntityType<LittleMaidEntity>> LITTLE_MAID = register("little_maid",
			EntityType.Builder.of(LittleMaidEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.5F).clientTrackingRange(10)
	);

	private ModEntities() {
	}

	private static boolean initialized;

	public static void initialize(IEventBus forgeBus, IEventBus modBus) {
		if (initialized) throw new IllegalStateException("Entities already initialized!");

		REGISTRY.register(modBus);
		modBus.addListener(ModEntities::onEntityAttributeCreation);

		initialized = true;
	}

	@SuppressWarnings("SameParameterValue")
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
		return REGISTRY.register(name, () -> builder.build(name));
	}

	private static void onEntityAttributeCreation(final EntityAttributeCreationEvent event) {
		event.put(LITTLE_MAID.get(), LittleMaidEntity.createAttributes().build());
	}
}
