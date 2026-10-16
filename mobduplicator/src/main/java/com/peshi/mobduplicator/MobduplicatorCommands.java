package com.peshi.mobduplicator;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class MobduplicatorCommands {
	private static final int MAX_MULTIPLIER_LIMIT = 100;

	private MobduplicatorCommands() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(Commands.literal("mobduplicator")
				.requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
				.then(Commands.literal("multiplier")
					.then(Commands.argument("min", IntegerArgumentType.integer(1, MAX_MULTIPLIER_LIMIT))
						.then(Commands.argument("max", IntegerArgumentType.integer(1, MAX_MULTIPLIER_LIMIT))
							.executes(context -> setMultiplier(context.getSource(),
								IntegerArgumentType.getInteger(context, "min"),
								IntegerArgumentType.getInteger(context, "max")))))))
		);
	}

	private static int setMultiplier(CommandSourceStack source, int min, int max) {
		if (min > max) {
			source.sendFailure(Component.literal("min must be less than or equal to max"));
			return 0;
		}

		DropSettings.setMultiplier(min, max);
		source.sendSuccess(() -> Component.literal("Drop multiplier set to " + min + " to " + max), true);
		return 1;
	}
}
