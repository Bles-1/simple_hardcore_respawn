package net.ultrastudios.simplehardcorerespawn.respawner;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ServerBlockUseContext;
import net.ultrastudios.simplehardcorerespawn.CommonClass;
import net.ultrastudios.simplehardcorerespawn.LoreLinkIntegrations;
import org.jetbrains.annotations.NotNull;

public class RespawnParticles {

    public static void run(@NotNull ServerBlockUseContext context) {

        switch (CommonClass.getConfig().particles) {
            // STANDARD is redirected to default.
            case TOTEM -> totem(context);
            case SOUL -> soul(context);
            case BEACON -> beacon(context);
            case PLAYER -> player(context);
            case FANTASY -> fantasy(context);
            default -> standard(context);
        }

    }

    public static void standard(@NotNull ServerBlockUseContext context) {
        LoreLinkIntegrations.getReSpawnerActionHandler().respawnParticlesEffect(context);
    }

    public static void totem(@NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();

        int x = context.base().pPos().getX(),
                y = context.base().pPos().getY(),
                z = context.base().pPos().getZ();
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (x + 0.5), (y + 2), (z + 0.5), 32, 0.25, 0.5, 0.25, 0);
    }

    public static void soul(@NotNull ServerBlockUseContext context) {

    }

    public static void beacon(@NotNull ServerBlockUseContext context) {

    }

    public static void fantasy(@NotNull ServerBlockUseContext context) {

    }

    public static void player(@NotNull ServerBlockUseContext context) {

    }
}
