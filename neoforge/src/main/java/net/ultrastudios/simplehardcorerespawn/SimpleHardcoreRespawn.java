package net.ultrastudios.simplehardcorerespawn;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.ultrastudios.simplehardcorerespawn.init.InitTabs;
import net.ultrastudios.simplehardcorerespawn.platform.NeoForgePlatformHelper;

@Mod(Constants.MOD_ID)
public class SimpleHardcoreRespawn {

    public SimpleHardcoreRespawn(IEventBus eventBus) {
        eventBus.addListener(InitTabs::build);
        NeoForgePlatformHelper.register(eventBus);

        CommonClass.init();

        Constants.LOG.info(Constants.MOD_ID + " loaded and ready to save thousands of lives!");
    }
}