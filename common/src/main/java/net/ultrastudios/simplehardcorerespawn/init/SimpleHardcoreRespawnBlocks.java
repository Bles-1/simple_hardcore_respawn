
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.obj.ObjMaterialLibrary;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock;
import net.ultrastudios.simplehardcorerespawn.SimpleHardcoreRespawn;

public class SimpleHardcoreRespawnBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleHardcoreRespawn.MOD_ID);

	public static final RegistryObject<Block> RE_SPAWNER = BLOCKS.register(ReSpawnerBlock.BLOCK_ID, ReSpawnerBlock::new);
}
