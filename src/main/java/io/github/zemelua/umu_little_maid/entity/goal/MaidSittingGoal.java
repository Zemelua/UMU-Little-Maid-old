package io.github.zemelua.umu_little_maid.entity.goal;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MaidSittingGoal extends Goal {
	private final LittleMaidEntity maid;

	public MaidSittingGoal(LittleMaidEntity maid) {
		this.maid = maid;
		this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.maid.isTame() && !this.maid.isInWaterOrBubble() && this.maid.isOnGround()) {
			Entity owner = this.maid.getOwner();
			if (!(owner instanceof LivingEntity ownerLiving)) return true;

			return ownerLiving.getLastHurtByMob() == null && this.maid.isOrderedToSit();
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.maid.isOrderedToSit();
	}

	@Override
	public void start() {
		this.maid.getNavigation().stop();
		this.maid.setSitting(true);
	}

	@Override
	public void stop() {
		this.maid.setSitting(false);
	}
}
