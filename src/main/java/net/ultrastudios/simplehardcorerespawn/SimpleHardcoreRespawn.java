package net.ultrastudios.simplehardcorerespawn;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnBlocks;
import net.ultrastudios.simplehardcorerespawn.init.SimpleHardcoreRespawnItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SimpleHardcoreRespawn.MOD_ID)
public class SimpleHardcoreRespawn {
    public static final String MOD_ID = "simple_hardcore_respawn";
	public static final Logger LOGGER = LogManager.getLogger(SimpleHardcoreRespawn.class);

	public SimpleHardcoreRespawn(FMLJavaModLoadingContext context) {
		IEventBus modEventBus = context.getModEventBus();

		SimpleHardcoreRespawnBlocks.BLOCKS.register(modEventBus);
		SimpleHardcoreRespawnItems.ITEMS.register(modEventBus);

		LOGGER.info(MOD_ID + " loaded and ready to save thousands of lives!");
	}
}