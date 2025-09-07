package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.ultrastudios.simplehardcorerespawn.Constants;
import net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock;
import net.ultrastudios.simplehardcorerespawn.platform.Services;

import java.util.function.Supplier;

public class SimpleHardcoreRespawnItems {
	public static Supplier<Item> RE_SPAWNER_ITEM;

    public static void register() {
        RE_SPAWNER_ITEM = Services.PLATFORM.registerItem(ReSpawnerBlock.BLOCK_ID,
                () -> new BlockItem(SimpleHardcoreRespawnBlocks.RE_SPAWNER.get(), new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM,
                                ResourceLocation.parse(Constants.MOD_ID + ":" + ReSpawnerBlock.BLOCK_ID)))));
    }
}
