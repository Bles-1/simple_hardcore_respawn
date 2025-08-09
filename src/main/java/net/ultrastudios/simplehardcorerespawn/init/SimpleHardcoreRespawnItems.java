
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

import net.ultrastudios.simplehardcorerespawn.SimpleHardcoreRespawn;

public class SimpleHardcoreRespawnItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleHardcoreRespawn.MOD_ID);

	public static RegistryObject<Item> RE_SPAWNER_ITEM = ITEMS.register("re_spawner", () -> new BlockItem(SimpleHardcoreRespawnBlocks.RE_SPAWNER.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(SimpleHardcoreRespawn.MOD_ID + ":re_spawner")))));
}
