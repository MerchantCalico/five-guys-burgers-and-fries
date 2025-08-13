package net.merchantcalico.fiveguysburgersandfries.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.merchantcalico.fiveguysburgersandfries.FiveGuysBurgersAndFries;
import net.merchantcalico.fiveguysburgersandfries.client.util.FoodGuiIcons;
import net.merchantcalico.fiveguysburgersandfries.data.ExtraFoodData;
import net.merchantcalico.fiveguysburgersandfries.registry.FiveGuysAttachments;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class FiveGuysExtraFoodHudElement implements HudElement {
	public static final ResourceLocation ID = FiveGuysBurgersAndFries.id("extra_food");
	public static final FiveGuysExtraFoodHudElement INSTANCE = new FiveGuysExtraFoodHudElement();

	private FiveGuysExtraFoodHudElement() {}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void render(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
		int y = guiGraphics.guiHeight() - 39;
		int x = guiGraphics.guiWidth() / 2 + 91;
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;
		RandomSource random = player.getRandom();
		ExtraFoodData extraFood = player.getAttachedOrElse(FiveGuysAttachments.EXTRA_FOOD_DATA, ExtraFoodData.EMPTY);
		int extraFoodLevel = extraFood.extraFoodLevel();
		int heightModifier = Math.max(10 - (Mth.ceil((20 + extraFoodLevel) / 2.0F / 10.0F) - 2), 3);
		for (int j = extraFoodLevel - 1; j >= 0; --j) {
			int index = j % 20 / 2;
			int height = y - ((j / 20 + 1) * heightModifier);
			ResourceLocation emptySprite;
			ResourceLocation emptyHalfSprite;
			ResourceLocation halfHunger;
			ResourceLocation fullHunger;
			if (player.hasEffect(MobEffects.HUNGER)) {
				emptySprite = FoodGuiIcons.FOOD_EMPTY_HUNGER_SPRITE;
				emptyHalfSprite = FoodGuiIcons.FOOD_EMPTY_HALF_HUNGER_SPRITE;
				halfHunger = FoodGuiIcons.FOOD_HALF_HUNGER_SPRITE;
				fullHunger = FoodGuiIcons.FOOD_FULL_HUNGER_SPRITE;
			} else {
				emptySprite = FoodGuiIcons.FOOD_EMPTY_SPRITE;
				emptyHalfSprite = FoodGuiIcons.FOOD_EMPTY_HALF_SPRITE;
				halfHunger = FoodGuiIcons.FOOD_HALF_SPRITE;
				fullHunger = FoodGuiIcons.FOOD_FULL_SPRITE;
			}

			if (player.getFoodData().getSaturationLevel() <= 0.0F && extraFood.extraSaturationLevel() <= 0.0F &&
				Minecraft.getInstance().gui.getGuiTicks() % (player.getFoodData().getFoodLevel() * 3 + 1) == 0) {
				height = height + (random.nextInt(3) - 1);
			}

			int l = x - index * 8 - 9;

			if (j + 1 == extraFoodLevel) {
				guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, emptyHalfSprite, l, height, 9, 9);
				guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, halfHunger, l, height, 9, 9);
			} else if (j + 1 < extraFoodLevel) {
				guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, emptySprite, l, height, 9, 9);
				guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, fullHunger, l, height, 9, 9);
			}
		}
	}
}
