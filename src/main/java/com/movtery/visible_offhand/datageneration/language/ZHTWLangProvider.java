package com.movtery.visible_offhand.datageneration.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ZHTWLangProvider extends FabricLanguageProvider {
    public ZHTWLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "zh_tw", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        //https://github.com/HopiHopy/Visible-Offhand/pull/3
        translationBuilder.add("modmenu.nameTranslation.visible_offhand", "可見副手");
        translationBuilder.add("modmenu.descriptionTranslation.visible_offhand", "讓你的第一人稱視角內同時渲染兩隻手！");
        translationBuilder.add("button.vo.double_hands", "顯示雙手");
        translationBuilder.add("button.vo.reload_config", "重載配置文件/完成");
        translationBuilder.add("button.vo.on", "開");
        translationBuilder.add("button.vo.off", "關");
        translationBuilder.add("config.vo.reloaded", "配置文件已經重新載入!");
    }
}
