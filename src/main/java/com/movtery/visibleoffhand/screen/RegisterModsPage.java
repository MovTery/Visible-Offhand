package com.movtery.visibleoffhand.screen;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;

public class RegisterModsPage {
    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen)));
    }
}
