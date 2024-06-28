package com.movtery.visible_offhand.datageneration.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ZHCNLangProvider extends FabricLanguageProvider {
    public ZHCNLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("modmenu.nameTranslation.visible_offhand", "可见副手");
        translationBuilder.add("modmenu.descriptionTranslation.visible_offhand", "让你的第一人称视角内同时渲染两只手！");
        translationBuilder.add("button.vo.double_hands", "显示双手");
        translationBuilder.add("button.vo.reload_config", "重载配置文件/完成");
        translationBuilder.add("button.vo.on", "开");
        translationBuilder.add("button.vo.off", "关");
        translationBuilder.add("config.vo.reloaded", "配置文件已经重新加载!");
    }
}
