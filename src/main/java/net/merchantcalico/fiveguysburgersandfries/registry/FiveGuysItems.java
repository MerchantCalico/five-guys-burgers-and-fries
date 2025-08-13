package net.merchantcalico.fiveguysburgersandfries.registry;

import net.merchantcalico.fiveguysburgersandfries.FiveGuysBurgersAndFries;
import net.merchantcalico.fiveguysburgersandfries.data.ExtraFoodConsumeEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;

import java.util.function.Function;

public class FiveGuysItems {
	public static final FoodProperties BURGER_AND_FRIES_FOOD = new FoodProperties.Builder()
		.nutrition(20)
		.saturationModifier(0.5F)
		.alwaysEdible()
		.build();
	public static final Consumable BURGERS_AND_FRIES_CONSUMABLE = Consumable.builder()
		.consumeSeconds(3.2F)
		.onConsume(new ExtraFoodConsumeEffect(80, 0.5F, 80))
		.build();

	public static final Item BURGER_AND_FRIES = registerItem("burger_and_fries", Item::new,
		new Item.Properties().food(BURGER_AND_FRIES_FOOD).component(DataComponents.CONSUMABLE, BURGERS_AND_FRIES_CONSUMABLE));

	private FiveGuysItems() {
		throw new UnsupportedOperationException(getClass().getCanonicalName() + "should not be initialized as it only contains static methods/fields.");
	}

	public static void registerAll() {}


	public static Item registerItem(String path, Function<Item.Properties, Item> factory, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, FiveGuysBurgersAndFries.id(path));
		Item item = factory.apply(properties.setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}
}
