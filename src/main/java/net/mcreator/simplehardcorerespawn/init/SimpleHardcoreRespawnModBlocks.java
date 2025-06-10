
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.simplehardcorerespawn.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.mcreator.simplehardcorerespawn.block.ReSpawnerBlock;
import net.mcreator.simplehardcorerespawn.SimpleHardcoreRespawnMod;

public class SimpleHardcoreRespawnModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleHardcoreRespawnMod.MODID);
	public static final RegistryObject<Block> RE_SPAWNER = REGISTRY.register("re_spawner", () -> new ReSpawnerBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
