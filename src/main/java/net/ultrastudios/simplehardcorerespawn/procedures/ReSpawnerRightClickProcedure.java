package net.ultrastudios.simplehardcorerespawn.procedures;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;

import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnModGameRules;

public class ReSpawnerRightClickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		if ((blockstate.getBlock().getStateDefinition().getProperty("charges") instanceof IntegerProperty _getip1 ? blockstate.getValue(_getip1) : -1) >= 1
				&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawn_item")))) {
			if (world.getLevelData().getGameRules().getBoolean(SimpleHardcoreRespawnModGameRules.RESPAWNER_UNBANNING)) {
				UnbanProcedure.execute(world, x, y, z, blockstate, entity);
			} else {
				RespawnProcedure.execute(world, x, y, z, blockstate, entity);
			}
		} else if (world.getLevelData().getGameRules().getBoolean(SimpleHardcoreRespawnModGameRules.RESPAWNER_CHARGING)
				&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel")))) {
			ChargeProcedure.execute(world, x, y, z, blockstate, entity);
		}
	}
}
