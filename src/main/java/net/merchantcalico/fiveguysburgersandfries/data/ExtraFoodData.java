package net.merchantcalico.fiveguysburgersandfries.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ExtraFoodData(int extraFoodLevel, float extraSaturationLevel) {
	public static final ExtraFoodData EMPTY = new ExtraFoodData(0, 0.0F);

	public static final Codec<ExtraFoodData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ExtraCodecs.POSITIVE_INT.fieldOf("extra_food_level").forGetter(ExtraFoodData::extraFoodLevel),
		ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("extra_saturation_level").forGetter(ExtraFoodData::extraSaturationLevel)
	).apply(inst, ExtraFoodData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ExtraFoodData> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.INT, ExtraFoodData::extraFoodLevel,
		ByteBufCodecs.FLOAT, ExtraFoodData::extraSaturationLevel,
		ExtraFoodData::new
	);
}
