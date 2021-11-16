package io.github.zemelua.umu_little_maid.entity.goal;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;

public class MaidLeapGoal extends LeapAtTargetGoal {
	private final LittleMaidEntity maid;

	public MaidLeapGoal(LittleMaidEntity maid) {
		super(maid, 0.4F);

		this.maid = maid;
	}

	@Override
	public boolean canUse() {
		return this.maid.getPersonality().canLeapAtTarget(this.maid) && this.maid.getJob().canLeapAtTarget() && super.canUse();
	}
}
