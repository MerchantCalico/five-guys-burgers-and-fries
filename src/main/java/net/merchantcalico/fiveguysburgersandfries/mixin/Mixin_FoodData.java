package net.merchantcalico.fiveguysburgersandfries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.merchantcalico.fiveguysburgersandfries.data.ExtraFoodData;
import net.merchantcalico.fiveguysburgersandfries.registry.FiveGuysAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public class Mixin_FoodData {
	@Shadow
	private float exhaustionLevel;

	@Shadow
	private float saturationLevel;

	@SuppressWarnings("UnstableApiUsage")
	@ModifyExpressionValue(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/food/FoodData;exhaustionLevel:F", ordinal = 0))
	private float fiveguysburgersandfries$exhaustFromExtraFood(float original, @Local(argsOnly = true) ServerPlayer player) {
		if (original  > 4.0F && player.hasAttached(FiveGuysAttachments.EXTRA_FOOD_DATA)) {
			exhaustionLevel -= 4.0F;
			ExtraFoodData extraFoodData = player.getAttached(FiveGuysAttachments.EXTRA_FOOD_DATA);
			int extraFood;
			float extraSaturation;
			if (extraFoodData.extraSaturationLevel() > 0.0F) {
				extraFood = extraFoodData.extraFoodLevel();
				extraSaturation = Math.max(extraFoodData.extraSaturationLevel() - 1.0F, 0.0F);
			} else if (saturationLevel > 0.0F) {
				return original; // Cancel early to move to regular saturation logic.
			} else {
				extraFood = Math.max(extraFoodData.extraFoodLevel() - 1, 0);
				extraSaturation = 0.0F;
			}
			if (extraFood == 0) {
				player.removeAttached(FiveGuysAttachments.EXTRA_FOOD_DATA);
			} else {
				player.setAttached(FiveGuysAttachments.EXTRA_FOOD_DATA, new ExtraFoodData(extraFood, extraSaturation));
			}
			return Float.MIN_VALUE; // Make the original expression false.
		}
		return original;
	}
}
