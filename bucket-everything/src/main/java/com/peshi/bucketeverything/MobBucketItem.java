package com.peshi.bucketeverything;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class MobBucketItem extends Item {
	public MobBucketItem(Properties properties) {
		super(properties);
	}

	@Override
	public Component getName(ItemStack stack) {
		TypedEntityData<EntityType<?>> data = stack.get(DataComponents.ENTITY_DATA);
		if (data == null) {
			return super.getName(stack);
		}
		return Component.translatable(getDescriptionId() + ".filled", data.type().getDescription());
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		TypedEntityData<EntityType<?>> data = stack.get(DataComponents.ENTITY_DATA);
		if (data == null) {
			return InteractionResult.PASS;
		}

		if (!(context.getLevel() instanceof ServerLevel level)) {
			return InteractionResult.SUCCESS;
		}

		BlockPos clicked = context.getClickedPos();
		BlockPos pos = level.getBlockState(clicked).getCollisionShape(level, clicked).isEmpty()
			? clicked
			: clicked.relative(context.getClickedFace());

		Entity entity = data.type().create(level, EntitySpawnReason.BUCKET);
		if (entity == null) {
			return InteractionResult.FAIL;
		}

		// loadInto keeps the fresh uuid so creative mode copies never collide
		data.loadInto(entity);
		entity.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, Mth.wrapDegrees(context.getRotation() + 180.0F), 0.0F);
		entity.setYHeadRot(entity.getYRot());
		entity.setDeltaMovement(Vec3.ZERO);
		entity.resetFallDistance();
		level.addFreshEntity(entity);

		level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.PLAYERS, 1.0F, 1.0F);
		level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, pos);

		if (context.getPlayer() instanceof ServerPlayer player) {
			BucketEverything.RELEASED_MOB.trigger(player, entity);
			player.setItemInHand(context.getHand(), ItemUtils.createFilledResult(stack, player, new ItemStack(Items.BUCKET)));
		}
		return InteractionResult.SUCCESS;
	}
}
