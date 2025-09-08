package net.ultrastudios.simplehardcorerespawn;

import net.fabricmc.api.ModInitializer;
import net.ultrastudios.simplehardcorerespawn.init.InitItemGroups;

public class SimpleHardcoreRespawn implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();

        InitItemGroups.init();
    }
}
