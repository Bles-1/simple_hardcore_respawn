package net.ultrastudios.simplehardcorerespawn.platform;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.ultrastudios.simplehardcorerespawn.Constants;
import net.ultrastudios.simplehardcorerespawn.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
        T registered = Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), block.get());
        return () -> registered;
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        T registered = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, id), item.get());
        return () -> registered;
    }

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String id, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(id, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    @Override
    public GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String id, GameRules.Category category, int defaultValue) {
        return GameRuleRegistry.register(id, category, GameRuleFactory.createIntRule(defaultValue));
    }
}
