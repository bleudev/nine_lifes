package com.bleudev.nine_lifes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2d;

import java.util.ArrayList;

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

        private float getAlpha() {
//            if (this.time >= duration) return 0f;
            return 1f;
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
            System.out.println(time + " " + res);
            return res;
        }
    }

    public static AmethysmEffectInfo amethysm_effect_info = new AmethysmEffectInfo();
    public static ArrayList<DynamicQuestionMarkInfo> question_marks = new ArrayList<>();

    static {
        question_marks.add(new DynamicQuestionMarkInfo(0.5f, 0.5f, new Vector2d(-0.03, -0.015), 200));
    }
}
