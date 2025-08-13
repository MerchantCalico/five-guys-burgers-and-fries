package net.merchantcalico.fiveguysburgersandfries.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.merchantcalico.fiveguysburgersandfries.client.gui.FiveGuysExtraFoodHudElement;

public class FiveGuysBurgersAndFriesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HudElementRegistry.attachElementBefore(VanillaHudElements.FOOD_BAR, FiveGuysExtraFoodHudElement.ID,
			FiveGuysExtraFoodHudElement.INSTANCE);
	}
}
