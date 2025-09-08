package net.ultrastudios.simplehardcorerespawn.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class InitItemGroups {
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(SimpleHardcoreRespawnTabs::fillFunctionalBlocksTab);
    }
}
