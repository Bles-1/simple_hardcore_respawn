package net.ultrastudios.simplehardcorerespawn.platform.services;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Registers new block.
     *
     * @param id The id of block.
     * @param block The supplier that will return new instance of block.
     * @return The supplier that will return registered block.
     * @param <T> Your block class, or Block if you don't have custom class.
     */
    <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block);

    /**
     * Registers new item.
     *
     * @param id The id of item.
     * @param item The supplier that will return new instance of item.
     * @return The supplier that will return registered item.
     * @param <T> Your item class, or Item if you don't have custom class.
     */
    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);

    GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String id, GameRules.Category category, boolean defaultValue);

    GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String id, GameRules.Category category, int defaultValue);

    /**
     * Gets the directory where game/server is run from (usually .minecraft for clients).
     *
     * @return The directory of instance.
     */
    Path getGameDir();

    /**
     * Gets the directory where config files are usually stored.
     *
     * @return The config directory.
     */
    Path getConfigDir();
}