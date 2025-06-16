package net.ultrastudios.simplehardcorerespawn.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

public class ChargeProcedure {
	public static InteractionResult execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return InteractionResult.PASS;
		double power = 0;
		double charges = 0;
		double maxPower = 0;
		double maxCharges = 0;
		double powerToAdd = 0;
		double totalPower = 0;
		maxPower = 8;
		maxCharges = 4;
		power = blockstate.getBlock().getStateDefinition().getProperty("power") instanceof IntegerProperty _getip1 ? blockstate.getValue(_getip1) : -1;
		charges = blockstate.getBlock().getStateDefinition().getProperty("charges") instanceof IntegerProperty _getip3 ? blockstate.getValue(_getip3) : -1;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/1")))) {
			powerToAdd = 1;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/2")))) {
			powerToAdd = 2;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/3")))) {
			powerToAdd = 3;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/4")))) {
			powerToAdd = 4;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/5")))) {
			powerToAdd = 5;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/6")))) {
			powerToAdd = 6;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/7")))) {
			powerToAdd = 7;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/8")))) {
			powerToAdd = 8;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/9")))) {
			powerToAdd = 9;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/10")))) {
			powerToAdd = 10;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/11")))) {
			powerToAdd = 11;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/12")))) {
			powerToAdd = 12;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/13")))) {
			powerToAdd = 13;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/14")))) {
			powerToAdd = 14;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/15")))) {
			powerToAdd = 15;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/16")))) {
			powerToAdd = 16;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/17")))) {
			powerToAdd = 17;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/18")))) {
			powerToAdd = 18;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/19")))) {
			powerToAdd = 19;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/20")))) {
			powerToAdd = 20;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/21")))) {
			powerToAdd = 21;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/22")))) {
			powerToAdd = 22;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/23")))) {
			powerToAdd = 23;
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("simple_hardcore_respawn:respawner_fuel/24")))) {
			powerToAdd = 24;
		} else {
			return InteractionResult.FAIL;
		}
		totalPower = powerToAdd + power + charges * (maxPower + 1);
		if (totalPower / (maxPower + 1) > maxCharges) {
			return InteractionResult.FAIL;
		}
		(entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).shrink(1);
		{
			int _value = (int) (totalPower % (maxPower + 1));
			BlockPos _pos = BlockPos.containing(x, y, z);
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("power") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
		{
			int _value = (int) Math.floor(totalPower / (maxPower + 1));
			BlockPos _pos = BlockPos.containing(x, y, z);
			BlockState _bs = world.getBlockState(_pos);
			if (_bs.getBlock().getStateDefinition().getProperty("charges") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
				world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
		}
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.respawn_anchor.charge")), SoundSource.BLOCKS, 2,
						(float) ((blockstate.getBlock().getStateDefinition().getProperty("power") instanceof IntegerProperty _getip59 ? blockstate.getValue(_getip59) : -1) / 10 + 2));
			} else {
				_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.respawn_anchor.charge")), SoundSource.BLOCKS, 2,
						(float) ((blockstate.getBlock().getStateDefinition().getProperty("power") instanceof IntegerProperty _getip59 ? blockstate.getValue(_getip59) : -1) / 10 + 2), false);
			}
		}
		return InteractionResult.SUCCESS;
	}
}
