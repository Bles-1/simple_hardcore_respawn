
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.simplehardcorerespawn.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SimpleHardcoreRespawnModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> RESPAWNER_CHARGING = GameRules.register("respawnerCharging", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> RESPAWNER_UNBANNING = GameRules.register("respawnerUnbanning", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
}
