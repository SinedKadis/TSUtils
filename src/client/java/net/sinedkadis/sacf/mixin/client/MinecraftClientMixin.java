package net.sinedkadis.sacf.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.sinedkadis.sacf.binds.SizeAttributeBind.scaleAttributes;
import static net.sinedkadis.sacf.binds.SizeAttributeBind.toggleState;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onClientTick(CallbackInfo ci) {
        if (world != null) {
            if (!toggleState && !scaleAttributes.isEmpty()){
                for (Map.Entry<AttributeContainer, EntityAttributesS2CPacket.Entry> mapEntry : scaleAttributes.entrySet()){
                    AttributeContainer attributeContainer = mapEntry.getKey();
                    EntityAttributesS2CPacket.Entry entry = mapEntry.getValue();
                    if (entry == null) {
                        continue;
                    }
                    EntityAttributeInstance attributeInstance = attributeContainer.getCustomInstance(entry.attribute());
                    assert attributeInstance != null;
                    if (attributeInstance.getBaseValue() != entry.base()
                            ||attributeInstance.getModifiers() != entry.modifiers()) {
                        attributeInstance.setBaseValue(entry.base());
                        attributeInstance.clearModifiers();
                        entry.modifiers().forEach(attributeInstance::addTemporaryModifier);
                    }
                }
            }
            if (toggleState && !scaleAttributes.isEmpty()){
                for (Map.Entry<AttributeContainer, EntityAttributesS2CPacket.Entry> mapEntry : scaleAttributes.entrySet()){
                    AttributeContainer attributeContainer = mapEntry.getKey();
                    EntityAttributesS2CPacket.Entry entry = mapEntry.getValue();
                    if (entry == null) {
                        continue;
                    }
                    EntityAttributeInstance attributeInstance = attributeContainer.getCustomInstance(entry.attribute());
                    assert attributeInstance != null;
                    if (attributeInstance.getBaseValue() != entry.base()
                            ||attributeInstance.getModifiers() != entry.modifiers()) {
                        attributeInstance.setBaseValue(entry.attribute().value().getDefaultValue());
                        attributeInstance.clearModifiers();
                    }
                }
            }
        }
    }
}
