package com.movtery.visible_offhand.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movtery.visible_offhand.VisibleOffhand;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

public class Config {
    private static Options options = null;
    private final File file;
    private final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public Config(File file) {
        this.file = file;
    }

    public static Options getOptions() {
        return options;
    }

    public void load() {
        if (file.exists()) {
            try {
                options = GSON.fromJson(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8), Options.class);
            } catch (IOException e) {
                VisibleOffhand.LOGGER.error("Error loading config");
            }

        }
        if (options == null) {
            options = new Options();
            save();
        }
    }

    public void save() {
        try {
            Files.write(file.toPath(), Collections.singleton(GSON.toJson(options)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            VisibleOffhand.LOGGER.error("Error saving config");
        }
    }

    public static class Options {
        public boolean doubleHands = true;
    }
}