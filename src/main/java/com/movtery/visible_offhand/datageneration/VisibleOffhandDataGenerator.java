package com.movtery.visible_offhand.datageneration;

import com.movtery.visible_offhand.datageneration.language.ENUSLangProvider;
import com.movtery.visible_offhand.datageneration.language.ZHCNLangProvider;
import com.movtery.visible_offhand.datageneration.language.ZHTWLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class VisibleOffhandDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ENUSLangProvider::new);
		pack.addProvider(ZHCNLangProvider::new);
		pack.addProvider(ZHTWLangProvider::new);
	}
}
