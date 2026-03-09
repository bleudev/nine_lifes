package com.bleudev.nine_lifes.interfaces.mixin;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface CustomServerPlayer {
    /**
     * Get player lifes
     *
     * <p><i>Note: This method is unstable and use of this method is not recommended. Use {@code ServerPlayer.lifes} inject instead.</i></p>
     *
     * @return Player lifes
     * */
    int nl$getLifes();
    /**
     * Set player lifes to {@code newLifesCount}
     *
     * <p><i>Note: This method is unstable and use of this method is not recommended. Use {@code ServerPlayer.setLifes()} inject instead.</i></p>
     *
     * @param newLifesCount New lifes count
     *
     * @throws IllegalArgumentException {@code newLifesCount} out of range[0; 9]
     * */
    void nl$setLifes(int newLifesCount) throws IllegalArgumentException;
    /**
     * Get play time with specified {@code lifesCount}
     *
     * <p><i>Note: This method is unstable and use of this method is not recommended. Use {@code ServerPlayer.lifesPlayTime()} inject instead.</i></p>
     *
     * @param lifesCount Lifes count to get play time with
     * @return Play time with specified lifes count
     *
     * @throws NullPointerException {@code lifesCount} out of range [0; 9]
     * */
    int nl$getLifesPlayTime(int lifesCount) throws NullPointerException;
    /**
     * Get amethyst stick use effect ticks
     *
     * <p><i>Note: This method is unstable and use of this method is not recommended. Use {@code ServerPlayer.stickUsedTicks} inject instead.</i></p>
     *
     * @return Amethyst stick use effect ticks
     * */
    int nl$getStickUsedTicks();
    /**
     * Set amethyst stick use effect ticks
     *
     * <p><i>Note: This method is unstable and use of this method is not recommended. Use {@code ServerPlayer.stickUsedTicks} inject instead.</i></p>
     *
     * @param newTicks New amethyst stick use effect ticks
     * */
    void nl$setStickUsedTicks(int newTicks);
}
