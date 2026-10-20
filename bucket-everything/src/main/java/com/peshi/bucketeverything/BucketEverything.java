package com.peshi.bucketeverything;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketEverything implements ModInitializer {
	public static final String MOD_ID = "bucket-everything";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<Item> MOB_BUCKET_KEY = ResourceKey.create(Registries.ITEM, id("mob_bucket"));
	public static final Item MOB_BUCKET = Registry.register(BuiltInRegistries.ITEM, MOB_BUCKET_KEY,
		new MobBucketItem(new Item.Properties().setId(MOB_BUCKET_KEY).stacksTo(1)));

	public static final MobTrigger BUCKETED_MOB = Registry.register(BuiltInRegistries.TRIGGER_TYPES, id("bucketed_mob"), new MobTrigger());
	public static final MobTrigger RELEASED_MOB = Registry.register(BuiltInRegistries.TRIGGER_TYPES, id("released_mob"), new MobTrigger());

	@Override
	public void onInitialize() {
		UseEntityCallback.EVENT.register(MobCapture::capture);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
