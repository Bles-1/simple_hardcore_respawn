package net.ultrastudios.simplehardcorerespawn.respawner;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.ultrastudios.lorelink.modsconfig.shr.IActionHandler;
import net.ultrastudios.lorelink.utils.Advancements;
import net.ultrastudios.lorelink.utils.BanListHelper;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ClientBlockUseContext;
import net.ultrastudios.lorelink.utils.actioncontext.blockusecontext.ServerBlockUseContext;
import net.ultrastudios.simplehardcorerespawn.Constants;
import net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnGameRules;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnTags;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock.MAX_CHARGES;
import static net.ultrastudios.simplehardcorerespawn.block.ReSpawnerBlock.MAX_POWER;

public class DefaultActionHandler implements IActionHandler {
    @Override
    public boolean canCharge(@NotNull ServerBlockUseContext context) {
        return context.base().pStack().is(SimpleHardcoreRespawnTags.RESPAWNER_FUEL) && context.base().pState().getValue(ReSpawnerBlock.CHARGES) < 4;
    }

    @Override
    public boolean canRespawn(@NotNull ServerBlockUseContext context) {
        return context.base().pStack().is(SimpleHardcoreRespawnTags.RESPAWN_ITEM)
                && isCharged(context)
                && context.level().getBlockState(context.base().pPos().above()).isAir() && context.level().getBlockState(context.base().pPos().above().above()).isAir();
    }

    @Override
    public boolean isCharged(@NotNull ServerBlockUseContext context) {
        return context.base().pState().getValue(ReSpawnerBlock.CHARGES) >= 1;
    }

    @Override
    public boolean shouldTriggerEasterEgg(@NotNull ServerBlockUseContext context) {
        Component component = context.base().pStack().getCustomName();
        if (component == null) return false;
        String name = component.getString();
        return Objects.equals(name, "Herobrine");
    }

    @Override
    public int getPowerToCharge(@NotNull ServerBlockUseContext context) {
        for (int i = 0; i < SimpleHardcoreRespawnTags.RESPAWNER_FUEL_LEVEL.length; i++) {
            if (context.base().pStack().is(SimpleHardcoreRespawnTags.RESPAWNER_FUEL_LEVEL[i])) return i+1;
        }
        return 0;
    }

    @Override
    public @NotNull InteractionResult getClientResults(@NotNull ClientBlockUseContext context) {
        return InteractionResult.SUCCESS;
    }

    //

    @Override
    public @NotNull InteractionResult doRespawn(@NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        Component component = context.base().pStack().getCustomName();
        if (component == null) return InteractionResult.FAIL;
        String name = component.getString();
        ServerPlayer target = null;
        boolean targetFound = false;
        BlockPos pos = context.base().pPos();
        ItemStack stack = context.base().pStack();
        BlockState state = context.base().pState();
        Player player = context.base().pPlayer();
        // Search for player:
        for (ServerPlayer playerIterator : level.players()) {
            if (Objects.equals(playerIterator.getGameProfile().getName(), name)) {
                if (!playerIterator.isSpectator()) return InteractionResult.FAIL;
                target = playerIterator;
                targetFound = true;
                break;
            }
        }
        if (!targetFound) return  InteractionResult.FAIL;
        // Action:
        target.setGameMode(level.getServer().getDefaultGameType());
        target.teleportTo(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
        // Advancement for respawned player:
        ResourceLocation advancementID = ResourceLocation.parse("simple_hardcore_respawn:back_one_day");
        AdvancementHolder advancement = level.getServer().getAdvancements().get(advancementID);
        if (advancement != null && !target.getAdvancements().getOrStartProgress(advancement).isDone())
            target.getAdvancements().award(advancement, "respawn");
        // Consume power:
        if (level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) {
            BlockState _state = state
                    .setValue(ReSpawnerBlock.CHARGES, state.getValue(ReSpawnerBlock.CHARGES) - 1);
            level.setBlock(pos, _state, Block.UPDATE_CLIENTS);
        }
        // Consume item:
        if (!player.isCreative())
            if (stack.isDamageableItem()) {
                var slot = context.base().pHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, player, slot);
            } else
                stack.shrink(1);

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult doCharge(int pPower, @NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        BlockState pState = context.base().pState();
        Player pPlayer = context.base().pPlayer();
        ItemStack pStack = context.base().pStack();

        // Don't charge Respawner in no-charge mode ;)
        if (!level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) return InteractionResult.PASS;

        // math
        int power = pState.getValue(ReSpawnerBlock.POWER);
        int charges = pState.getValue(ReSpawnerBlock.CHARGES);
        int currentPower = power + (charges * (MAX_POWER+1));
        int maxPower = ((MAX_POWER+1) * MAX_CHARGES); // On the 4th (max) charge ReSpawner is full, so max intended power for the 4th charge is 0.
        int totalPower = currentPower + pPower;

        // Fail if action will result more power than max capacity.
        if (totalPower > maxPower) return InteractionResult.FAIL;

        // Consume item.
        if (!pPlayer.isCreative()) {
            if (pStack.isDamageableItem()) {
                var slot = context.base().pHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                pStack.hurtAndBreak(1, pPlayer, slot);
            } else {
                pStack.shrink(1);
            }
        }
        // Charge
        BlockState state = pState
                .setValue(ReSpawnerBlock.POWER  , totalPower % (MAX_POWER+1))
                .setValue(ReSpawnerBlock.CHARGES, totalPower / (MAX_POWER+1));
        level.setBlock(context.base().pPos(), state, Block.UPDATE_CLIENTS);

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult doUnban(@NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        Component component = context.base().pStack().getCustomName();
        if (component == null) return InteractionResult.FAIL;
        String name = component.getString();
        UUID playerUUID = null;
        // Action:
        boolean success = false;
        UserBanList bans = level.getServer().getPlayerList().getBans();
        UserBanListEntry found = null;
        for (UserBanListEntry entry : bans.getEntries()) {
            if (Objects.equals(entry.getDisplayName().getString(), name)) {
                found = entry;
                success = true;
                break;
            }
        }
        if (!success) return InteractionResult.FAIL;
        try {
            playerUUID = BanListHelper.getBannedPlayerUuidByName(bans.getFile(), name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bans.remove(found);
        // Advancement for respawned player:
        String advancementID = "simple_hardcore_respawn:back_one_day";
        AdvancementHolder advancement = level.getServer().getAdvancements().get(ResourceLocation.parse(advancementID));
        if (advancement != null && playerUUID != null)
            Advancements.grantOfflineAdvancement(level.getServer(), playerUUID, advancementID, advancement.value());
        // Consume power:
        if (level.getGameRules().getBoolean(SimpleHardcoreRespawnGameRules.RESPAWNER_CHARGING)) {
            BlockState state = context.base().pState()
                    .setValue(ReSpawnerBlock.CHARGES, context.base().pState().getValue(ReSpawnerBlock.CHARGES) - 1);
            level.setBlock(context.base().pPos(), state, Block.UPDATE_CLIENTS);
        }
        // Consume item:
        ItemStack stack = context.base().pStack();
        if (!context.base().pPlayer().isCreative())
            if (stack.isDamageableItem()) {
                EquipmentSlot slot = context.base().pHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, context.base().pPlayer(), slot);
            } else
                stack.shrink(1);

        return InteractionResult.SUCCESS;
    }

    //

    @Override
    public void respawnEffect(@NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        level.playSound(null, context.base().pPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 2, 1);
    }

    @Override
    public void respawnFailedEffect(@NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        level.playSound(null, context.base().pPos(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 2, 1);
    }

    @Override
    public void chargeEffect(int pPower, @NotNull ServerBlockUseContext context) {
        ServerLevel level = context.level();
        level.playSound(null, context.base().pPos(), SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 2, (float) (context.base().pState().getValue(ReSpawnerBlock.POWER) / 10 + 2));
    }

    @Override
    public void chargeFailedEffect(int i, @NotNull ServerBlockUseContext serverBlockUseContext) { }

    @Override
    public void easterEggEffect(@NotNull ServerBlockUseContext context) {
        Constants.LOG.error("Respawner action interrupted by player [069a79f4-44e9-4726-a5be-fca90e38aaf5]");
        context.level().sendParticles(ParticleTypes.SOUL, (context.base().pPos().getX() + 0.5), (context.base().pPos().getY() + 2), (context.base().pPos().getZ() + 0.5), 1, 0, 0, 0, 0);
    }

    @Override
    public void respawnParticlesEffect(@NotNull ServerBlockUseContext serverBlockUseContext) {
        RespawnParticles.totem(serverBlockUseContext);
    }
}
