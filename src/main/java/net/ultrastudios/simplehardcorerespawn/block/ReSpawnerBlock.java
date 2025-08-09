
package net.ultrastudios.simplehardcorerespawn.block;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.minecraftforge.registries.ForgeRegistries;
import net.ultrastudios.simplehardcorerespawn.SimpleHardcoreRespawn;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnGameRules;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnItems;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ReSpawnerBlock extends Block {

	public static final String BLOCK_ID = "re_spawner";
	public static final ResourceKey<Block> BLOCK_RESOURCE_KEY = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(SimpleHardcoreRespawn.MOD_ID + ":" + BLOCK_ID));

	private static final int MAX_CHARGES = 4;
	private static final int MIN_CHARGES = 0;
	public static final IntegerProperty CHARGES = IntegerProperty.create("charges", MIN_CHARGES, MAX_CHARGES);
	private static final int MAX_POWER = 8;
	private static final int MIN_POWER = 0;
	public static final IntegerProperty POWER = IntegerProperty.create("power", MIN_POWER, MAX_POWER);
	//	Respawner has max 4 charges. One charge is enough for one use. To charge Respawner to one charge, you have to charge it with 9 powers.
 	//	8 is max (required for texture capacity), and when the 9th step is charged, respawner makes itself powered (charges - 1). Then power
	//	comes to 0, and you can charge it again until reach maximum of full-charges.
	//	(Charges is int from 0 to 4, what makes 5 possible values; power is int from 0 to 8, what makes 9 possible values.)

	public ReSpawnerBlock() {
		super(Properties.of().setId(BLOCK_RESOURCE_KEY)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.sound(SoundType.STONE)
				.strength(55f, 30f)
				.requiresCorrectToolForDrops()
				.hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true));
		this.registerDefaultState(this.defaultBlockState()
				.setValue(CHARGES, 0)
				.setValue(POWER, 0));
	}

    @Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(CHARGES, POWER);
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if (state != null) {
			Level level = context.getLevel();
			if (level instanceof ServerLevel serverLevel) {
				if (serverLevel.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING))
					state = (state.setValue(CHARGES, 0).setValue(POWER, 0));
				else
					state = (state.setValue(CHARGES, 4).setValue(POWER, 0));
			}
		}
		return state;
	}

	@Override
	protected  @NotNull InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {

		if (pLevel instanceof ServerLevel level) {
            TagKey<Item> tag = ItemTags.create(ResourceLocation.fromNamespaceAndPath(SimpleHardcoreRespawn.MOD_ID, "respawn_item"));

            if (CanRespawn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult)) {

				// Prepare
				InteractionResult result;

				// Action
				if (level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_UNBANNING))
					result = Unban(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
				else
					result = Respawn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);

				// Effect
				if (result == InteractionResult.SUCCESS) {
                    RespawnEffect(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
                    return InteractionResult.FAIL;
                } else if (result == InteractionResult.FAIL)
					if (Objects.equals(GetNameFromItem(pStack), "Herobrine")) {
                        EasterEggEffect(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
                        return InteractionResult.SUCCESS;
                    }
					else {
                        RespawnFailEffect(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
                        return InteractionResult.FAIL;
                    }

			} else if (CanCharge(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult)) {
				// Prepare
				int amount = GetFuelPower(pStack, pLevel);
				InteractionResult result;
				// Action
				result = Charge(amount, pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
				// Effect
				if (result == InteractionResult.SUCCESS)
                    ChargeEffect(amount, pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);

                return result;

			} else return InteractionResult.FAIL;
		}
		return InteractionResult.SUCCESS;
	}


	// // //
	/* Respawner logic methods: */
	// // //

	// Effects

	/**
	 * Effect to play if respawn action was failed.
	 */
	private void RespawnFailEffect(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (!pLevel.isClientSide()) {
			pLevel.playSound(null, pPos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 2, 1);
		}
	}

	/**
	 * Effect to play if respawn action was successfully competed.
	 */
	private void RespawnEffect(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (!pLevel.isClientSide()) {
			RespawnParticlesTotem(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
			pLevel.playSound(null, pPos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 2, 1);
		}
	}

	/**
	 * Easter egg...
	 */
	private void EasterEggEffect(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		SimpleHardcoreRespawn.LOGGER.fatal("Respawner action interrupted by player [069a79f4-44e9-4726-a5be-fca90e38aaf5]");
		if (pLevel instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.SOUL, (pPos.getX() + 0.5), (pPos.getY() + 2), (pPos.getZ() + 0.5), 1, 0, 0, 0, 0);
	}

	/**
	 * Effect to play after successfully and completed charge action.
	 */
	private void ChargeEffect(int pChargedAmount, @NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (!pLevel.isClientSide()) {
			pLevel.playSound(null, pPos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 2, (float) (pState.getValue(ReSpawnerBlock.POWER) / 10 + 2));
		}
	}

	/* particles */

	/**
	 * Spawn totem-like particles.
	 */
	private void RespawnParticlesTotem(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		int x = pPos.getX(), y = pPos.getY(), z = pPos.getZ();
		if (pLevel instanceof ServerLevel _level) {
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x + 1), (y + 2), (z), 128, 0, 0.5, 0, 0.5);
			_level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, (x), (y + 2), (z + 1), 128, 0, 0.5, 0, 0.5);
			_level.sendParticles(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (x + 0.5), (y + 2), (z + 0.5), 32, 0.25, 0.5, 0.25, 0);
		}
	}

	// Actions

	/**
	 * <p>Attempt respawn action.</p>
	 * <p>This method can check conditions like can respawn given player, etc., but doesn't have to check things like e.g., is respawner charged.</p>
	 * <p>If action was failed, fail effect can be played. Conditions that have to exclude any effect should be checked in {@code CanRespawn()} method.</p>
	 * @param pStack Item stack used to respawn.
	 * @param pState BlockState of respawner.
	 * @param pLevel Current level.
	 * @param pPos Position of block.
	 * @param pPlayer Player who attempted action.
	 * @param pHand {@code InteractionHand}
	 * @param pHitResult {@code BlockHitResult}
	 * @return <p>{@code InteractionResult.SUCCESS} when player has been successfully respawned. Success effect can be played.</p> <p>{@code InteractionResult.FAIL} if player hasn't been respawned. (E.g., player was not found or cannot be respawned.) Failure effect can be played.</p> <p>Optionally others {@code InteractionResults} in accordance with their intended purpose.</p>
	 */
	private InteractionResult Respawn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (pLevel instanceof ServerLevel level) {
			String rawName = GetNameFromItem(pStack);
			if (rawName == null) return InteractionResult.FAIL;
			ServerPlayer target = null;
			boolean targetFound = false;
			// Search for player:
			for (ServerPlayer player : level.players()) {
				if (Objects.equals(player.getGameProfile().getName(), rawName)) {
					if (!player.isSpectator()) return InteractionResult.FAIL;
					target = player;
					targetFound = true;
					break;
				}
			}
			if (!targetFound) return  InteractionResult.FAIL;
			// Action:
			target.setGameMode(level.getServer().getDefaultGameType());
			target.teleportTo(pPos.getX(), pPos.getY()+1, pPos.getZ());
			// Advancement for respawned player:
			ResourceLocation advancementID = ResourceLocation.parse("simple_hardcore_respawn:back_one_day");
			AdvancementHolder advancement = level.getServer().getAdvancements().get(advancementID);
			if (advancement != null && !target.getAdvancements().getOrStartProgress(advancement).isDone())
				target.getAdvancements().award(advancement, "respawn");
			// Consume power:
			if (level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) {
				BlockState state = pState
                        .setValue(ReSpawnerBlock.CHARGES, pState.getValue(ReSpawnerBlock.CHARGES) - 1);
                level.setBlock(pPos, state, Block.UPDATE_CLIENTS);
			}
			// Consume item:
			if (!pPlayer.isCreative())
				if (pStack.isDamageableItem()) {
					EquipmentSlot slot = pStack.getEquipmentSlot();
					if (slot == null) slot = pHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
					pStack.hurtAndBreak(1, pPlayer, slot);
				} else
					pStack.shrink(1);

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS; // code was run client-side
	}

	private  InteractionResult Unban(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (pLevel instanceof ServerLevel level) {
			String name = GetNameFromItem(pStack);
			if (name == null) return InteractionResult.FAIL;
			// Unban:
			boolean success = false;
			UserBanList bans = level.getServer().getPlayerList().getBans();
			for (UserBanListEntry entry : bans.getEntries()) {
				if (Objects.equals(entry.getDisplayName().getString(), name)) {
					bans.remove(entry);
					success = true;
					break;
				}
			}
			if (!success) return InteractionResult.FAIL;
			// Consume power:
			if (level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) {
				BlockState state = pState
                        .setValue(ReSpawnerBlock.CHARGES, pState.getValue(ReSpawnerBlock.CHARGES) - 1);
                level.setBlock(pPos, state, Block.UPDATE_CLIENTS);
			}
			// Consume item:
			if (!pPlayer.isCreative())
				if (pStack.isDamageableItem()) {
					EquipmentSlot slot = pStack.getEquipmentSlot();
					if (slot == null) slot = pHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
					pStack.hurtAndBreak(1, pPlayer, slot);
				} else
					pStack.shrink(1);

			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	private InteractionResult Charge(int pAmount, @NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		if (pLevel instanceof ServerLevel level) {
			// Don't charge Respawner in no-charge mode ;)
			if (!level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) return InteractionResult.PASS;

            // math
            int power = pState.getValue(ReSpawnerBlock.POWER);
            int charges = pState.getValue(ReSpawnerBlock.CHARGES);
			int currentPower = power + (charges * (MAX_POWER+1));
			int maxPower = ((MAX_POWER+1) * MAX_CHARGES); // On the 4th (max) charge ReSpawner is full, so max intended power for the 4th charge is 0.
			int totalPower = currentPower + pAmount;

			// Fail if action will result more power than max capacity.
			if (totalPower > maxPower) return InteractionResult.FAIL;

			// Consume item.
			if (!pPlayer.isCreative()) {
				if (pStack.isDamageableItem()) {
					EquipmentSlot slot = pStack.getEquipmentSlot();
					if (slot == null) slot = pHand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
					pStack.hurtAndBreak(1, pPlayer, slot);
				} else {
					pStack.shrink(1);
				}
			}
            SimpleHardcoreRespawn.LOGGER.debug("Preparing to load. totalPower: " + totalPower);
			// Charge
            BlockState state = pState
                    .setValue(ReSpawnerBlock.POWER  , totalPower % (MAX_POWER+1))
                    .setValue(ReSpawnerBlock.CHARGES, totalPower / (MAX_POWER+1));
            level.setBlock(pPos, state, Block.UPDATE_CLIENTS);

            return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS; // code was run client-side
	}

	// Conditions

	/**
	 * A preliminary check to determine if a respawn attempt can even be considered.
	 * <p>
	 * This method acts as a 'gatekeeper,' verifying essential preconditions like item validity, available space, and respawner charge. If it returns {@code false}, the entire respawn process is skipped, preventing a failed attempt and its associated effects.
	 * </p>
	 * <p>
	 * <b>This method should not check player state (e.g., online status, alive/dead) or trigger any failure effects.</b>
	 * </p>
	 * @param pStack Current itemStack.
	 * @param pState Current blockState.
	 * @param pLevel Current level.
	 * @param pPos Block position.
	 * @param pPlayer Player using respawner.
	 * @param pHand {@code InteractionHand}
	 * @param pHitResult {@code BlockHitResult}
	 * @return {@code true} if respawn action can be attempted.
	 */
	private boolean CanRespawn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		return pStack.is(ItemTags.create(SimpleHardcoreRespawn.MOD_ID, "respawn_item"))
				&& IsCharged(pState, pLevel, pPos)
				&& pLevel.getBlockState(pPos.above()).isAir() && pLevel.getBlockState(pPos.above().above()).isAir();
	}

	/**
	 * A preliminary check to determine if a charge attempt can even be considered.
	 * <p>
	 * This method acts as a 'gatekeeper,' verifying essential preconditions like item validity and respawner charge. If it returns {@code false}, the entire charging process is skipped.
	 * @param pStack Current itemStack.
	 * @param pState Current blockState.
	 * @param pLevel Current level.
	 * @param pPos Block position.
	 * @param pPlayer Player using respawner.
	 * @param pHand {@code InteractionHand}
	 * @param pHitResult {@code BlockHitResult}
	 * @return {@code true} if respawn action can be attempted.
	 */
	private boolean CanCharge(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {
		return pStack.is(ItemTags.create(SimpleHardcoreRespawn.MOD_ID, "respawner_fuel")) && pState.getValue(ReSpawnerBlock.CHARGES) < 4;
	}

	/**
	 * Method that checks if ReSpawner is charged to the minimum required to perform respawn action.
	 * @param pState Current blockState.
	 * @param pLevel Current level.
	 * @param pPos Block position.
	 * @return Return {@code true} if ReSpawner has minimum one charge.
	 */
	private boolean IsCharged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos) {
		return pState.getValue(ReSpawnerBlock.CHARGES) >= 1;
	}

	/**
	 * Method that checks the power of fuel.
	 * @param pStack {@code ItemStack} with fuel item.
	 * @param pLevel Current level.
	 * @return Amount of fuel power.
	 */
	private int GetFuelPower(@NotNull ItemStack pStack, @NotNull Level pLevel) {
		for (int i = 1; i <= 24; i++) {
			TagKey<Item> tag = ItemTags.create(SimpleHardcoreRespawn.MOD_ID, "respawner_fuel/" + i);
			if (pStack.is(tag)) return i;
		}
		return 0;
	}

	private String GetNameFromItem(@NotNull ItemStack pStack) {
		Component name = pStack.getCustomName();
		if (name == null) return null;
		return name.getString();
	}
}
