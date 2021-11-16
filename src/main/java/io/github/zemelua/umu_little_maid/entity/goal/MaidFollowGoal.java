package io.github.zemelua.umu_little_maid.entity.goal;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class MaidFollowGoal extends Goal {
	private final LittleMaidEntity maid;

	private int recalculationTicks;
	private float waterCostCache;

	public MaidFollowGoal(LittleMaidEntity maid) {
		this.maid = maid;
	}

	@Override
	public boolean canUse() {
		Entity owner = this.maid.getOwner();
		return owner != null && !owner.isSpectator() && !this.maid.isOrderedToSit()
				&& this.maid.distanceTo(owner) >= this.maid.getPersonality().getFollowStartDistance(this.maid);
	}

	@Override
	public boolean canContinueToUse() {
		Entity owner = this.maid.getOwner();
		return owner != null && !this.maid.getNavigation().isDone() && !this.maid.isOrderedToSit()
				&& this.maid.distanceTo(owner) >= this.maid.getPersonality().getFollowStopDistance(this.maid);
	}

	@Override
	public void start() {
		this.recalculationTicks = 0;
		this.waterCostCache = this.maid.getPathfindingMalus(BlockPathTypes.WATER);
		this.maid.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	@Override
	public void tick() {
		Entity owner = this.maid.getOwner();
		if (owner == null) return;

		this.maid.getLookControl().setLookAt(this.maid.getOwner(), 10.0F, (float)this.maid.getMaxHeadXRot());
		this.recalculationTicks--;

		if (recalculationTicks <= 0) {
			this.recalculationTicks = 10;

			if (!this.maid.isLeashed() && !this.maid.isPassenger()) {
				if (this.maid.distanceTo(owner) >= 12.0D) {

				}
			}
		}
	}

	@Override
	public void stop() {
		this.maid.getNavigation().stop();
		this.maid.setPathfindingMalus(BlockPathTypes.WATER, this.waterCostCache);
	}
}
