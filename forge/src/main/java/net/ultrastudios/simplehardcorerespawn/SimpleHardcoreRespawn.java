package net.ultrastudios.simplehardcorerespawn;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.ultrastudios.simplehardcorerespawn.platform.ForgePlatformHelper;

@Mod(Constants.MOD_ID)
public class SimpleHardcoreRespawn {

    public SimpleHardcoreRespawn(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();

        ForgePlatformHelper.register(bus);

        CommonClass.init();

        Constants.LOG.info(Constants.MOD_ID + " loaded and ready to save thousands of lives!");
    }
}