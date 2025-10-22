package com.bleudev.nine_lifes.custom;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

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

    @Deprecated(since = "1.8")
    public static final TagKey<Item> CAUSE_BLAST_FURNACE_EXPLODE = ItemTags.CAUSE_BLAST_FURNACE_EXPLODE;
    @Deprecated(since = "1.8")
    public static final TagKey<Item> CAUSE_FURNACE_EXPLODE = ItemTags.CAUSE_FURNACE_EXPLODE;
    @Deprecated(since = "1.8")
    public static final TagKey<Item> CAUSE_SMOKER_EXPLODE = ItemTags.CAUSE_SMOKER_EXPLODE;
    @Deprecated(since = "1.8")
    public static final TagKey<Item> CAUSE_CAMPFIRE_EXPLODE = ItemTags.CAUSE_CAMPFIRE_EXPLODE;

    private static Identifier get(String name) {
        return Identifier.of(MOD_ID, name);
    }
}
