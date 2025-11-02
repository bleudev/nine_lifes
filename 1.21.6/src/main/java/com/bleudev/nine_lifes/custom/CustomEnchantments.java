package com.bleudev.nine_lifes.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

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

        private Entries(DynamicRegistryManager manager) {
            this.manager = manager;
        }

        private @NotNull RegistryEntry<Enchantment> getEntry(RegistryKey<Enchantment> key) {
            return RegistryEntry.of(manager.getOrThrow(RegistryKeys.ENCHANTMENT).get(key));
        }

        // Public nonstatic
        /**
         * {@link RegistryEntry} of {@code nine_lifes:charge} enchantment
         *
         * @return registry entry of enchantment
         * */
        public RegistryEntry<Enchantment> charge() {
            return getEntry(CustomEnchantments.CHARGE);
        }

        // Public static
        /**
         * Create new {@link Entries} object from provided {@link DynamicRegistryManager}
         *
         * @param manager Registry manager to use in created object
         * @return new {@link Entries} object
         * */
        @Contract(value = "_ -> new", pure = true)
        public static @NotNull Entries create(DynamicRegistryManager manager) {
            return new Entries(manager);
        }

        /**
         * {@link RegistryEntry} of {@code nine_lifes:charge} enchantment
         *
         * @param manager Registry manager where to get entry
         * @return registry entry of enchantment
         * */
        public static RegistryEntry<Enchantment> charge(DynamicRegistryManager manager) {
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
     * }</pre>
     * */
    @Contract("_, _ -> new")
    @Deprecated(since = "1.9")
    public static @NotNull RegistryEntry<Enchantment> getEntry(DynamicRegistryManager manager, RegistryKey<Enchantment> key) {
        return Entries.create(manager).getEntry(key);
    }

    /**
     * [DANGER]
     * Do not use in mods depends on Nine Lifes!
     * */
    public static void initialize() {
    }
}
