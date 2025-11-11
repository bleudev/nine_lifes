package com.bleudev.nine_lifes.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.bleudev.nine_lifes.NineLifesConst.MOD_ID;

/**
 * Class which provides custom {@link Enchantment enchantments} added in Nine lifes mod(
 * {@code nine_lifes:charge}
 * )
 * */
public class CustomEnchantments {
    private CustomEnchantments() {}

    /**
     * {@link RegistryKey} of {@code nine_lifes:charge} enchantment
     * */
    public static final RegistryKey<Enchantment> CHARGE = of("charge");

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, name));
    }

    /**
     * Class which provides functions and methods to get enchantment {@link RegistryEntry registry entries} with provided {@link DynamicRegistryManager}
     *
     * <pre>{@code
     * // Simple example
     * CustomEnchantments.Entries.create(world.getRegistryManager()).charge()
     * // Or
     * CustomEnchantments.Entries.charge(world.getRegistryManager())
     * }</pre>
     * */
    public static class Entries {
        private final DynamicRegistryManager manager;
        private final Map<RegistryKey<Enchantment>, RegistryEntry<Enchantment>> entries;

        @SafeVarargs
        private Map<RegistryKey<Enchantment>, RegistryEntry<Enchantment>> getEntries(RegistryKey<Enchantment>... enchantments) {
            var r = new HashMap<RegistryKey<Enchantment>, RegistryEntry<Enchantment>>();
            for (RegistryKey<Enchantment> key: enchantments)
                r.put(key, getEntry(key));
            return Collections.unmodifiableMap(r);
        }

        private Entries(@NotNull DynamicRegistryManager manager) {
            this.manager = manager;
            this.entries = getEntries(CustomEnchantments.CHARGE);
        }

        private @NotNull RegistryEntry<Enchantment> getEntry(RegistryKey<Enchantment> key) {
            return RegistryEntry.of(manager.getOrThrow(RegistryKeys.ENCHANTMENT).get(key));
        }

        /**
         * {@link RegistryEntry} of {@code nine_lifes:charge} enchantment
         *
         * @return registry entry of enchantment
         * */
        public RegistryEntry<Enchantment> charge() {
            return this.entries.get(CustomEnchantments.CHARGE);
        }

        /**
         * Create new {@link Entries} object from provided {@link DynamicRegistryManager}
         *
         * @param manager Registry manager to use in created object
         * @return new {@link Entries} object
         * */
        @Contract(value = "_ -> new", pure = true)
        public static @NotNull Entries create(@NotNull DynamicRegistryManager manager) {
            return new Entries(manager);
        }

        /**
         * {@link RegistryEntry} of {@code nine_lifes:charge} enchantment
         *
         * @param manager Registry manager where to get entry
         * @return registry entry of enchantment
         * */
        public static RegistryEntry<Enchantment> charge(@NotNull DynamicRegistryManager manager) {
            return create(manager).charge();
        }
    }

    /**
     * Deprecated since 1.9 version and will be deleted in 1.10.
     * Use {@link Entries} functions or methods instead
     * <pre>{@code
     * // Replace this line
     * CustomEnchantments.getEntry(world.getRegistryManager(), CustomEnchantments.CHARGE)
     * // With
     * CustomEnchantments.Entries.create(world.getRegistryManager()).charge()
     * // Or
     * CustomEnchantments.charge(world.getRegistryManager())
     * }</pre>
     * */
    @Deprecated
    @Contract("!null, _ -> new; null, _ -> fail")
    public static RegistryEntry<Enchantment> getEntry(DynamicRegistryManager manager, RegistryKey<Enchantment> key) {
        Objects.requireNonNull(manager);
        return Entries.create(manager).entries.get(key);
    }

    /**
     * [DANGER]
     * Do not use in mods depends on Nine Lifes!
     * */
    public static void initialize() {
    }
}
