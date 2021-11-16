package io.github.zemelua.umu_little_maid.entity.maid.personality;

public final class MaidPersonalities {
	public static final MaidPersonality BRAVERY = new MaidPersonality(new MaidPersonality.Builder()
			.setLeap()
	);
	public static final MaidPersonality TSUNDERE = new MaidPersonality(new MaidPersonality.Builder()
			.setCurt().setFollowStartDistance(13.8D).setFollowStopDistance(5.0D)
	);
	public static final MaidPersonality SHY = new ShyPersonality(new MaidPersonality.Builder()
			.setFollowStartDistance(12.0D).setFollowStopDistance(5.17D)
	);
}
