package net.sinedkadis.ts_utils.binds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.text.Text;
import net.sinedkadis.ts_utils.config.TSUtilsConfig;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SizeAttributeBind {
    private static KeyBinding SIZE_ATTRIBUTE_BIND;
    public static boolean toggleState = false;
    private static boolean wasPressedLastTick = false;
    public static Map<AttributeContainer, EntityAttributesS2CPacket.Entry> scaleAttributes = new ConcurrentHashMap<>();
    public static void register() {
        // Создаем бинд (клавиша "R" в данном случае)
        SIZE_ATTRIBUTE_BIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sacf.size_attribute_bind",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.sacf.binds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(SizeAttributeBind::updateScale);
    }

    public static void updateScale(MinecraftClient client) {
        boolean isPressed = SIZE_ATTRIBUTE_BIND.isPressed();

        if (isPressed && !wasPressedLastTick) {
            toggleState = !toggleState;
            applyScale();
            if (TSUtilsConfig.get().showToggleMessages && client.player != null) {
                client.player.sendMessage(Text.translatable(toggleState ? "key.sacf.size_attribute_bind.enabled" : "key.sacf.size_attribute_bind.disabled"), false);
            }
        }

        wasPressedLastTick = isPressed;
    }

    public static void applyScale() {
        if (!toggleState && !scaleAttributes.isEmpty()){
            for (Map.Entry<AttributeContainer, EntityAttributesS2CPacket.Entry> mapEntry : scaleAttributes.entrySet()) {
                EntityAttributeInstance attributeInstance = mapEntry.getKey().getCustomInstance(mapEntry.getValue().attribute());
                assert attributeInstance != null;
                attributeInstance.setBaseValue(mapEntry.getValue().base());
                attributeInstance.clearModifiers();
                mapEntry.getValue().modifiers().forEach(attributeInstance::addTemporaryModifier);
            }
        }
        if (toggleState && !scaleAttributes.isEmpty()){
            for (Map.Entry<AttributeContainer, EntityAttributesS2CPacket.Entry> mapEntry : scaleAttributes.entrySet()){
                EntityAttributeInstance attributeInstance = mapEntry.getKey().getCustomInstance(mapEntry.getValue().attribute());
                assert attributeInstance != null;
                attributeInstance.setBaseValue(mapEntry.getValue().attribute().value().getDefaultValue());
                attributeInstance.clearModifiers();
            }
        }
    }

    public static void delayedApplyScale(MinecraftClient client) {
        client.execute(
                () -> client.execute(
                        () -> client.execute(
                                () -> client.execute(
                                        () -> client.execute(
                                                () -> client.execute(
                                                        () -> client.execute(
                                                                () -> client.execute(
                                                                        () -> client.execute(
                                                                                () -> {
                                                                                    client.execute(
                                                                                            () -> {
                                                                                                applyScale();
                                                                                            }
                                                                                    );
                                                                                }
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
