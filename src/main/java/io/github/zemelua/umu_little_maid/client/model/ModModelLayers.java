package io.github.zemelua.umu_little_maid.client.model;

import com.google.common.collect.Sets;
import io.github.zemelua.umu_little_maid.UMULittleMaid;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.Set;

public class ModModelLayers {
	public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

	public static final ModelLayerLocation LITTLE_MAID = register("little_maid", "main");

	@SuppressWarnings("SameParameterValue")
	private static ModelLayerLocation register(String path, String layer) {
		ModelLayerLocation modelLayerLocation = new ModelLayerLocation(UMULittleMaid.location(path), layer);
		if (ALL_MODELS.add(modelLayerLocation)) {
			return modelLayerLocation;
		}

		throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
	}
}
