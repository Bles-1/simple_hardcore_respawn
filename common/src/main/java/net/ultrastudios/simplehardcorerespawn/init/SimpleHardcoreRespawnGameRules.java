package net.ultrastudios.simplehardcorerespawn.init;

import net.minecraft.world.level.GameRules;
import net.ultrastudios.simplehardcorerespawn.platform.Services;

public class SimpleHardcoreRespawnGameRules {
    public static GameRules.Key<GameRules.BooleanValue> RESPAWNER_CHARGING;

    public static GameRules.Key<GameRules.BooleanValue> RESPAWNER_UNBANNING;

    public static void register() {
        RESPAWNER_CHARGING = Services.PLATFORM.registerBooleanGameRule("respawnerCharging", GameRules.Category.MISC, true);
        RESPAWNER_UNBANNING = Services.PLATFORM.registerBooleanGameRule("respawnerUnbanning", GameRules.Category.MISC, false);
    }
}
