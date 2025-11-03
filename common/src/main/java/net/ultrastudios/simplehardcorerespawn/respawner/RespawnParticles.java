package net.ultrastudios.simplehardcorerespawn.respawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ServerBlockUseContext;
import net.ultrastudios.simplehardcorerespawn.CommonClass;
import net.ultrastudios.simplehardcorerespawn.LoreLinkIntegrations;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class RespawnParticles {

    public static void run(@NotNull ServerBlockUseContext context) {
        switch (CommonClass.getConfig().particles) {
            // STANDARD is redirected to default.
            case TOTEM -> totem(context);
            case SOUL -> soul(context);
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
        BlockPos pos = context.base().pPos(); // It's corner of block, not center. To get center, you have to add 0.5 to x y and z.
        ServerLevel level = context.level();

        double  radius          = 1,
                height          = 3.5;
        int     turns           = 6,
                pointsPerTurn   = 15;

        int totalPoints = pointsPerTurn * turns;
        for (int i = 0; i < totalPoints; i++) {
            double angle = 2 * Math.PI * i / pointsPerTurn;
            double x = pos.getX() + 0.5 + radius * Math.cos(angle);
            double z = pos.getZ() + 0.5 + radius * Math.sin(angle);
            double y = pos.getY() + 0.5 + (height * i / totalPoints);

            level.sendParticles(ParticleTypes.SOUL, x, y, z, 1, 0, 0, 0, 0);
        }

        int x = pos.getX(),
                y = pos.getY(),
                z = pos.getZ();
        level.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (x + 0.5), (y + 2), (z + 0.5), 32, 0.25, 0.5, 0.25, 0);
    }

    public static void fantasy(@NotNull ServerBlockUseContext context) {
        BlockPos pos = context.base().pPos();
        int x = pos.getX(),
                y = pos.getY(),
                z = pos.getZ();
        ServerLevel level = context.level();

        level.sendParticles(ParticleTypes.ENCHANT, (x), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.ENCHANT, (x + 1), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.ENCHANT, (x + 1), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
        level.sendParticles(ParticleTypes.ENCHANT, (x), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);

        level.sendParticles(ParticleTypes.PORTAL, x+0.25, y+2.25, z+0.25, 256, 0.5, 0.5, 0.5, 0.5);
    }
}
