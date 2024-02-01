package com.movtery.visible_offhand;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.movtery.visible_offhand.config.Config;
import com.movtery.visible_offhand.screen.RegisterModsPage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
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

    public VisibleOffhand() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> RegisterModsPage::registerModsPage);

        MinecraftForge.EVENT_BUS.register(this);
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

    public static void reloadConfig() {
        if (config == null) loadConfig();
        config.load();
        LOGGER.info("The configuration file has been reloaded!");
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

    // 注册客户端命令
    @SubscribeEvent
    public void registerCommands(RegisterClientCommandsEvent clientCommandsEvent) {
        clientCommandsEvent.getDispatcher().register(Commands.literal("visibleoffhand")
                .then(Commands.literal("reload").executes((context) -> {
                    reloadConfig();
                    context.getSource().sendSystemMessage(Component.translatable("config.vo.reloaded"));
                    return 1;
                }))
        );
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

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent keyMappingsEvent) {
            //注册按键绑定
            keyMappingsEvent.register(KEY_DOUBLE_HANDS.get());
        }
    }
}
