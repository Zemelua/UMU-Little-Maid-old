package io.github.zemelua.umu_little_maid.entity.goal.target;

import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class MaidOwnerHurtByTargetGoal extends TargetGoal {
	@Nullable private LivingEntity ownerHurtBy = null;
	private int timeStamp;

	public MaidOwnerHurtByTargetGoal(LittleMaidEntity maid) {
		super(maid, false);
		this.setFlags(EnumSet.of(Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (!(this.mob instanceof LittleMaidEntity mobMaid)) return false;
		if (!mobMaid.getJob().isActive() || !mobMaid.getJob().isMad()) return false;

		if (mobMaid.isTame() && !mobMaid.isOrderedToSit()) {
			Entity owner = mobMaid.getOwner();

			if (owner instanceof LivingEntity ownerLiving) {
				this.ownerHurtBy = ownerLiving.getLastHurtByMob();

				if (mobMaid.getJob().isActive()) {
					if (ownerLiving instanceof Player ownerPlayer && this.ownerHurtBy instanceof Player hurtByPlayer
							&& !ownerPlayer.canHarmPlayer(hurtByPlayer)) {
						return false;
					} else if (ownerLiving instanceof TamableAnimal ownerTamable && ownerTamable.isTame()) {
						return false;
					} else if (ownerLiving instanceof AbstractHorse ownerHorse && ownerHorse.isTamed()) {
						return false;
					}
				}

				return ownerLiving.getLastHurtByMobTimestamp() != timeStamp && this.canAttack(this.ownerHurtBy, TargetingConditions.DEFAULT);
			}
		}

		return false;
	}

	@Override
	public void start() {
		this.mob.setTarget(this.ownerHurtBy);
		if (!(this.mob instanceof LittleMaidEntity mobMaid)) return;

		Entity owner = mobMaid.getOwner();
		if (owner instanceof LivingEntity ownerLiving) {
			this.timeStamp = ownerLiving.getLastHurtByMobTimestamp();
		}

		super.start();
	}
}
