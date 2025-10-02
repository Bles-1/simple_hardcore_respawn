package net.ultrastudios.simplehardcorerespawn;

import net.ultrastudios.lorelink.utils.config.UltraConfigManager;
import net.ultrastudios.simplehardcorerespawn.config.objects.Config;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnBlocks;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnGameRules;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnItems;
import net.ultrastudios.simplehardcorerespawn.platform.Services;

public class CommonClass {

    public static void init() {

        SimpleHardcoreRespawnBlocks.register();
        SimpleHardcoreRespawnItems.register();
        SimpleHardcoreRespawnGameRules.register();

        UltraConfigManager.register(Constants.MOD_ID, Services.PLATFORM.getConfigDir(), Config.class, new Config());
    }

    public static Config getConfig() {
        return UltraConfigManager.get(Constants.MOD_ID, Config.class).get();
    }
}