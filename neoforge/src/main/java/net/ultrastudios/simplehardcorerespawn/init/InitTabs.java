package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.ultrastudios.simplehardcorerespawn.Constants;
import org.jetbrains.annotations.NotNull;

public class InitTabs {

    public static void build(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            SimpleHardcoreRespawnTabs.fillFunctionalBlocksTab(event);
        }
    }
}