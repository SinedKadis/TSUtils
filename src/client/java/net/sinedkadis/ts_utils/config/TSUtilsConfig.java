package net.sinedkadis.ts_utils.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "sacf")
public class TSUtilsConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean showToggleMessages = true;

    public static void register() {
        AutoConfig.register(TSUtilsConfig.class, Toml4jConfigSerializer::new);
    }

    public static TSUtilsConfig get() {
        return AutoConfig.getConfigHolder(TSUtilsConfig.class).getConfig();
    }
}
