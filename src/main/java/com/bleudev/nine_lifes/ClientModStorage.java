package com.bleudev.nine_lifes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.PI;

@Environment(EnvType.CLIENT)
public class ClientModStorage {
    public static int lives = 9;

    public static int armor_stand_hit_event_ticks = 0;
    public static boolean armor_stand_hit_event_running = false;

    public static float max_whiteness_screen = 0f;
    public static int max_whiteness_screen_ticks = 0;
    public static int whiteness_screen_ticks = 0;
    public static boolean whiteness_screen_running = false;


    public static float whiteness = 0f;
    public static float redness = 0f;

    public static float amethysm_whiteness = 0f;
    public static float amethysm_purpleness = 0f;

    public static float armor_stand_hit_redness = 0f;

    public static class AmethysmEffectInfo {
        private boolean running = false;
        private int ticks = 0;
        private int duration;

        public void start(int duration) {
            running = true;
            ticks = 0;
            this.duration = duration;
        }

        public void tick() {
            if (running) {
                if (ticks < duration) {
                    if (ticks % 15 == 0)
                        question_marks.add(new DynamicQuestionMarkInfo(0.5f, 0.5f, .25f, 40));

                    final int WHITENESS_START = 5;
                    final int WHITENESS_END_FROM_DURATION_END = 10;

                    if (ticks < WHITENESS_START)
                        amethysm_whiteness = (float) ticks / WHITENESS_START;
                    else if (ticks < duration - WHITENESS_END_FROM_DURATION_END)
                        amethysm_whiteness = 1f - (float) (ticks - WHITENESS_START) / (duration - WHITENESS_START - WHITENESS_END_FROM_DURATION_END);

                    if (ticks >= duration - WHITENESS_END_FROM_DURATION_END)
                        amethysm_purpleness = 1f - (float) (ticks - duration + WHITENESS_END_FROM_DURATION_END) / WHITENESS_END_FROM_DURATION_END;
                    else if (ticks >= WHITENESS_START)
                        amethysm_purpleness = 1f;
                    ticks++;
                } else {
                    running = false;
                    amethysm_whiteness = 0f;
                    amethysm_purpleness = 0f;
                }
            }
        }
    }

    public static class DynamicQuestionMarkInfo {
        private static float lastAngle = 0f;

        private final Vector2d xy0p;
        private final Vector2d direction;
        private float time;
        private final int duration;

        public DynamicQuestionMarkInfo(float x0p, float y0p, Vector2d direction, int duration) {
            this.xy0p = new Vector2d(x0p, y0p);
            this.direction = direction;
            this.time = 0f;
            this.duration = duration;
        }

        public DynamicQuestionMarkInfo(float x0p, float y0p, float speed, int duration) {
            this(x0p, y0p, new Vector2d(0), duration);

            float angle = new Random().nextFloat(0f, (float) (2*PI));
            if ((lastAngle > PI && angle > PI) || (lastAngle < PI && angle < PI))
                angle = (float) (2*PI - angle);
            lastAngle = angle;

            this.direction.x = Math.cos(angle) * 2 * speed;
            this.direction.y = Math.sin(angle) * speed;
        }

        private float getAlpha() {
            final int FADE_IN = 30;
            final int FADE_OUT = 10;
            final float max_alpha = .5f;

            Function<Float, Float> clamp = a -> MathHelper.lerp(MathHelper.clamp(a, 0f, 1f), 0f, max_alpha);

            if (time <= FADE_IN) return clamp.apply(time / FADE_IN);
            if (time <= duration - FADE_OUT) return max_alpha;
            return clamp.apply(1f - (time - duration + FADE_OUT) / FADE_OUT);
        }

        public float getOffset() {
            return MathHelper.lerp(time / duration, 0f, 5f);
        }

        public float getTime() {
            return time;
        }

        public int getDuration() {
            return duration;
        }

        public Vec3d tick(float delta_tick_progress) {
            Vec3d res = new Vec3d(this.direction.x, this.direction.y, 0)
                .multiply(this.time / 20)
                .add(this.xy0p.x, this.xy0p.y, getAlpha());
            this.time += delta_tick_progress;
            return res;
        }
    }

    public static AmethysmEffectInfo amethysm_effect_info = new AmethysmEffectInfo();
    public static ArrayList<DynamicQuestionMarkInfo> question_marks = new ArrayList<>();
}
