package com.movtery.visible_offhand.datageneration.language;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ENUSLangProvider extends FabricLanguageProvider {
    public ENUSLangProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("modmenu.nameTranslation.visible_offhand", "Visible Offhand");
        translationBuilder.add("modmenu.descriptionTranslation.visible_offhand", "Show both hands at the same time!");
        translationBuilder.add("button.vo.double_hands", "Double Hands");
        translationBuilder.add("button.vo.reload_config", "Reload Profile/Done");
        translationBuilder.add("button.vo.on", "ON");
        translationBuilder.add("button.vo.off", "OFF");
        translationBuilder.add("config.vo.reloaded", "The configuration file has been reloaded!");
    }
}
