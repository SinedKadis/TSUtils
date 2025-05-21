package com.sacf.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {
	@Shadow @Final private static Logger LOGGER;
	@Shadow private net.minecraft.client.world.ClientWorld world;

	protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
		super(client, connection, connectionState);
	}

	@Inject(at = @At("HEAD"), method = "onEntityAttributes",cancellable = true)
	private void init(EntityAttributesS2CPacket packet,CallbackInfo info) {
		if (!client.isOnThread()) {
			NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
		}
		Entity entity = this.world.getEntityById(packet.getEntityId());
		if (entity != null) {
			if (!(entity instanceof LivingEntity)) {
				throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
			} else {
				AttributeContainer attributeContainer = ((LivingEntity)entity).getAttributes();
                for (EntityAttributesS2CPacket.Entry entry : packet.getEntries()) {
                    if (entry.attribute().equals(EntityAttributes.SCALE)){
						continue;
					}
					EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.attribute());
                    if (entityAttributeInstance == null) {
						LOGGER.warn("Entity {} does not have attribute {}", entity, entry.attribute().getIdAsString());
					} else {
                        entityAttributeInstance.setBaseValue(entry.base());
                        entityAttributeInstance.clearModifiers();
                        for (EntityAttributeModifier entityAttributeModifier : entry.modifiers()) {
                            entityAttributeInstance.addTemporaryModifier(entityAttributeModifier);
                        }
                    }
                }
			}
		}
		info.cancel();
	}
}