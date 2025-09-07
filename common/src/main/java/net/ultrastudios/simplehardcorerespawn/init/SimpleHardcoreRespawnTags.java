package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.ultrastudios.simplehardcorerespawn.Constants;

public class SimpleHardcoreRespawnTags {
    public static final TagKey<Item> RESPAWN_ITEM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "respawn_item"));

    public static final TagKey<Item> RESPAWNER_FUEL = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "respawner_fuel"));

    @SuppressWarnings("unchecked")
    public static final TagKey<Item>[] RESPAWNER_FUEL_LEVEL = new TagKey[24];
    static {
        for (int i = 0; i < RESPAWNER_FUEL_LEVEL.length; i++) {
            RESPAWNER_FUEL_LEVEL[i] = TagKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "respawner_fuel/" + (i+1))
            );
        }
    }


}
