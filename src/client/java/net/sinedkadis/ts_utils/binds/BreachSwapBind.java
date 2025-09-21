package net.sinedkadis.ts_utils.binds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sinedkadis.ts_utils.config.TSUtilsConfig;
import org.lwjgl.glfw.GLFW;

public class BreachSwapBind {
    private static KeyBinding BREACH_SWAP_BIND;
    public static boolean toggleState = false;
    private static boolean wasPressedLastTick = false;

    private static KeyBinding BREACH_SWAP_ON_FULL_CHARGE_BIND;
    public static boolean toggleStateFC = false;
    private static boolean wasPressedLastTickFC = false;

    private static KeyBinding BREACH_SWAP_FROM_SLOT_BIND;
    public static boolean toggleStateFS = false;
    private static boolean wasPressedLastTickFS = false;

    public static boolean attackPressState = false;
    private static boolean wasAttackedLastTick = false;
    private static int wasPressedTickCoolDown = -1;
    private static int lastSlot = -1;
    public static void register() {
        BREACH_SWAP_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ts_utils.breachSwap",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                "category.ts_utils.binds"
        ));
        BREACH_SWAP_ON_FULL_CHARGE_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ts_utils.breachSwapOnFullCharge",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_MENU,
                "category.ts_utils.binds"
        ));
        BREACH_SWAP_FROM_SLOT_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ts_utils.breachSwapFromSlotAllow",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "category.ts_utils.binds"
        ));
        ClientTickEvents.START_CLIENT_TICK.register(BreachSwapBind::onTick);
    }

    public static void onTick(MinecraftClient client) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        attackPressState = minecraftClient.options.attackKey.isPressed();
        boolean isPressed = BREACH_SWAP_BIND.isPressed();
        boolean isPressedFC = BREACH_SWAP_ON_FULL_CHARGE_BIND.isPressed();
        boolean isPressedFS = BREACH_SWAP_FROM_SLOT_BIND.isPressed();
        if (isPressed && !wasPressedLastTick) {
            toggleState = !toggleState;
            TSUtilsConfig.get().breachSwap = toggleState;
            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
                client.player.sendMessage(Text.translatable(toggleState ? "key.ts_utils.breachSwap.enabled" : "key.ts_utils.breachSwap.disabled"), false);
            }
        }
        if (isPressedFC && !wasPressedLastTickFC) {
            toggleStateFC = !toggleStateFC;
            TSUtilsConfig.get().breachSwapOnFullCharge = toggleStateFC;
            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
                client.player.sendMessage(Text.translatable(toggleStateFC ? "key.ts_utils.breachSwapOnFullCharge.enabled" : "key.ts_utils.breachSwapOnFullCharge.disabled"), false);
            }
        }
        if (isPressedFS && !wasPressedLastTickFS) {
            toggleStateFS = !toggleStateFS;
            TSUtilsConfig.get().breachSwapFromSlotAllow = toggleStateFS;
            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
                client.player.sendMessage(Text.translatable(toggleStateFS ? "key.ts_utils.breachSwapFromSlotAllow.enabled" : "key.ts_utils.breachSwapFromSlotAllow.disabled"), false);
            }
        }
        if (attackPressState && !wasAttackedLastTick && toggleState){
            if (player != null) {
                float attackCooldownProgress = player.getAttackCooldownProgress(0.5f);
                if (!TSUtilsConfig.get().breachSwapOnFullCharge || attackCooldownProgress == 1){
                    int selectedSlot = player.getInventory().getSelectedSlot();
                    if (selectedSlot != TSUtilsConfig.get().breachSwapSlot-1) {
                        lastSlot = selectedSlot;
                    }
                    if (!TSUtilsConfig.get().breachSwapFromSlotAllow || TSUtilsConfig.get().breachSwapSlotFrom-1 == selectedSlot) {
                        player.getInventory().setSelectedSlot(TSUtilsConfig.get().breachSwapSlot - 1);
                        wasPressedTickCoolDown = 4;
                    }
                }
            }
        }
        if (wasPressedTickCoolDown == 0){
            if (player != null && lastSlot != -1) {
                player.getInventory().setSelectedSlot(lastSlot);
                lastSlot = -1;
            }
        }
        wasPressedTickCoolDown = Math.max(-1,wasPressedTickCoolDown-1);
        wasAttackedLastTick = attackPressState;

        wasPressedLastTick = isPressed;
        wasPressedLastTickFC = isPressedFC;
        wasPressedLastTickFS = isPressedFS;

        toggleState = TSUtilsConfig.get().breachSwap;
        toggleStateFC = TSUtilsConfig.get().breachSwapOnFullCharge;
        toggleStateFS = TSUtilsConfig.get().breachSwapFromSlotAllow;
    }
}
