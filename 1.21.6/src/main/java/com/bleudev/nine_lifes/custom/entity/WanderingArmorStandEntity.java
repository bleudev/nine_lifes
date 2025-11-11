package com.bleudev.nine_lifes.custom.entity;

import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandLookAroundGoal;
import com.bleudev.nine_lifes.networking.payloads.ArmorStandHitEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nine_lifes.compat.VersionCompat.getPosCompat;
import static com.bleudev.nine_lifes.compat.VersionCompat.getWorldCompat;

public class WanderingArmorStandEntity extends PathAwareEntity {
    private static final TrackedData<Boolean> IS_ALIVE;

    public WanderingArmorStandEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(1);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_ALIVE, false);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new TemptGoal(this, 0.4, (stack) -> stack.isOf(Items.AMETHYST_SHARD), false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 0.3) {
            @Override
            public boolean canStart() {
                if (((WanderingArmorStandEntity) mob).cannotWander()) return false;
                return super.canStart();
            }
        });
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6f) {
            @Override
            public boolean canStart() {
                if (((WanderingArmorStandEntity) mob).cannotWander()) return false;
                return super.canStart();
            }
        });
        this.goalSelector.add(4, new WanderingArmorStandLookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createLivingAttributes() {
        return LivingEntity.createLivingAttributes()
                           .add(EntityAttributes.MAX_HEALTH, 1)
                           .add(EntityAttributes.FOLLOW_RANGE)
                           .add(EntityAttributes.TEMPT_RANGE);
    }

    @Override
    public boolean canUsePortals(boolean allowVehicles) {
        return false;
    }
    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }
    @Override
    protected boolean canGlide() {
        return false;
    }
    @Override
    public boolean canBreatheInWater() {
        return true;
    }
    @Override
    public boolean isPushable() {
        return false;
    }
    @Override
    protected void pushAway(Entity entity) {
    }
    @Override
    public void pushAwayFrom(Entity entity) {
    }
    @Override
    public void kill(@NotNull ServerWorld world) {
        if (!world.isClient) {
            this.remove(RemovalReason.KILLED);
        }
    }
    @Override
    public boolean damage(ServerWorld world, @NotNull DamageSource source, float amount) {
        if (source.getAttacker() instanceof ServerPlayerEntity player)
            ServerPlayNetworking.send(player, new ArmorStandHitEvent(getPosCompat(this)));
        return source.isOf(DamageTypes.GENERIC_KILL);
    }

    @Override
    public boolean isInvulnerableTo(ServerWorld world, @NotNull DamageSource source) {
        return !source.isOf(DamageTypes.GENERIC_KILL);
    }

    public boolean canWander() {
        return this.dataTracker.get(IS_ALIVE);
    }
    public boolean cannotWander() {
        return !canWander();
    }

    public void setWander(boolean is_alive) {
        this.dataTracker.set(IS_ALIVE, is_alive);
    }

    public ActionResult interactMob(@NotNull PlayerEntity player, Hand hand) {
        ItemStack stack = player.getMainHandStack();
        // Eat
        if (stack.isOf(Items.AMETHYST_SHARD)) {
            if (getWorldCompat(this) instanceof ServerWorld world) {
                var pos = getPosCompat(this);
                for (int i = 0; i < 3; i++) {
                    double offsetX = (world.random.nextDouble() - 0.5) * 2;
                    double offsetY = world.random.nextDouble() * 2;
                    double offsetZ = (world.random.nextDouble() - 0.5) * 2;

                    world.spawnParticles(ParticleTypes.HEART,
                        pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ,
                        1, 0, 0, 0, 1
                    );
                }
            }

            // Consume amethyst shard
            if (!player.isCreative()) {
                if (stack.getCount() == 1)
                    player.setStackInHand(hand, ItemStack.EMPTY);
                else {
                    stack.setCount(stack.getCount() - 1);
                    player.setStackInHand(hand, stack);
                }
            }

            setWander(true);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    static {
        IS_ALIVE = DataTracker.registerData(WanderingArmorStandEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
