package net.merchantcalico.fiveguysburgersandfries;

import net.fabricmc.api.ModInitializer;
import net.merchantcalico.fiveguysburgersandfries.registry.FiveGuysAttachments;
import net.merchantcalico.fiveguysburgersandfries.registry.FiveGuysItems;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiveGuysBurgersAndFries implements ModInitializer {
	public static final String MOD_ID = "fiveguysburgersandfries";
	public static final String MOD_NAME = "5 Guys (Burgers and Fries)";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	@Override
	public void onInitialize() {
		FiveGuysAttachments.registerAll();
		FiveGuysItems.registerAll();
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
