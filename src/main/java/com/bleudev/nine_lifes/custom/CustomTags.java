package com.bleudev.nine_lifes.custom;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class CustomTags {
    public static class ItemTags {
        public static final TagKey<Item> CAUSE_BLAST_FURNACE_EXPLODE = of("cause_blast_furnace_explode");
        public static final TagKey<Item> CAUSE_FURNACE_EXPLODE = of("cause_furnace_explode");
        public static final TagKey<Item> CAUSE_SMOKER_EXPLODE = of("cause_smoker_explode");
        public static final TagKey<Item> CAUSE_CAMPFIRE_EXPLODE = of("cause_campfire_explode");
        public static final TagKey<Item> LIGHTNING_CHARGEABLE = of("lightning_chargeable");

        private static TagKey<Item> of(String name) {
            return TagKey.of(RegistryKeys.ITEM, get(name));
        }
    }

    public static class DamageTypeTags {
        public static final TagKey<DamageType> IS_LIGHTNING_OR_FIRE = of("is_lightning_or_fire");

        private static TagKey<DamageType> of(String name) {
            return TagKey.of(RegistryKeys.DAMAGE_TYPE, get(name));
        }
    }

    @Contract("_ -> new")
    private static @NotNull Identifier get(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
