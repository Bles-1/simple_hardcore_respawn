package net.ultrastudios.simplehardcorerespawn;

import net.ultrastudios.lorelink.modsconfig.shr.*;
import net.ultrastudios.simplehardcorerespawn.respawner.DefaultActionHandler;

public class LoreLinkIntegrations {
    private static IActionHandler reSpawnerActionHandler = new DefaultActionHandler();

    public static IActionHandler getReSpawnerActionHandler()
    {
        return reSpawnerActionHandler;
    }

    public static void updateReSpawnerActionHandler()
    {
        reSpawnerActionHandler = Integrations.getActionHandler(new DefaultActionHandler());
    }
}
