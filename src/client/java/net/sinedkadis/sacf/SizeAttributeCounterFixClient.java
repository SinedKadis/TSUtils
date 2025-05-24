package net.sinedkadis.sacf;

import net.fabricmc.api.ClientModInitializer;
import net.sinedkadis.sacf.binds.SizeAttributeBind;
import net.sinedkadis.sacf.config.SACFConfig;

public class SizeAttributeCounterFixClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SizeAttributeBind.register();
		SACFConfig.register();
	}
}