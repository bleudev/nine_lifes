package com.bleudev.nine_lifes.api

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

typealias Condition = () -> Boolean
typealias FriendlyStreamCodec<T> = StreamCodec<RegistryFriendlyByteBuf, T>
