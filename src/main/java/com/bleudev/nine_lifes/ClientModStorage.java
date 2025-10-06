package com.bleudev.nine_lifes;

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

    public static AmethysmEffectInfo amethysm_effect_info = new AmethysmEffectInfo();
}
