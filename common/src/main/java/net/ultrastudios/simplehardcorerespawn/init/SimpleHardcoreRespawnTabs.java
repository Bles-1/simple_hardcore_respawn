package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class SimpleHardcoreRespawnTabs {

    public static void fillFunctionalBlocksTab(CreativeModeTab.Output output) {
        output.accept(SimpleHardcoreRespawnItems.RE_SPAWNER_ITEM.get());
    }
}
