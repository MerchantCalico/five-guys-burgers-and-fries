package net.merchantcalico.fiveguysburgersandfries.registry;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.merchantcalico.fiveguysburgersandfries.FiveGuysBurgersAndFries;
import net.merchantcalico.fiveguysburgersandfries.data.ExtraFoodData;

public class FiveGuysAttachments {
	public static final AttachmentType<ExtraFoodData> EXTRA_FOOD_DATA = AttachmentRegistry.create(FiveGuysBurgersAndFries.id("extra_food_data"), builder ->
		builder.persistent(ExtraFoodData.CODEC)
			.syncWith(ExtraFoodData.STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
	);

	private FiveGuysAttachments() {
		throw new UnsupportedOperationException(getClass().getCanonicalName() + "should not be initialized as it only contains static methods/fields.");
	}

	public static void registerAll() {}
}
