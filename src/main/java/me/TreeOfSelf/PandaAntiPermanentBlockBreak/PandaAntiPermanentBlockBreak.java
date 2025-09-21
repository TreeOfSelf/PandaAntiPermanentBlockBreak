package me.TreeOfSelf.PandaAntiPermanentBlockBreak;

import net.fabricmc.api.ModInitializer;

import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PandaAntiPermanentBlockBreak implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("panda-anti-permanent-block-break");

	@Override
	public void onInitialize() {
		PandaAntiPermanentBlockBreakConfig.loadConfig();
		LOGGER.info("PandaAntiPermanentBlockBreak Started!");
	}
}