package io.github.zemelua.umu_little_maid.entity;

import io.github.zemelua.umu_little_maid.entity.goal.MaidAttackGoal;
import io.github.zemelua.umu_little_maid.entity.goal.MaidAvoidGoal;
import io.github.zemelua.umu_little_maid.entity.goal.MaidLeapGoal;
import io.github.zemelua.umu_little_maid.entity.goal.MaidSittingGoal;
import io.github.zemelua.umu_little_maid.entity.goal.target.MaidOwnerHurtByTargetGoal;
import io.github.zemelua.umu_little_maid.entity.maid.job.MaidJob;
import io.github.zemelua.umu_little_maid.entity.maid.job.MaidJobs;
import io.github.zemelua.umu_little_maid.entity.maid.personality.MaidPersonalities;
import io.github.zemelua.umu_little_maid.entity.maid.personality.MaidPersonality;
import io.github.zemelua.umu_little_maid.inventory.MaidContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class LittleMaidEntity extends PathfinderMob implements OwnableEntity {
	public static final double FOLLOW_START_DISTANCE = 10.0F;
	public static final int MAX_INTIMACY = 300;
	public static final EquipmentSlot[] ARMORS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.FEET};

	private static final EntityDataAccessor<Integer> DATA_PERSONALITY = SynchedEntityData.defineId(LittleMaidEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> DATA_TAME = SynchedEntityData.defineId(LittleMaidEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DATA_SITTING = SynchedEntityData.defineId(LittleMaidEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER = SynchedEntityData.defineId(LittleMaidEntity.class, EntityDataSerializers.OPTIONAL_UUID);

	private final IItemHandler inventory;

	private MaidJob job;
	private boolean orderedToSit = false;
	private int intimacy;

	public LittleMaidEntity(EntityType<LittleMaidEntity> type, Level world) {
		super(type, world);

		this.inventory = new ItemStackHandler(15);

		this.job = MaidJobs.NONE;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();

		this.entityData.define(DATA_PERSONALITY, 0);
		this.entityData.define(DATA_TAME, false);
		this.entityData.define(DATA_SITTING, false);
		this.entityData.define(DATA_OWNER, Optional.empty());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MaidSittingGoal(this));
		this.goalSelector.addGoal(3, new MaidAvoidGoal(this));
		this.goalSelector.addGoal(4, new MaidLeapGoal(this));
		this.goalSelector.addGoal(5, new MaidAttackGoal(this));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new MaidOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());

		super.registerGoals();
	}

	protected static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	public void tick() {
		super.tick();

		this.setJob(MaidJobs.getByItem(this.getMainHandItem()));
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack interactItem = player.getItemInHand(hand);
		InteractionResult defaultResult = super.mobInteract(player, hand);

		if (this.isTame()) {
			if (player.isCrouching()) {
				if (!this.level.isClientSide()) {
					if (player instanceof ServerPlayer playerServer) {
						NetworkHooks.openGui(playerServer, new SimpleMenuProvider((id, inventory, playerArg)
								-> new MaidContainer(id, inventory, this), new TranslatableComponent("tex")),
								buffer -> buffer.writeVarInt(this.getId())
						);
					}
				}
			} else {
				if (player == this.getOwner() && !defaultResult.consumesAction()) {
					if (!this.level.isClientSide()) {
						this.navigation.stop();
						this.setTarget(null);
						this.setOrderedToSit(!this.isOrderedToSit());
					}

					return InteractionResult.sidedSuccess(!this.level.isClientSide());
				}
			}

		} else {
			if (interactItem.is(Items.SUGAR)) {
				if (!this.level.isClientSide()) {
					if (!player.getAbilities().instabuild) {
						interactItem.shrink(1);
					}

					if (this.random.nextInt(3) == 0) {
						this.tame(player);
						this.getNavigation().stop();
						this.setTarget(null);

						this.level.broadcastEntityEvent(this, (byte) 7);
					} else {
						this.level.broadcastEntityEvent(this, (byte) 6);
					}
				}

				return InteractionResult.sidedSuccess(!this.level.isClientSide());
			}
		}


		return super.mobInteract(player, hand);
	}

	public void tame(Player player) {
		this.setTame(true);
		this.setOwner(player);
		this.setTarget(null);
		this.setIntimacy(0);
	}

	@Override
	public void handleEntityEvent(byte index) {
		if (index == (byte) 6) {
			this.spawnTamingParticles(ParticleTypes.SMOKE);
		} else if (index == (byte) 7) {
			this.spawnTamingParticles(ParticleTypes.HEART);
		} else {
			super.handleEntityEvent(index);
		}
	}

	protected void spawnTamingParticles(ParticleOptions particle) {
		for(int i = 0; i < 7; ++i) {
			double xGauss = this.random.nextGaussian() * 0.02D;
			double yGauss = this.random.nextGaussian() * 0.02D;
			double zGauss = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(particle, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), xGauss, yGauss, zGauss);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);

		tag.putInt("personality", this.getPersonalityID());
		tag.putBoolean("isTame", this.isTame());
		tag.putBoolean("isSitting", this.isSitting());
		if (this.getOwnerUUID() != null) {
			tag.putUUID("owner", this.getOwnerUUID());
		}
		ContainerHelper.saveAllItems(tag, this.getInventoryList());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		this.setPersonalityID(tag.getInt("personality"));
		this.setTame(tag.getBoolean("isTame"));
		this.setSitting(tag.getBoolean("isSitting"));
		if (tag.contains("owner")) {
			this.setOwnerUUID(tag.getUUID("owner"));
		}
		NonNullList<ItemStack> inventoryList = NonNullList.of(ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, inventoryList);
		this.setInventoryList(inventoryList);

	}

	public int getPersonalityID() {
		return this.entityData.get(DATA_PERSONALITY);
	}

	public void setPersonalityID(int personalityID) {
		this.entityData.set(DATA_PERSONALITY, personalityID);
	}

	public MaidPersonality getPersonality() {
		return MaidPersonalities.getByID(this.getPersonalityID());
	}

	public boolean isTame() {
		return this.entityData.get(DATA_TAME);
	}

	public void setTame(boolean tame) {
		this.entityData.set(DATA_TAME, tame);
	}

	public boolean isSitting() {
		return this.entityData.get(DATA_SITTING);
	}

	public void setSitting(boolean sitting) {
		this.entityData.set(DATA_SITTING, sitting);
	}

	@Nullable
	@Override
	public UUID getOwnerUUID() {
		return this.entityData.get(DATA_OWNER).orElse(null);
	}

	public void setOwnerUUID(@Nullable UUID ownerID) {
		this.entityData.set(DATA_OWNER, Optional.ofNullable(ownerID));
	}

	@Nullable
	@Override
	public Entity getOwner() {
		UUID ownerID = this.getOwnerUUID();

		if (ownerID != null) {
			return this.level.getPlayerByUUID(ownerID);
		}

		return null;
	}

	public void setOwner(@Nullable Entity owner) {
		if (owner != null) {
			this.setOwnerUUID(owner.getUUID());
		}
	}

	public IItemHandler getInventory() {
		return this.inventory;
	}

	public NonNullList<ItemStack> getInventoryList() {
		IItemHandler inventory = this.getInventory();
		NonNullList<ItemStack> inventoryList = NonNullList.withSize(inventory.getSlots(), ItemStack.EMPTY);

		for (int i = 0; i < inventory.getSlots(); i++) {
			inventoryList.set(i, inventory.getStackInSlot(i));
		}

		return inventoryList;
	}

	public void setInventoryList(NonNullList<ItemStack> inventoryList) {
		IItemHandler inventory = this.getInventory();
		if (!(inventory instanceof ItemStackHandler inventoryHandler)) return;

		for (int i = 0; i < inventoryList.size(); i++) {
			inventoryHandler.setStackInSlot(i, inventoryList.get(i));
		}
	}

	public MaidJob getJob() {
		return this.job;
	}

	public void setJob(MaidJob job) {
		this.job = job;
	}

	public boolean isOrderedToSit() {
		return this.orderedToSit;
	}

	public void setOrderedToSit(boolean orderedToSit) {
		this.orderedToSit = orderedToSit;
	}

	public int getIntimacy() {
		return this.intimacy;
	}

	public void setIntimacy(int intimacy) {
		this.intimacy = intimacy;
	}
}
