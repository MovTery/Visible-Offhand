package com.movtery.visibleoffhand;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.movtery.visibleoffhand.config.Config;
import com.movtery.visibleoffhand.screen.RegisterModsPage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Mod(VisibleOffhand.MODID)
public class VisibleOffhand {

    public static final String MODID = "visible_offhand";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Lazy<KeyMapping> KEY_DOUBLE_HANDS = Lazy.of(() -> new KeyMapping(  //创建一个新的按键绑定
            "button.vo.double_hands",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "mod.vo.name"
    ));

    private static Config config = null;

    public VisibleOffhand(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> RegisterModsPage::registerModsPage);

        NeoForge.EVENT_BUS.register(this);
    }

    public static Config getConfig() {
        if (config == null) loadConfig();
        return config;
    }

    //重新生成配置文件
    private static void loadConfig() {
        Path configPath = FMLLoader.getGamePath();
        File configFile = new File(configPath.toFile() + "/config/", "visible_offhand.json");
        config = new Config(configFile);
        config.load();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //显示双手开关
            while (KEY_DOUBLE_HANDS.get().consumeClick()) {
                getConfig().getOptions().doubleHands = !getConfig().getOptions().doubleHands;
                getConfig().save();

                //给玩家发送提示消息
                if (Minecraft.getInstance().player != null) {
                    Component component;
                    if (getConfig().getOptions().doubleHands) {
                        component = Component.translatable("button.vo.double_hands").append(" : ").append(Component.translatable("button.vo.on"));
                    } else {
                        component = Component.translatable("button.vo.double_hands").append(" : ").append(Component.translatable("button.vo.off"));
                    }
                    Minecraft.getInstance().player.displayClientMessage(component, true);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //检测是否生成过配置文件
            if (config == null) {
                loadConfig();
                LOGGER.info("Config file not found, has been regenerated");
            }
        }

        //注册按键绑定
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(KEY_DOUBLE_HANDS.get());
        }
    }
}
