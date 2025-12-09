package com.bleudev.nine_lifes.custom.entity;

import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandLookAroundGoal;
import com.bleudev.nine_lifes.networking.payloads.ArmorStandHitEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import static com.bleudev.nine_lifes.compat.VersionCompat.getPosCompat;
import static com.bleudev.nine_lifes.compat.VersionCompat.getWorldCompat;

public class WanderingArmorStandEntity extends PathfinderMob {
    private static final TrackedData<Boolean> IS_ALIVE;

    public WanderingArmorStandEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
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

    public static AttributeSupplier.Builder createLivingAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.FOLLOW_RANGE)
                .add(Attributes.TEMPT_RANGE);
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
    public void kill(ServerWorld world) {
        if (!world.isClient()) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (source.getAttacker() instanceof ServerPlayerEntity player)
            ServerPlayNetworking.send(player, new ArmorStandHitEvent(getPosCompat(this)));
        return source.isOf(DamageTypes.GENERIC_KILL);
    }

    @Override
    public boolean isInvulnerableTo(ServerWorld world, DamageSource source) {
        return !source.isOf(DamageTypes.GENERIC_KILL);
    }

    public boolean cannotWander() {
        return (!this.dataTracker.get(IS_ALIVE));
    }

    public void setWander(boolean is_alive) {
        this.dataTracker.set(IS_ALIVE, is_alive);
    }

    private static void spawnParticle(ParticleEffect particle, ServerWorld world, Vec3d pos) {
        double offsetX = (world.random.nextDouble() - 0.5) * 2.0;
        double offsetY = world.random.nextDouble() * 2.0;
        double offsetZ = (world.random.nextDouble() - 0.5) * 2.0;

        world.spawnParticles(particle,
            pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ,
            1, 0, 0, 0, 1
        );
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getMainHandStack();
        // Eat
        if (stack.isOf(Items.AMETHYST_SHARD)) {
            if (getWorldCompat(this) instanceof ServerWorld world) {
                var pos = getPosCompat(this);
                for (int i = 0; i < 3; i++)
                    spawnParticle(ParticleTypes.HEART, world, pos);
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
