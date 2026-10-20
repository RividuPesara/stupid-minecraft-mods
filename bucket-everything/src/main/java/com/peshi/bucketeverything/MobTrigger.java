package com.peshi.bucketeverything;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class MobTrigger extends SimpleCriterionTrigger<MobTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Entity entity) {
		LootContext context = EntityPredicate.createContext(player, entity);
		trigger(player, instance -> instance.matches(context));
	}

	public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Optional<Holder<LootItemCondition>> entity)
		implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity)
		).apply(instance, TriggerInstance::new));

		public boolean matches(LootContext context) {
			return entity.isEmpty() || entity.get().value().test(context);
		}

		@Override
		public void validate(ValidationContextSource source) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(source);
			Validatable.validateHolder(source.entityContext(), "entity", entity);
		}
	}
}
