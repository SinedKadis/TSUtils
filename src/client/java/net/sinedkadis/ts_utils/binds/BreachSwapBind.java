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
        ClientTickEvents.START_CLIENT_TICK.register(BreachSwapBind::onTick);
    }

    public static void onTick(MinecraftClient client) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        attackPressState = minecraftClient.options.attackKey.isPressed();
        boolean isPressed = BREACH_SWAP_BIND.isPressed();
        if (isPressed && !wasPressedLastTick) {
            toggleState = !toggleState;
            TSUtilsConfig.get().breachSwap = toggleState;
            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
                client.player.sendMessage(Text.translatable(toggleState ? "key.ts_utils.breachSwap.enabled" : "key.ts_utils.breachSwap.disabled"), false);
            }
        }
        if (attackPressState && !wasAttackedLastTick && toggleState){
            wasPressedTickCoolDown = 4;
            if (player != null) {
                int selectedSlot = player.getInventory().selectedSlot;
                if (selectedSlot != TSUtilsConfig.get().breachSwapSlot-1) {
                    lastSlot = selectedSlot;
                }
                player.getInventory().selectedSlot = TSUtilsConfig.get().breachSwapSlot-1;
            }
        }
        if (wasPressedTickCoolDown == 0){
            if (player != null && lastSlot != -1) {
                player.getInventory().selectedSlot = lastSlot;
                lastSlot = -1;
            }
        }
        wasPressedTickCoolDown = Math.max(-1,wasPressedTickCoolDown-1);
        wasAttackedLastTick = attackPressState;
        wasPressedLastTick = isPressed;
        toggleState = TSUtilsConfig.get().breachSwap;
//        if (isPressed && !wasPressedLastTick) {
//            toggleState = !toggleState;
//            applyScale();
//            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
//                client.player.sendMessage(Text.translatable(toggleState ? "key.sacf.size_attribute_bind.enabled" : "key.sacf.size_attribute_bind.disabled"), false);
//            }
//        }
//
//        wasPressedLastTick = isPressed;
    }
}
