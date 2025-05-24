package net.sinedkadis.sacf.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "sacf")
public class SACFConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean showToggleMessages = true;

    public static void register() {
        AutoConfig.register(SACFConfig.class, Toml4jConfigSerializer::new);
    }

    public static SACFConfig get() {
        return AutoConfig.getConfigHolder(SACFConfig.class).getConfig();
    }
}
