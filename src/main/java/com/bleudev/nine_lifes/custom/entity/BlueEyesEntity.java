package com.bleudev.nine_lifes.custom.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class BlueEyesEntity extends LivingEntity {
    private boolean oriented = false;
    public BlueEyesEntity(EntityType<BlueEyesEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public void orient() {
        List<? extends PlayerEntity> players = getWorld().getPlayers();
        if (players == null || players.isEmpty()) return;

        Optional<? extends PlayerEntity> player;
        if (players.size() > 1)
            player = players.stream().reduce((player1, player2) -> {
                boolean bl = player1.distanceTo(this) < player2.distanceTo(this);
                return bl ? player1 : player2;
            });
        else player = Optional.ofNullable(players.getFirst());

        if (player.isPresent()) {
            var pl = player.get();
            double dx = pl.getX() - this.getX();
            double dz = pl.getZ() - this.getZ();
            float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90F);

            yaw = MathHelper.wrapDegrees(yaw);
            this.setYaw(yaw);
            this.setHeadYaw(yaw);
            this.setBodyYaw(yaw);
            this.setPitch(0f);

            oriented = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            this.setVelocity(0, 0, 0);
            this.fallDistance = 0;
            if (!oriented)
                orient();
        }
    }

    @Override
    public void kill(ServerWorld world) {
        if (!world.isClient) {
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return source.isOf(DamageTypes.GENERIC_KILL);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public boolean isInvulnerableTo(ServerWorld world, DamageSource source) {
        return !source.isOf(DamageTypes.GENERIC_KILL);
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public ItemStack getMainHandStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getOffHandStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }
}
