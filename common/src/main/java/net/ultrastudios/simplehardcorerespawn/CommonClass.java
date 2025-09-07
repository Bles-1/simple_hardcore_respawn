package net.ultrastudios.simplehardcorerespawn;

import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnBlocks;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnGameRules;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnItems;
import net.ultrastudios.simplehardcorerespawn.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class CommonClass {

    public static void init() {

        SimpleHardcoreRespawnBlocks.register();
        SimpleHardcoreRespawnItems.register();
        SimpleHardcoreRespawnGameRules.register();
    }
}