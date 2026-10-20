package com.peshi.bucketeverything;

import java.util.List;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.EntityHitResult;

public final class MobCapture {
	private MobCapture() {
	}

	public static boolean canBucket(Entity entity) {
		// fish and axolotls keep their vanilla water bucket behaviour
		return entity instanceof Mob && entity.isAlive() && !(entity instanceof Bucketable) && !(entity instanceof EnderDragon);
	}

	// milkable mobs need sneaking 
	private static boolean needsSneak(Entity entity) {
		return (entity instanceof AbstractCow || entity instanceof Goat) && !((Mob) entity).isBaby();
	}

	public static InteractionResult capture(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
		ItemStack held = player.getItemInHand(hand);
		if (player.isSpectator() || !held.is(Items.BUCKET) || !canBucket(entity)
			|| (needsSneak(entity) && !player.isShiftKeyDown())) {
			return InteractionResult.PASS;
		}

		if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.SUCCESS;
		}

		Mob mob = (Mob) entity;
		mob.ejectPassengers();
		mob.stopRiding();
		mob.dropLeash();

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, serverLevel.registryAccess());
		mob.saveWithoutId(output);
		CompoundTag tag = output.buildResult();

		ItemStack filled = new ItemStack(BucketEverything.MOB_BUCKET);
		filled.set(DataComponents.ENTITY_DATA, TypedEntityData.of(mob.getType(), tag));
		// the item model picks the mob texture from this string
		String typeId = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).toString();
		filled.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(typeId), List.of()));

		serverLevel.playSound(null, mob.blockPosition(), SoundEvents.BUCKET_FILL_FISH, SoundSource.PLAYERS, 1.0F, 1.0F);
		BucketEverything.BUCKETED_MOB.trigger(serverPlayer, mob);
		mob.discard();

		player.setItemInHand(hand, ItemUtils.createFilledResult(held, player, filled, false));
		return InteractionResult.SUCCESS;
	}
}
