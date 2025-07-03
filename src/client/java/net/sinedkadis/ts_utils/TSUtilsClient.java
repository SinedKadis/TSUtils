package net.sinedkadis.ts_utils;

import net.fabricmc.api.ClientModInitializer;
import net.sinedkadis.ts_utils.binds.BreachSwapBind;
import net.sinedkadis.ts_utils.binds.SizeAttributeBind;
import net.sinedkadis.ts_utils.config.TSUtilsConfig;

public class TSUtilsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SizeAttributeBind.register();
		BreachSwapBind.register();
		TSUtilsConfig.register();
	}
}