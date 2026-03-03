package com.bleudev.nine_lifes.interfaces.mixin;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface CustomServerPlayer {
    int nl$getLifes();
    void nl$setLifes(int value);
    int nl$getLifesPlayTime(int lifesCount);
}
