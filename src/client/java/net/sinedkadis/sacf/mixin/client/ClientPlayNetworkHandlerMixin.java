package net.sinedkadis.sacf.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.sinedkadis.sacf.binds.SizeAttributeBind;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.sinedkadis.sacf.binds.SizeAttributeBind.scaleAttributes;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {
	@Shadow @Final private static Logger LOGGER;
	@Shadow private net.minecraft.client.world.ClientWorld world;

	protected ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
		super(client, connection, connectionState);
	}

	@Inject(at = @At("HEAD"), method = "onEntityAttributes")
	private void onOnEntityAttributes(EntityAttributesS2CPacket packet, CallbackInfo info) {
		Entity entity = this.world.getEntityById(packet.getEntityId());
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		AttributeContainer attributeContainer = livingEntity.getAttributes();
		for (EntityAttributesS2CPacket.Entry entry : packet.getEntries()) {
			EntityAttributeInstance attributeInstance = attributeContainer.getCustomInstance(entry.attribute());
			if (attributeInstance != null) {
				if (entry.attribute().equals(EntityAttributes.GENERIC_SCALE)) {
					scaleAttributes.put(attributeContainer, entry);
					if (SizeAttributeBind.toggleState)
						continue;
				}
				attributeInstance.setBaseValue(entry.base());
				attributeInstance.clearModifiers();
				entry.modifiers().forEach(attributeInstance::addTemporaryModifier);
			} else {
				LOGGER.warn("Entity {} has no attribute {}", entity, entry.attribute().getIdAsString());
			}
		}
	}
	@Inject(method = "onEntitiesDestroy", at = @At("HEAD"))
	private void onEntityRemove(EntitiesDestroyS2CPacket packet, CallbackInfo ci) {
		for (int id : packet.getEntityIds()) {
            assert MinecraftClient.getInstance().world != null;
			Entity entityById = MinecraftClient.getInstance().world.getEntityById(id);
			if (entityById instanceof LivingEntity entity){
				AttributeContainer attributes = entity.getAttributes();
				scaleAttributes.remove(attributes);
			}
		}
	}
}