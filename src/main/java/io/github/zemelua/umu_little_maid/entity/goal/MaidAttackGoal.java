package io.github.zemelua.umu_little_maid.entity.goal;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class MaidAttackGoal extends MeleeAttackGoal {
	public MaidAttackGoal(LittleMaidEntity maid) {
		super(maid, 1.17D, true);
	}

	@Override
	public boolean canUse() {
		if (this.mob instanceof LittleMaidEntity mobMaid) {
			return (mobMaid.getJob().isActive() || mobMaid.getJob().isMad()) && super.canUse();
		}

		return false;
	}
}
