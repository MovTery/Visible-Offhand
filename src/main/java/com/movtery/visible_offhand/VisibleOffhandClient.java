package com.movtery.visible_offhand;

import com.mojang.blaze3d.platform.InputConstants;
import com.movtery.visible_offhand.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.file.Path;

public class VisibleOffhandClient implements ClientModInitializer {
	private static Config config = null;
	KeyMapping doubleHands;

	public static Config getConfig() {
		if (config == null) loadConfig();
		return config;
	}

	private static void loadConfig() {
		Path configPath = FabricLoader.getInstance().getConfigDir();
		File configFile = new File(configPath.toFile(), "visible_offhand.json");
		config = new Config(configFile);
		config.load();
	}

	public static void reloadConfig() {
		if (config == null) loadConfig();
		config.load();
		VisibleOffhand.LOGGER.info("The configuration file has been reloaded!");
	}

	@Override
	public void onInitializeClient() {
		doubleHands = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"button.vo.double_hands",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				"modmenu.nameTranslation.visible_offhand"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (doubleHands.consumeClick()) {
                //读写配置文件
                getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                getConfig().save();

                //给玩家发送一条提示语
                if (client.player != null) {
                    Component component;
                    if (getConfig().getOptions().doubleHands) {
                        component = Component.translatable("button.vo.double_hands").append(" : ").append(Component.translatable("button.vo.on"));
                    } else {
                        component = Component.translatable("button.vo.double_hands").append(" : ").append(Component.translatable("button.vo.off"));
                    }
                    client.player.displayClientMessage(component, true);
                }
            }
        });
	}
}