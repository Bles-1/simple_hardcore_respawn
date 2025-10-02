
package net.ultrastudios.simplehardcorerespawn.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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

import net.ultrastudios.lorelink.modsconfig.shr.IActionHandler;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.BlockUseContext;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ClientBlockUseContext;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ServerBlockUseContext;
import net.ultrastudios.simplehardcorerespawn.Constants;
import net.ultrastudios.simplehardcorerespawn.LoreLinkIntegrations;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnGameRules;
import net.ultrastudios.simplehardcorerespawn.respawner.RespawnParticles;
import org.jetbrains.annotations.NotNull;

public class ReSpawnerBlock extends Block {

	public static final String BLOCK_ID = "re_spawner";
	public static final ResourceKey<Block> BLOCK_RESOURCE_KEY = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(Constants.MOD_ID + ":" + BLOCK_ID));

    public static final int MAX_CHARGES = 4;
    public static final int MIN_CHARGES = 0;
	public static final IntegerProperty CHARGES = IntegerProperty.create("charges", MIN_CHARGES, MAX_CHARGES);
	public static final int MAX_POWER = 8;
    public static final int MIN_POWER = 0;
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

    //
    // Logic for ReSpawner
    //
	@Override
	protected  @NotNull InteractionResult useItemOn(@NotNull ItemStack pStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHitResult) {

        IActionHandler H = LoreLinkIntegrations.getReSpawnerActionHandler();
        BlockUseContext con = new BlockUseContext(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);

        if (pLevel instanceof ServerLevel level) {
            ServerBlockUseContext context = new ServerBlockUseContext(con);

            if (H.canRespawn(context)) {
                InteractionResult result =
                        level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_UNBANNING) ?
                                H.doUnban(context) :
                                H.doRespawn(context);

                if (result == InteractionResult.SUCCESS) {
                    H.respawnEffect(context);
                    RespawnParticles.run(context);
                }
                else if (result == InteractionResult.FAIL) {
                    if (H.shouldTriggerEasterEgg(context)) {
                        H.easterEggEffect(context);
                        return InteractionResult.SUCCESS;
                    } else {
                        H.respawnFailedEffect(context);
                    }
                }
                return result;

            } else if (H.canCharge(context)) {
                int amount = H.getPowerToCharge(context);
                InteractionResult result =
                        H.doCharge(amount, context);

                if (result == InteractionResult.SUCCESS)
                    H.chargeEffect(amount, context);
                else if (result == InteractionResult.FAIL)
                    H.chargeFailedEffect(amount, context);

                return result;
            } else return InteractionResult.FAIL;
        }
        else return H.getClientResults(new ClientBlockUseContext(con));
	}
}
