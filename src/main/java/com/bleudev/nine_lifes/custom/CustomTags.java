package com.bleudev.nine_lifes.custom;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class CustomTags {
    public static final TagKey<Item> CAUSE_BLAST_FURNACE_EXPLODE = getItemTag("cause_blast_furnace_explode");
    public static final TagKey<Item> CAUSE_FURNACE_EXPLODE = getItemTag("cause_furnace_explode");
    public static final TagKey<Item> CAUSE_SMOKER_EXPLODE = getItemTag("cause_smoker_explode");

    private static Identifier get(String name) {
        return Identifier.of(MOD_ID, name);
    }

    private static TagKey<Item> getItemTag(String name) {
        return TagKey.of(RegistryKeys.ITEM, get(name));
    }
}
