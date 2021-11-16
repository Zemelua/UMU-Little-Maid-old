package io.github.zemelua.umu_little_maid.client;

import io.github.zemelua.umu_little_maid.client.model.entity.LittleMaidModel;
import io.github.zemelua.umu_little_maid.client.model.ModModelLayers;
import io.github.zemelua.umu_little_maid.client.renderer.entity.LittleMaidRenderer;
import io.github.zemelua.umu_little_maid.entity.ModEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientHandler {
	private final IEventBus forgeBus;
	private final IEventBus modBus;

	private boolean initialized;

	public ClientHandler(IEventBus forgeBus, IEventBus modBus) {
		this.forgeBus = forgeBus;
		this.modBus = modBus;

		this.initialized = false;
	}

	public void initialize() {
		if (initialized) throw new IllegalStateException("Client is already initialized!");

		modBus.addListener(ClientHandler::onRegisterLayerDefinitions);
		modBus.addListener(ClientHandler::onRegisterEntityRenderers);

		this.initialized = true;
	}

	private static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModModelLayers.LITTLE_MAID, LittleMaidModel::createLayer);
	}

	private static void onRegisterEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.LITTLE_MAID.get(), LittleMaidRenderer::new);
	}
}
