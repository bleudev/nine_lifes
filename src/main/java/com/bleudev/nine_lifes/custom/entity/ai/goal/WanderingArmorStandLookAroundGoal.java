package com.bleudev.nine_lifes.custom.entity.ai.goal;

import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

/*
Copied from net.minecraft.entity.ai.goal.LookAroundGoal with some improvements
 */
public class WanderingArmorStandLookAroundGoal extends Goal {
    protected final WanderingArmorStand mob;
    private double deltaX;
    private double deltaZ;
    private int lookTime;

    public WanderingArmorStandLookAroundGoal(WanderingArmorStand mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public boolean canStart() {
        if (mob.cannotWander()) return false;
        return this.mob.getRandom().nextFloat() < 0.02F;
    }

    public boolean shouldContinue() {
        return this.lookTime >= 0;
    }

    public void start() {
        double d = (Math.PI * 2D) * this.mob.getRandom().nextDouble();
        this.deltaX = Math.cos(d);
        this.deltaZ = Math.sin(d);
        this.lookTime = 20 + this.mob.getRandom().nextInt(20);
    }

    public boolean shouldRunEveryTick() {
        return true;
    }

    public void tick() {
        --this.lookTime;
        this.mob.getLookControl().lookAt(this.mob.getX() + this.deltaX, this.mob.getEyeY(), this.mob.getZ() + this.deltaZ);
    }
}
