package com.movtery.visibleoffhand.datageneration;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.movtery.visibleoffhand.VisibleOffhand.MODID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MODID)
public class DataGenerationEvent {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        generator.addProvider(
                event.includeClient(),
                new LanguageProvider(packOutput, MODID, "zh_cn") {
                    @Override
                    protected void addTranslations() {
                        add("mod.vo.name", "可见副手");
                        add("button.vo.double_hands", "显示双手");
                        add("button.vo.reload_config", "重载配置文件/完成");
                        add("button.vo.on", "开");
                        add("button.vo.off", "关");
                    }
                }
        );
        generator.addProvider(event.includeClient(),
                new LanguageProvider(packOutput, MODID, "zh_tw") {
                    @Override
                    protected void addTranslations() {
                        add("mod.vo.name", "可見副手");
                        add("button.vo.double_hands", "顯示雙手");
                        add("button.vo.reload_config", "重載配置文件/完成");
                        add("button.vo.on", "開");
                        add("button.vo.off", "關");
                    }
                });
        generator.addProvider(event.includeClient(),
                new LanguageProvider(packOutput, MODID, "en_us") {
                    @Override
                    protected void addTranslations() {
                        add("mod.vo.name", "Visible Offhand");
                        add("button.vo.double_hands", "Double Hands");
                        add("button.vo.reload_config", "Reload Profile/Done");
                        add("button.vo.on", "ON");
                        add("button.vo.off", "OFF");
                    }
                });
    }
}
