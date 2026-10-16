package com.peshi.mobduplicator.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import com.peshi.mobduplicator.DropSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	private static final RandomSource RANDOM = RandomSource.create();

	// hooks the loot call, the method head injection never matched
	@ModifyArg(
		method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;Ljava/util/function/Consumer;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;JLjava/util/function/Consumer;)V"
		),
		index = 2
	)
	private Consumer<ItemStack> mobduplicator$multiplyDrops(Consumer<ItemStack> drops) {
		return stack -> {
			Optional<Holder.Reference<Item>> item = BuiltInRegistries.ITEM.getRandom(RANDOM);
			if (item.isEmpty()) {
				return;
			}

			ItemStack randomStack = new ItemStack(item.get(), 1);
			if (randomStack.isEmpty()) {
				return;
			}

			int min = DropSettings.minMultiplier();
			int total = stack.getCount() * (min + RANDOM.nextInt(DropSettings.maxMultiplier() - min + 1));
			int maxStackSize = randomStack.getOrDefault(DataComponents.MAX_STACK_SIZE, Item.DEFAULT_MAX_STACK_SIZE);

			while (total > 0) {
				int amount = Math.min(total, maxStackSize);
				drops.accept(randomStack.copyWithCount(amount));
				total -= amount;
			}
		};
	}
}
