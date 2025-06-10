package net.mcreator.simplehardcorerespawn.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class RespawnerParticlesProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 0), (y + 2), (z + 0), 128, 0, 0.5, 0, 0.5);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z + 0), 128, 0, 0.5, 0, 0.5);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 0), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (x + 0.5), (y + 2), (z + 0.5), 32, 0.25, 0.5, 0.25, 0);
	}
}
