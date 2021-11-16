package io.github.zemelua.umu_little_maid.entity.goal;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class MaidAvoidGoal extends Goal {
	private static final double AVOID_RANGE = 24.0D;
	private static final double WALK_SPEED = 1.0D;
	private static final double SPRINT_SPEED = 1.2D;

	private final LittleMaidEntity maid;
	private final TargetingConditions targeting;
	private LivingEntity toAvoid;
	private Path path;

	public MaidAvoidGoal(LittleMaidEntity maid) {
		this.maid = maid;
		this.targeting = TargetingConditions.forCombat().range(AVOID_RANGE).selector(EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
	}

	@Override
	public boolean canUse() {
		if (!this.maid.getJob().isAvoid() || this.maid.isOrderedToSit()) return false;

		this.toAvoid = this.maid.level.getNearestEntity(this.maid.level.getEntitiesOfClass(Mob.class, this.maid.getBoundingBox().inflate(24.0D, 3.0D, 24.0D), (mob
				-> !mob.getType().getCategory().isFriendly()
		)), this.targeting, this.maid, this.maid.getX(), this.maid.getY(), this.maid.getZ());
		if (this.toAvoid == null) return false;

		Entity owner = this.maid.getOwner();
		Vec3 awayPos;
		if (owner == null) {
			awayPos = DefaultRandomPos.getPosAway(this.maid, 16, 7, this.toAvoid.position());
		} else {
			Vec3 behindOwnerPos = toAvoid.position().subtract(owner.position()).normalize();
			awayPos = new Vec3(owner.getX() + behindOwnerPos.x(), owner.position().y(), owner.getZ() + behindOwnerPos.z());
		}

		if (awayPos != null && this.toAvoid.distanceToSqr(awayPos) >= this.toAvoid.distanceToSqr(this.maid)) {
			this.path = this.maid.getNavigation().createPath(new BlockPos(awayPos), 0);

			return this.path != null;
		}

		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return !this.maid.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.maid.getNavigation().moveTo(this.path, WALK_SPEED);
	}

	@Override
	public void tick() {
		if (this.maid.distanceToSqr(this.toAvoid) < 49.0D) {
			this.maid.getNavigation().setSpeedModifier(SPRINT_SPEED);
		} else {
			this.maid.getNavigation().setSpeedModifier(WALK_SPEED);
		}
	}

	@Override
	public void stop() {
		this.toAvoid = null;
	}
}
