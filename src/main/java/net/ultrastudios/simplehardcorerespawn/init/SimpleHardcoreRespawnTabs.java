package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ultrastudios.simplehardcorerespawn.SimpleHardcoreRespawn;

@Mod.EventBusSubscriber(modid = SimpleHardcoreRespawn.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SimpleHardcoreRespawnTabs {

    @SubscribeEvent
    public static void build(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(SimpleHardcoreRespawnItems.RE_SPAWNER_ITEM);
        }
    }
}
