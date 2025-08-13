package net.merchantcalico.fiveguysburgersandfries.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantcalico.fiveguysburgersandfries.registry.FiveGuysAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodConstants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record ExtraFoodConsumeEffect(int extraFood, float extraSaturationModifier, int maxExtraFood) implements ConsumeEffect {
	public static final MapCodec<ExtraFoodConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.INT.fieldOf("extra_food").forGetter(ExtraFoodConsumeEffect::extraFood),
		Codec.FLOAT.fieldOf("extra_saturation").forGetter(ExtraFoodConsumeEffect::extraSaturationModifier),
		Codec.INT.fieldOf("max_extra_food").forGetter(ExtraFoodConsumeEffect::maxExtraFood)
	).apply(inst, ExtraFoodConsumeEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ExtraFoodConsumeEffect> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, ExtraFoodConsumeEffect::extraFood,
		ByteBufCodecs.FLOAT, ExtraFoodConsumeEffect::extraSaturationModifier,
		ByteBufCodecs.INT, ExtraFoodConsumeEffect::maxExtraFood,
		ExtraFoodConsumeEffect::new
	);
	public static final Type<ExtraFoodConsumeEffect> TYPE = new Type<>(CODEC, STREAM_CODEC);

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
		if (entity instanceof Player player) {
			int extraFoodLevel;
			float extraSaturation;
			if (entity.hasAttached(FiveGuysAttachments.EXTRA_FOOD_DATA)) {
				ExtraFoodData extraFoodData = entity.getAttachedOrElse(FiveGuysAttachments.EXTRA_FOOD_DATA, ExtraFoodData.EMPTY);
				extraFoodLevel = Math.min(maxExtraFood, extraFoodData.extraFoodLevel() + extraFood);
				extraSaturation = Math.min(maxExtraFood, extraFoodData.extraSaturationLevel() + FoodConstants.saturationByModifier(extraFoodLevel, extraSaturationModifier));
			} else {
				extraFoodLevel = Math.min(maxExtraFood, extraFood);
				extraSaturation = Math.min(maxExtraFood, FoodConstants.saturationByModifier(extraFoodLevel, extraSaturationModifier));
			}
			int foodLevel = player.getFoodData().getFoodLevel();

			if (foodLevel < 20) {
				int food = Math.max(20, foodLevel + extraFoodLevel);
				extraFoodLevel = foodLevel + extraFoodLevel - food;
				player.getFoodData().setFoodLevel(food);
			}

			float saturationLevel = player.getFoodData().getSaturationLevel();
			if (saturationLevel < player.getFoodData().getFoodLevel()) {
				float saturation = Math.max(player.getFoodData().getFoodLevel(), saturationLevel + extraSaturation);
				extraSaturation = Math.clamp(extraSaturation - saturation, 0.0F, foodLevel + extraFoodLevel);
				player.getFoodData().setSaturation(saturation);
			}

			player.setAttached(FiveGuysAttachments.EXTRA_FOOD_DATA, new ExtraFoodData(extraFoodLevel, extraSaturation));
		}
		return true;
	}

	@Override
	public @NotNull Type<? extends ConsumeEffect> getType() {
		return TYPE;
	}
}
