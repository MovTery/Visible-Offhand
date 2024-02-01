package com.movtery.visible_offhand.client;

import com.movtery.visible_offhand.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

@Environment(EnvType.CLIENT)
public class VisibleOffhandClient implements ClientModInitializer {
    public static String MOD_NAME = "Visible Offhand";
    public static Logger LOGGER = Logger.getLogger(MOD_NAME);
    private static Config config = null;
    //注册一个按键绑定 用于一键开关副手显示
    KeyBinding doubleHands;

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
        LOGGER.config("The configuration file has been reloaded!");
    }

    @Override
    public void onInitializeClient() {
        doubleHands = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "button.vo.double_hands",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "modmenu.nameTranslation.visible_offhand"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            //一键开关副手显示
            while (doubleHands.wasPressed()) {
                //读写配置文件
                getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                getConfig().save();

                //给玩家发送一条提示语
                if (client.player != null) {
                    Text text;
                    if (getConfig().getOptions().doubleHands) {
                        text = Text.translatable("button.vo.double_hands").append(" : ").append(Text.translatable("button.vo.on"));
                    } else {
                        text = Text.translatable("button.vo.double_hands").append(" : ").append(Text.translatable("button.vo.off"));
                    }
                    client.player.sendMessage(text, true);
                }
            }
        });
    }
}
