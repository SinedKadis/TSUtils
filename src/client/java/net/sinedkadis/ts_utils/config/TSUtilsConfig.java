package net.sinedkadis.ts_utils.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "ts_utils")
public class TSUtilsConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean showToggleMessages = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category(value = "size_attribute_category")
    public boolean size_attribute = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category(value = "breach_swap_category")
    public boolean breachSwap = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category(value = "breach_swap_category")
    public boolean breachSwapOnFullCharge = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1,max = 9)
    @ConfigEntry.Category(value = "breach_swap_category")
    public int breachSwapSlot = 6;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Category(value = "breach_swap_category")
    public boolean breachSwapFromSlotAllow = false;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1,max = 9)
    @ConfigEntry.Category(value = "breach_swap_category")
    public int breachSwapSlotFrom = 3;

    public static void register() {
        AutoConfig.register(TSUtilsConfig.class, Toml4jConfigSerializer::new);
    }

    public static TSUtilsConfig get() {
        return AutoConfig.getConfigHolder(TSUtilsConfig.class).getConfig();
    }
}
