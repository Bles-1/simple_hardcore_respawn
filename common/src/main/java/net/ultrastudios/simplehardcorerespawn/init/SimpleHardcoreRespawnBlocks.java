package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.world.level.block.Block;
import net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock;
import net.ultrastudios.simplehardcorerespawn.platform.Services;

import java.util.function.Supplier;

public class SimpleHardcoreRespawnBlocks {
    public static Supplier<Block> RE_SPAWNER;

    public static void register() {
        RE_SPAWNER = Services.PLATFORM.registerBlock(ReSpawnerBlock.BLOCK_ID, ReSpawnerBlock::new);
    }
}
